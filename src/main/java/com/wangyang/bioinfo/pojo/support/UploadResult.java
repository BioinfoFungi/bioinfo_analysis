package com.wangyang.bioinfo.pojo.support;

import lombok.Data;
import org.springframework.http.MediaType;

/**
 * @author wangyang
 * @date 2021/6/13
 */
@Data
public class UploadResult {
    private String filename;
    private String absolutePath;
    private String relativePath;
    private String key;
    private String thumbPath;
    private String suffix;
    private MediaType mediaType;
    private Long size;
    private Integer width;
    private Integer height;
}
