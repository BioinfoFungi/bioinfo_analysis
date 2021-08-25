package com.wangyang.bioinfo.pojo.vo;

import com.wangyang.bioinfo.pojo.enums.ProjectStatus;
import com.wangyang.bioinfo.pojo.authorize.User;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author wangyang
 * @date 2021/6/13
 */
@Data
public class ProjectVo {
    private Date createDate;
    private Date updateDate;
    private int id;
    private String name;
    private String description;
    private String jupyterUrl;
    private Date deadline;
    private ProjectStatus projectStatus;
    private Integer userId;
    private User user;
    private String originalContent;
    private String formatContent;
    private List<User> chargePerson;
    private Set<Integer> userIds;
}
