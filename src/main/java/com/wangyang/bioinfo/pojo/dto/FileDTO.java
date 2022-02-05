package com.wangyang.bioinfo.pojo.dto;

import lombok.Data;

@Data
public class FileDTO {
    private String relativePath;
    private String absolutePath;
    private String fileName;
}
