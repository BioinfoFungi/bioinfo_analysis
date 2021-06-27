package com.wangyang.bioinfo.pojo.param;

import com.wangyang.bioinfo.pojo.enums.ProjectStatus;
import lombok.Data;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@Data
public class ProjectQuery {
    private Integer projectStatus;
    private String name;
}
