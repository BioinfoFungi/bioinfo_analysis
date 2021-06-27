package com.wangyang.bioinfo.pojo.param;

import com.wangyang.bioinfo.pojo.enums.AttachmentType;
import lombok.Data;

/**
 * @author wangyang
 * @date 2021/6/14
 */
@Data
public class AttachmentParam {
    private String fileName;
    private String fileType;
    private String path;

    private String fileKey;
    private String suffix;
    private Long size;
    private AttachmentType type;
    private Integer width;
    private Integer height;
    private String thumbPath;
    private Integer projectId;
    private Integer userId;
//    private Integer userId;
}
