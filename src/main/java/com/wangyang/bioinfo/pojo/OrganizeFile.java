package com.wangyang.bioinfo.pojo;

import com.wangyang.bioinfo.pojo.base.BaseFile;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author wangyang
 * @date 2021/7/8
 */
@Data
@Entity(name = "t_organize_file")
public class OrganizeFile extends BaseFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String localPath;
    private String networkPath;
}
