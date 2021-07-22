package com.wangyang.bioinfo.pojo.vo;

import com.wangyang.bioinfo.pojo.Cancer;
import com.wangyang.bioinfo.pojo.DataOrigin;
import com.wangyang.bioinfo.pojo.Study;
import lombok.Data;

import java.util.Date;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Data
public class CancerStudyVo {
    private int id;
    private Date createDate;
    private Date updateDate;
    private Cancer cancer;
    private Study study;
    private DataOrigin dataOrigin;
    private String fileName;
    private String fileType;
    private Long size;
    private Integer width;
    private Integer height;
    private String absolutePath;
    private String relativePath;
    private Boolean status;
    private String enName;
    private Integer times;
    private String md5;
    private Boolean isCompress;
    private String processedAbsolutePath;
    private String processedRelativePath;
    private Integer processedId;
    private Boolean isProcessed;
}
