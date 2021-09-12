package com.wangyang.bioinfo.pojo.entity;

import com.wangyang.bioinfo.pojo.entity.base.BaseFile;
import lombok.Data;

import javax.persistence.*;

/**
 * @author wangyang
 * @date 2021/7/8
 */
@Data
@Entity(name = "t_organize_file")
//@DiscriminatorValue(value = "1")
public class OrganizeFile extends BaseFile {
    /**
     * 获取唯一文件的名称
     */
    private String enName;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;

}
