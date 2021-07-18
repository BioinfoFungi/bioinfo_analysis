package com.wangyang.bioinfo.handle;

import com.wangyang.bioinfo.pojo.enums.FileLocation;
import com.wangyang.bioinfo.pojo.support.UploadResult;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wangyang
 * @date 2021/6/13
 */
public interface FileHandler {
    UploadResult upload(@NonNull MultipartFile file);
    UploadResult uploadFixed(@NonNull MultipartFile file,String path);
    UploadResult upload(@NonNull MultipartFile file,String path,String name,String suffix);
    UploadResult upload(@NonNull MultipartFile file,String fullPath);
    boolean supportType(@Nullable FileLocation type);
}
