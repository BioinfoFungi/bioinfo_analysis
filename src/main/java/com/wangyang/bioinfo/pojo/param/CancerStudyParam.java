package com.wangyang.bioinfo.pojo.param;

import lombok.Data;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Data
public class CancerStudyParam  extends BaseFileParam{

    private String cancer;
    private String study;
    private String dataOrigin;
}
