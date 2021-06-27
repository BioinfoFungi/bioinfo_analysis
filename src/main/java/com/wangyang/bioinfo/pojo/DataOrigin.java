package com.wangyang.bioinfo.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Entity(name = "t_data_origin")
@Getter
@Setter
public class DataOrigin extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String enName;
    private int userId;
}
