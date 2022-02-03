package com.wangyang.bioinfo.pojo.entity.base;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.enums.FileLocation;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

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
//    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "myid")
//    @GenericGenerator(name = "myid", strategy = "com.wangyang.bioinfo.support.ManualInsertGenerator")
//    @Parsed
//    private Integer id;
    /**
     * 显示文件的名称
     */
    private String fileName;
    /**
     * 文件后缀
     */
    private String fileType;
    @Parsed
    private String absolutePath;
    @Parsed
    private String relativePath;
    private Long size=0L;
    private Boolean status=true;
    private Integer times=0;
    @Parsed
    private FileLocation location = FileLocation.LOCAL;
    private Integer userId;
    private String uuid;
    private String md5;
}
