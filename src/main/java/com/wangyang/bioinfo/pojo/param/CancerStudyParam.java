package com.wangyang.bioinfo.pojo.param;

import lombok.Data;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Data
public class CancerStudyParam {
    private String fileName;
    private String path;
    private String cancer;
    private String study;
    private String dataOrigin;
//    private String localPath;
//    private String networkPath;
    private String fileType;
    private int userId;
    private int width;
    private int height;
}
