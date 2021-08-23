package com.wangyang.bioinfo.pojo.param;

import com.wangyang.bioinfo.pojo.dto.term.BaseFileDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangyang
 * @date 2021/6/14
 */
@Data
public class AttachmentParam extends BaseFileDTO {
    @NotBlank(message = "enName不能为空!")
    private String enName;
    private Integer projectId;
    private String fileName;
}
