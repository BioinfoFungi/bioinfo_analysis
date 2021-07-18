package com.wangyang.bioinfo.handle;

import com.wangyang.bioinfo.pojo.enums.FileLocation;
import com.wangyang.bioinfo.pojo.support.UploadResult;
import com.wangyang.bioinfo.util.FilenameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangyang
 * @date 2021/6/13
 */
@Component
@Slf4j
public class LocalFileHandler implements FileHandler {
    @Value("${bioinfo.workDir}")
    private String workDir;
    ReentrantLock lock = new ReentrantLock();

    @Override
    public UploadResult upload(MultipartFile file, String path, String name,String suffix) {
        UploadResult uploadResult = new UploadResult();
        String  originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String basename = FilenameUtils.getBasename(originalFilename);
        if(name==null){
            name=basename;
        }
        if(suffix==null){
            suffix = extension;
        }
        String subFilePath =path+ "/"+name+"."+suffix;

        uploadResult.setRelativePath(subFilePath);
        uploadResult.setFilename(basename);
        uploadResult.setSuffix(extension);

        Path uploadPath = Paths.get(workDir, subFilePath);
        if(uploadPath.toFile().exists()){
            uploadPath.toFile().delete();
        }
        uploadResult.setAbsolutePath(uploadPath.toString());
        try {
            Files.createDirectories(uploadPath.getParent());
            //创建文件
            Files.createFile(uploadPath);
            file.transferTo(uploadPath);
            log.info("上传文件到",uploadPath.toFile().getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        long length = uploadPath.toFile().length();
//        uploadResult.setSize(length);
//        lock.lock();
//        try (InputStream uploadFileInputStream = new FileInputStream(uploadPath.toFile())) {
//
//        }finally {
//            lock.unlock();
//        }
        return uploadResult;
    }

    @Override
    public UploadResult upload(MultipartFile file) {
        return  this.upload(file,"upload",FilenameUtils.randomName(),null);
    }


    @Override
    public UploadResult uploadFixed(MultipartFile file,String path) {
        return  this.upload(file,path,null,null);
    }

    @Override
    public UploadResult upload(MultipartFile file, String fullPath) {
        Path path = Paths.get(fullPath);
        Path parent = path.getParent();
        Path fileName = path.getFileName();
        String basename = FilenameUtils.getBasename(fileName.toString());

        return upload(file,parent.toString(),basename,null);
    }

    @Override
    public boolean supportType(FileLocation type) {
        return FileLocation.LOCAL.equals(type);
    }
}
