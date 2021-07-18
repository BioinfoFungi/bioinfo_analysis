package com.wangyang.bioinfo.pojo.param;

import com.wangyang.bioinfo.pojo.enums.FileLocation;
import lombok.Data;

/**
 * @author wangyang
 * @date 2021/6/14
 */
@Data
public class AttachmentParam extends BaseFileParam{

    private Integer projectId;
}
