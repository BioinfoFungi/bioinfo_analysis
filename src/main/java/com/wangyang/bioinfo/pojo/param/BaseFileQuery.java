package com.wangyang.bioinfo.pojo.param;

import lombok.Data;

/**
 * @author wangyang
 * @date 2021/7/8
 */
@Data
public class BaseFileQuery {
    private String fileName;
    private String enName;
    private String keyword;
    private String path;
}
