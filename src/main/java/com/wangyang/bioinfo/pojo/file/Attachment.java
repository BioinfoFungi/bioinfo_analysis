package com.wangyang.bioinfo.pojo.file;

import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.enums.FileLocation;
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
@DiscriminatorValue(value = "3")
public class Attachment extends BaseFile {
    private String thumbPath;

    private Integer projectId;

}
