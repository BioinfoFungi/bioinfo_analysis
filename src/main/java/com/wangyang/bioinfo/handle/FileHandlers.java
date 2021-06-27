package com.wangyang.bioinfo.handle;

import com.wangyang.bioinfo.pojo.enums.AttachmentType;
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
    public UploadResult upload(@NonNull MultipartFile file, @NonNull AttachmentType attachmentType) {
        Assert.notNull(file, "Multipart file must not be null");
        Assert.notNull(attachmentType, "Attachment type must not be null");

        for (FileHandler fileHandler : fileHandlers) {
            if (fileHandler.supportType(attachmentType)) {
                return fileHandler.upload(file);
            }
        }
        throw new BioinfoException("文件长传出错!");
    }

    public UploadResult upload(@NonNull MultipartFile file, @NonNull AttachmentType attachmentType,String path,String name,String suffix) {
        Assert.notNull(file, "Multipart file must not be null");
        Assert.notNull(attachmentType, "Attachment type must not be null");

        for (FileHandler fileHandler : fileHandlers) {
            if (fileHandler.supportType(attachmentType)) {
                return fileHandler.upload(file,path,name,suffix);
            }
        }
        throw new BioinfoException("文件长传出错!");
    }

    public UploadResult upload(@NonNull MultipartFile file, @NonNull AttachmentType attachmentType,String fullPath) {
        Assert.notNull(file, "Multipart file must not be null");
        Assert.notNull(attachmentType, "Attachment type must not be null");

        for (FileHandler fileHandler : fileHandlers) {
            if (fileHandler.supportType(attachmentType)) {
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
