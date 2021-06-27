package com.wangyang.bioinfo.handle;

import com.wangyang.bioinfo.pojo.enums.AttachmentType;
import com.wangyang.bioinfo.pojo.support.UploadResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wangyang
 * @date 2021/6/13
 */
@Component
public class AliOssFileHandler implements FileHandler {
    @Override
    public UploadResult upload(MultipartFile file) {
        return null;
    }

    @Override
    public UploadResult upload(MultipartFile file, String path, String name,String suffix) {
        return null;
    }

    @Override
    public UploadResult upload(MultipartFile file, String fullPath) {
        return null;
    }

    @Override
    public boolean supportType(AttachmentType type) {
        return AttachmentType.ALIOSS.equals(type);
    }
}
