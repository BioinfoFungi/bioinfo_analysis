package com.wangyang.bioinfo.pojo.file;

import com.wangyang.bioinfo.pojo.base.BaseFile;
import lombok.Data;

import javax.persistence.*;

/**
 * @author wangyang
 * @date 2021/7/8
 */
@Data
@Entity(name = "t_organize_file")
@DiscriminatorValue(value = "1")
public class OrganizeFile extends BaseFile {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;

}
