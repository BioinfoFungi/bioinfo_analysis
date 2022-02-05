package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.handle.FileHandlers;
import com.wangyang.bioinfo.pojo.entity.base.BaseFile;
import com.wangyang.bioinfo.pojo.enums.FileLocation;
import com.wangyang.bioinfo.pojo.support.FileTree;
import com.wangyang.bioinfo.pojo.support.UploadResult;
import com.wangyang.bioinfo.repository.base.BaseFileRepository;
import com.wangyang.bioinfo.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wangyang
 * @date 2021/7/8
 */
@Slf4j
public abstract class BaseFileService<FILE extends BaseFile>
        extends AbstractCrudService<FILE,Integer>
        implements IBaseFileService<FILE> {


    private final  BaseFileRepository<FILE> baseFileRepository;
    private final   FileHandlers fileHandlers;
    public BaseFileService(FileHandlers fileHandlers,BaseFileRepository<FILE> baseFileRepository) {
        super(baseFileRepository);
        this.baseFileRepository= baseFileRepository;
        this.fileHandlers = fileHandlers;
    }

    private final String[] CAN_EDIT_SUFFIX = {".sh",".R",".ftl", ".css", ".js", ".yaml", ".yml", ".properties"};

    public UploadResult upload(MultipartFile file,String path,String filename,String fileType){
        UploadResult uploadResult = fileHandlers.upload(file, FileLocation.LOCAL,path,filename,fileType);
        return uploadResult;
    }

    public UploadResult upload(MultipartFile file){
        UploadResult uploadResult = fileHandlers.upload(file, FileLocation.LOCAL);
        return uploadResult;
    }

    @Override
    public List<FILE> findByFileName(String fileName){
        return baseFileRepository.findAll(new Specification<FILE>() {
            @Override
            public Predicate toPredicate(Root<FILE> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("fileName"),fileName)).getRestriction();
            }
        });
    }

    @Override
    public FILE findByUUIDAndCheck(String uuid){
        FILE file = findByUUID(uuid);
        if (file==null){
            throw new BioinfoException("要查找的文件对象不存在!");
        }
        return file;
    }
    @Override
    public FILE findById(Integer Id){
        Optional<FILE> fileOptional = baseFileRepository.findById(Id);
        if(!fileOptional.isPresent()){
            throw new BioinfoException("查找的File对象不存在!");
        }
        return fileOptional.get();
    }
    @Override
    public FILE findByUUID(String uuid){
        List<FILE> files = baseFileRepository.findAll(new Specification<FILE>() {
            @Override
            public Predicate toPredicate(Root<FILE> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("uuid"),uuid)).getRestriction();
            }
        });
        if(files.size()==0){
            return null;
        }
        return files.get(0);
    }

    @Override
    public Page<FILE> pageBy(FILE baseFileQuery, String keywords,Pageable pageable,String ... filed) {
        Set<String> sets =Arrays.stream(filed).collect(Collectors.toSet());
        Page<FILE> page = baseFileRepository.findAll(buildSpecByQuery(baseFileQuery,keywords),pageable);
        return page;
    }

//    private Specification<FILE> buildSpecByQuery(FILE baseFileQuery,String keywords) {
//        return (Specification<FILE>) (root, query, criteriaBuilder) ->{
//            List<Predicate> predicates = toPredicate(baseFileQuery,root, query, criteriaBuilder);
//            if(keywords!=null){
//                String likeCondition = String
//                        .format("%%%s%%", StringUtils.strip(keywords));
//
//                Predicate name = criteriaBuilder.like(root.get("enName"), likeCondition);
//                Predicate fileName = criteriaBuilder
//                        .like(root.get("fileName"), likeCondition);
//                predicates.add(criteriaBuilder.or(name, fileName));
//            }likeCondition
//            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
//        };
//    }


//    @Override
//    @Transactional
//    public List<FILE> initData(String filePath, Boolean isEmpty) {
//        if(isEmpty){
//            truncateTable();
//        }
//        List<FILE> beans = tsvToBean(filePath);
//
//        if(beans==null){
//            throw new BioinfoException(filePath+" 不存在！");
//        }
//        if(beans.size()!=0){
//            beans.forEach(bean->{
//                if(bean.getRelativePath()!=null && bean.getAbsolutePath()==null){
//                    String workDir = CacheStore.getValue("workDir");
//                    Path path = Paths.get(workDir, bean.getRelativePath());
//                    bean.setAbsolutePath(path.toString());
//                }
//                saveAndCheckFile(bean);
//            });
//        }
//        return beans;
//    }

    @Override
    public FILE download(String uuid, FileLocation fileLocation,HttpServletResponse response){
        FILE file = findByUUIDAndCheck(uuid);
        return download(file,response);
    }

    @Override
    public FILE download(Integer id,FileLocation fileLocation, HttpServletResponse response){
        FILE file = findById(id);
        if(fileLocation!=null){
            file.setLocation(fileLocation);
        }
        return download(file,response);
    }

    public FILE download(FILE file, HttpServletResponse response){
        if(file.getLocation().equals(FileLocation.LOCAL)){
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                byte[] bytes = FileUtils.readFileToByteArray(new File(file.getAbsolutePath()));
                //写之前设置响应流以附件的形式打开返回值,这样可以保证前边打开文件出错时异常可以返回给前台
                response.setHeader("Content-Disposition","attachment;filename="+file.getFileName()+"."+file.getFileType());
//                response.setContentType("text/tab-separated-values");
                outputStream.write(bytes);
                outputStream.flush();
                outputStream.close();
                file.setTimes(file.getTimes()+1);
                return baseFileRepository.save(file);
            } catch (IOException e) {
                e.printStackTrace();
                throw new BioinfoException(e.getMessage());
            }
        }else if (file.getLocation().equals(FileLocation.ALIOSS)){
            try {
                String oss_url = CacheStore.getValue("oss_url") + "/" + file.getRelativePath();
                response.sendRedirect(oss_url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            file.setTimes(file.getTimes()+1);
            return baseFileRepository.save(file);
        }else {
            return null;
        }
    }

    public FILE saveAndCheckFile(FILE file){
        if(file.getUuid()==null){
            file.setUuid(UUID.randomUUID().toString());
        }
        file.setStatus(false);
        if(file.getAbsolutePath()!=null && !file.getAbsolutePath().equals("")){
            if(file.getFileName()==null){
                String basename = FilenameUtils.getBasename(file.getAbsolutePath());
                file.setFileName(basename);
            }
//            if(file.getRelativePath()==null){
            String relativePath = FilenameUtils.relativePath(file.getAbsolutePath());
            file.setRelativePath(relativePath);
//            }
            String extension = FilenameUtils.getExtension(file.getAbsolutePath());
            file.setFileType(extension);
            FileLocation location= file.getLocation();
            if(location.equals(FileLocation.LOCAL)){
                File f = new File(file.getAbsolutePath());
                if(f.exists()&& f.isFile()){
                    file.setStatus(true);
                    file.setSize(f.length());
                    String md5String = FileMd5Utils.getFileMD5String(f);
                    file.setMd5(md5String);
                }
            }
        }


        return baseFileRepository.save(file);
    }

    @Override
    public FILE checkFileExist(int id) {
        FILE file = findById(id);
        return saveAndCheckFile(file);
    }
    @Override
    public void saveContent(@NonNull String absolutePath, String content) {
        // Write file
        Path path = Paths.get(absolutePath);
        if(!path.toFile().exists()){
            throw new BioinfoException("文件不存在！");
        }
        try {
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new BioinfoException("保存内容失败 " + absolutePath);
        }
    }
    @Override
    public String getFileContent(@NonNull String absolutePath) {
        // Read file
        Path path = Paths.get(absolutePath);
        if(!path.toFile().exists()){
            throw new BioinfoException("文件不存在！");
        }
        try {
            byte[] bytes = Files.readAllBytes(path);
            String content = new String(bytes, StandardCharsets.UTF_8);
            return content;
        } catch (IOException e) {
            throw new BioinfoException("读取内容失败 " + absolutePath);
        }
    }

    @Override
    public List<FileTree> listFiles(@NotNull String strPath){
        Assert.notNull(strPath, "Root path must not be null");
        Path path = Paths.get(strPath);
        if(!path.toFile().exists()){
            throw new BioinfoException("目录不存在！");
        }
        return scan(path);
    }

    private boolean isEditable(@NonNull Path path) {
        Assert.notNull(path, "Path must not be null");

        boolean isEditable = Files.isReadable(path) && Files.isWritable(path);

        if (!isEditable) {
            return false;
        }

        // Check suffix
        for (String suffix : CAN_EDIT_SUFFIX) {
            if (path.toString().endsWith(suffix)) {
                return true;
            }
        }

        return false;
    }

    public List<FileTree> scan(@NonNull Path rootPath) {
        Assert.notNull(rootPath, "Root path must not be null");

        // Check file type
        if (!Files.isDirectory(rootPath)) {
            return Collections.emptyList();
        }

        try (Stream<Path> pathStream = Files.list(rootPath)) {
            List<FileTree> themeFiles = new LinkedList<>();

            pathStream.forEach(path -> {
                // Build theme file
                FileTree themeFile = new FileTree();
                themeFile.setName(path.getFileName().toString());
                themeFile.setPath(path.toString());
                themeFile.setIsFile(Files.isRegularFile(path));
                themeFile.setEditable(isEditable(path));

                if (Files.isDirectory(path)) {
                    themeFile.setNode(scan(path));
                }

                // Add to theme files
                themeFiles.add(themeFile);
            });

            // Sort with isFile param
            themeFiles.sort(new FileTree());

            return themeFiles;
        } catch (IOException e) {
            throw new BioinfoException("Failed to list sub files");
        }
    }



    public FILE upload(UploadResult uploadResult,FILE file) {
        file.setFileType(uploadResult.getSuffix());
        file.setRelativePath(uploadResult.getRelativePath());
        file.setAbsolutePath(uploadResult.getAbsolutePath());
        file.setSize(uploadResult.getSize());

        return saveAndCheckFile(file);
    }
}
