package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.handle.FileHandlers;
import com.wangyang.bioinfo.pojo.Project;
import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.enums.FileLocation;
import com.wangyang.bioinfo.pojo.file.Attachment;
import com.wangyang.bioinfo.pojo.param.AttachmentParam;
import com.wangyang.bioinfo.pojo.param.BaseFileParam;
import com.wangyang.bioinfo.pojo.param.BaseFileQuery;
import com.wangyang.bioinfo.pojo.support.UploadResult;
import com.wangyang.bioinfo.repository.base.BaseFileRepository;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.FileUtil;
import com.wangyang.bioinfo.util.FilenameUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/8
 */

public class AbstractBaseFileService<FILE extends BaseFile>
        extends AbstractCrudService<FILE,Integer>
        implements IAbstractBaseFileService<FILE> {

    @Autowired
    BaseFileRepository<FILE> baseFileRepository;
    @Autowired
    protected  FileHandlers fileHandlers;



    public UploadResult upload(MultipartFile file,String path,String filename,String fileType){
        UploadResult uploadResult = fileHandlers.upload(file, FileLocation.LOCAL,path,filename,fileType);
        return uploadResult;
    }

    public UploadResult upload(MultipartFile file){
        UploadResult uploadResult = fileHandlers.upload(file, FileLocation.LOCAL);
        return uploadResult;
    }


    @Override
    public FILE findByEnNameAndCheck(String name){
        FILE file = findByEnName(name);
        if (file==null){
            throw new BioinfoException("要查找的文件对象不存在!");
        }
        return file;
    }

    @Override
    public FILE findByEnName(String name){
        List<FILE> files = baseFileRepository.findAll(new Specification<FILE>() {
            @Override
            public Predicate toPredicate(Root<FILE> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("enName"),name)).getRestriction();
            }
        });
        if(files.size()==0){
            return null;
        }
        return files.get(0);
    }
    @Override
    public Page<FILE> pageBy(BaseFileQuery baseFileQuery, Pageable pageable) {
        Page<FILE> page = baseFileRepository.findAll(buildSpecByQuery(baseFileQuery),pageable);
        return page;
    }

    private Specification<FILE> buildSpecByQuery(BaseFileQuery baseFileQuery) {
        return (Specification<FILE>) (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new LinkedList<>();
            if(baseFileQuery.getEnName()!=null){
                predicates.add(criteriaBuilder.equal(root.get("fileName"),baseFileQuery.getFileName()));
            }
            if(baseFileQuery.getKeyword()!=null){
                String likeCondition = String
                        .format("%%%s%%", StringUtils.strip(baseFileQuery.getKeyword()));
                Predicate name = criteriaBuilder.like(root.get("enName"), likeCondition);
                Predicate fileName = criteriaBuilder
                        .like(root.get("fileName"), likeCondition);

                predicates.add(criteriaBuilder.or(name, fileName));

            }
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }


    public FILE save(FILE inputFile) {
        FILE file = findByEnName(inputFile.getEnName());
        if (file==null){
            file = super.getInstance();
        }
        BeanUtils.copyProperties(inputFile,file);
        return super.add(file);
    }

    @Override
    public FILE download(String enName, HttpServletResponse response){
        FILE file = findByEnNameAndCheck(enName);
        return download(file,response);
    }


    public FILE download(FILE file, HttpServletResponse response){
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = FileUtils.readFileToByteArray(new File(file.getAbsolutePath()));
            //写之前设置响应流以附件的形式打开返回值,这样可以保证前边打开文件出错时异常可以返回给前台
            response.setHeader("Content-Disposition","attachment;filename="+file.getAbsolutePath());
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            file.setTimes(file.getTimes()+1);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BioinfoException(e.getMessage());
        }

    }

    public FILE saveAndCheckFile(FILE file){
        FileLocation location= file.getLocation();
        if(location.equals(FileLocation.LOCAL)){
            File f = new File(file.getAbsolutePath());
            if(f.exists()){
                file.setStatus(true);
                file.setSize(f.length());
            }else {
                file.setStatus(false);
            }

        }
        return baseFileRepository.save(file);
    }
    public FILE saveAndCheckFile(FILE file, BaseFileParam baseFileParam){
        FileUtil.checkPath(baseFileParam, file);
        return saveAndCheckFile(file);
    }

    @Override
    public FILE save(BaseFileParam baseFileParam) {
        FILE file = findByEnName(baseFileParam.getEnName());
        if (file==null){
            file = super.getInstance();
        }
        BeanUtils.copyProperties(baseFileParam,file);
        return saveAndCheckFile(file,baseFileParam);
    }
    @Override
    public FILE upload(MultipartFile multipartFile, String path,BaseFileParam baseFileParam) {
        UploadResult uploadResult = fileHandlers.uploadFixed(multipartFile, path,FileLocation.LOCAL);
        if(baseFileParam.getEnName()==null){
            String basename = FilenameUtils.getBasename(uploadResult.getAbsolutePath());
            baseFileParam.setEnName(basename);
        }

        FILE file = findByEnName(baseFileParam.getEnName());
        if(file==null){
            file =getInstance();
        }
        BeanUtils.copyProperties(baseFileParam,file);
        return upload(uploadResult,file,baseFileParam);
    }

    public FILE upload(UploadResult uploadResult,FILE file,BaseFileParam baseFileParam) {




        if(baseFileParam.getFileName()==null){
            String basename = FilenameUtils.getBasename(uploadResult.getAbsolutePath());
            file.setFileName(basename);
        }else {
            file.setFileName(baseFileParam.getEnName());
        }
        if(file.getFileType()==null){
            file.setFileType(uploadResult.getSuffix());
        }

        file.setRelativePath(uploadResult.getRelativePath());
        file.setAbsolutePath(uploadResult.getAbsolutePath());
        file.setSize(uploadResult.getSize());

        file = baseFileRepository.save(file);
        return file;
    }
}
