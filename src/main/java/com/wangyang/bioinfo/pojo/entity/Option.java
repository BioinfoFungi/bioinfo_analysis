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
 */
@Getter
@Setter
@Entity(name = "t_options")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String key_;
    private String value_;
}
