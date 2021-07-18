package com.wangyang.bioinfo.pojo.param;

import lombok.Data;

/**
 * @author wangyang
 * @date 2021/7/17
 */
@Data
public class BaseFileParam {
    private String enName;
    private String fileName;
    private String absolutePath;
    private String fileType;
    private String relativePath;
    private Integer userId;

}
