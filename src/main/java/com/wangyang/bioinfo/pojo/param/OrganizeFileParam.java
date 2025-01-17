package com.wangyang.bioinfo.pojo.param;

import com.wangyang.bioinfo.pojo.dto.term.BaseFileDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangyang
 * @date 2021/7/17
 */
@Data
public class OrganizeFileParam extends BaseFileDTO {
    @NotBlank(message = "enName不能为空!")
    private String enName;
    private String fileName;
}
