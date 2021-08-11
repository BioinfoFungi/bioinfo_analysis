package com.wangyang.bioinfo.pojo;

import com.wangyang.bioinfo.pojo.base.BaseEntity;
import com.wangyang.bioinfo.pojo.enums.TaskStatus;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    private Integer codeId;
    private String name;
    private Integer objId;
    private  TaskStatus taskStatus;
    private String[] param;
    @Column(columnDefinition = "longtext")
    private String runMsg;
    @Column(columnDefinition = "longtext")
    private String exception;
    private String threadName;
    private Boolean isSuccess;
    private TaskType taskType;

}
