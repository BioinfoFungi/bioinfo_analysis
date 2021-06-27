package com.wangyang.bioinfo.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 关于癌症的研究类型
 * @author wangyang
 * @date 2021/6/26
 */
@Entity(name = "t_study")
@Getter
@Setter
public class Study extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String enName;
    private int userId;
}
