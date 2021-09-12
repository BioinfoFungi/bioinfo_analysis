package com.wangyang.bioinfo.pojo.entity;

import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @QueryField
    private Integer codeId;
    private String name;
    @QueryField
    private Integer objId;
    private  TaskStatus taskStatus;
//    private String[] param;
    @Column(columnDefinition = "longtext")
    private String runMsg;
    @Column(columnDefinition = "longtext")
    private String exception;
    @Column(columnDefinition = "longtext")
    private String result;
    @Column(columnDefinition = "longtext")
    private String sourceCode;
    private String threadName;
    private Boolean isSuccess=false;
    private Integer userId;

    @QueryField
    private TaskType taskType;
}
