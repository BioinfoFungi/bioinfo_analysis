package com.wangyang.bioinfo.pojo.file;

import com.wangyang.bioinfo.pojo.base.BaseFile;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
