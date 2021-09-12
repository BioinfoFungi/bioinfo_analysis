package com.wangyang.bioinfo.pojo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author wangyang
 * @date 2021/6/14
 * 项目负责人的添加
 */
@Entity(name = "t_project_user")
@Getter
@Setter
public class ProjectUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private int projectId;
}
