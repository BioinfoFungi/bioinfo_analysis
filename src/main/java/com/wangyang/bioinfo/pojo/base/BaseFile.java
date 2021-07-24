package com.wangyang.bioinfo.pojo.base;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.base.BaseEntity;
import com.wangyang.bioinfo.pojo.enums.FileLocation;
import lombok.Data;

import javax.persistence.*;

/**
 * @author wangyang
 * @date 2021/6/26
 */
//@Entity(name = "t_base_file")
//@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")

@MappedSuperclass
@Data
public class BaseFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * 显示文件的名称
     */
    private String fileName;
    /**
     * 文件后缀
     */
    private String fileType;
    /**
     * 获取唯一文件的名称
     */
    private String enName;

    @Parsed(field = "absolute_path")
    private String absolutePath;
    private String relativePath;
    private Long size=0L;
    /**
     * 文件状态
     */
    private Boolean status=true;
    private Integer times=0;

    private FileLocation location = FileLocation.LOCAL;


    /**
     * 留用
     */
    private String fileKey;
    private Integer width;
    private Integer height;
    private Integer userId;

    private String md5;
    /**
     * 是否压缩
     */
    private Boolean isCompress=false;



    public Long getSize() {
        return size;
    }
}
