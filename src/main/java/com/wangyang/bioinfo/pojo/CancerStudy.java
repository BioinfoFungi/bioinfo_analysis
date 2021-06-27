package com.wangyang.bioinfo.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 具体的癌症研究
 * @author wangyang
 * @date 2021/6/26
 */
@Entity(name = "t_cancer_study")
@Getter
@Setter
public class CancerStudy extends BaseFile{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int cancerId;
    private int studyId;
    private int dataOriginId;
    private String localPath;
    private String networkPath;

    private int width;
    private int height;

    private int userId;
}
