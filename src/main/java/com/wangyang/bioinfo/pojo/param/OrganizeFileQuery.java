package com.wangyang.bioinfo.pojo.param;

import com.wangyang.bioinfo.pojo.dto.term.BaseFileDTO;
import lombok.Data;

@Data
public class OrganizeFileQuery extends BaseFileDTO {
    private String keywords;
}
