package com.wangyang.bioinfo.pojo.entity;

import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.pojo.enums.TaskStatus;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import lombok.Data;

import javax.persistence.*;

/**
 * @author wangyang
 * @date 2021/7/24
 */
@Entity(name = "t_task")
@Data
public class Task extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    @QueryField
    private Integer codeId;
    private String name;
    @QueryField
    private Integer objId;
    private  TaskStatus taskStatus=TaskStatus.CREATE;
    private CrudType crudEnum;
//    private String[] param;
    @Column(columnDefinition = "longtext")
    private String runMsg;
    @Column(columnDefinition = "longtext")
    private String exception;
    @Column(columnDefinition = "longtext")
    private String generateCode;
    private String threadName;
//    private Boolean isSuccess=false;
    private Integer userId;
    @Column(columnDefinition = "longtext")
    private String svgJson;
    @QueryField
    private TaskType taskType;
    private String metadata;
    private String matrix;
    @Column(columnDefinition = "longtext",name = "t_group")
    private String metadataJson;
    @Column(columnDefinition = "longtext")
    private String metadataColumnNames;
    @Column(columnDefinition = "longtext",name = "t_groups")
    private String groups;
    @Column(columnDefinition = "longtext",name = "t_sample")
    private String matrixJson;
    @Column(columnDefinition = "longtext",name = "t_param")
    private String param;
}
