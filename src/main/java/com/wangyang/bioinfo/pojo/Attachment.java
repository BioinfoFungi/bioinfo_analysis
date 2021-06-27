package com.wangyang.bioinfo.pojo;

import com.wangyang.bioinfo.pojo.enums.AttachmentType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author wangyang
 * @date 2021/6/13
 */
@Getter
@Setter
@Entity(name = "t_attachment")
public class Attachment extends BaseFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//    private String name;

    private String fileKey;
//    private String suffix;
    private Long size;
    private AttachmentType type;
//    private String mediaType;
    private Integer width;
    private Integer height;
    private String thumbPath;

    private Integer projectId;
    private Integer userId;
}
