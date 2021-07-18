package com.wangyang.bioinfo.handle;

import com.wangyang.bioinfo.pojo.enums.FileLocation;
import com.wangyang.bioinfo.pojo.support.UploadResult;
import com.wangyang.bioinfo.util.BioinfoException;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author wangyang
 * @date 2021/6/13
 */
@Component
public class FileHandlers {
    private final Collection<FileHandler> fileHandlers = new LinkedList<>();
    public FileHandlers(ApplicationContext applicationContext) {
        // Add all file handler
        addFileHandlers(applicationContext.getBeansOfType(FileHandler.class).values());
    }
    public UploadResult upload(@NonNull MultipartFile file, @NonNull FileLocation fileLocation) {
        Assert.notNull(file, "Multipart file must not be null");
        Assert.notNull(fileLocation, "Attachment type must not be null");

        for (FileHandler fileHandler : fileHandlers) {
            if (fileHandler.supportType(fileLocation)) {
                return fileHandler.upload(file);
            }
        }
        throw new BioinfoException("文件长传出错!");
    }
    public UploadResult uploadFixed(@NonNull MultipartFile file,String path, @NonNull FileLocation fileLocation) {
        Assert.notNull(file, "Multipart file must not be null");
        Assert.notNull(fileLocation, "Attachment type must not be null");

        for (FileHandler fileHandler : fileHandlers) {
            if (fileHandler.supportType(fileLocation)) {
                return fileHandler.uploadFixed(file,path);
            }
        }
        throw new BioinfoException("文件长传出错!");
    }
    public UploadResult upload(@NonNull MultipartFile file, @NonNull FileLocation fileLocation, String path, String name, String suffix) {
        Assert.notNull(file, "Multipart file must not be null");
        Assert.notNull(fileLocation, "Attachment type must not be null");

        for (FileHandler fileHandler : fileHandlers) {
            if (fileHandler.supportType(fileLocation)) {
                return fileHandler.upload(file,path,name,suffix);
            }
        }
        throw new BioinfoException("文件长传出错!");
    }

    public UploadResult upload(@NonNull MultipartFile file, @NonNull FileLocation fileLocation, String fullPath) {
        Assert.notNull(file, "Multipart file must not be null");
        Assert.notNull(fileLocation, "Attachment type must not be null");

        for (FileHandler fileHandler : fileHandlers) {
            if (fileHandler.supportType(fileLocation)) {
                return fileHandler.upload(file,fullPath);
            }
        }
        throw new BioinfoException("文件长传出错!");
    }




    public FileHandlers addFileHandlers(@Nullable Collection<FileHandler> fileHandlers) {
        if (!CollectionUtils.isEmpty(fileHandlers)) {
            this.fileHandlers.addAll(fileHandlers);
        }
        return this;
    }
}
