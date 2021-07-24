package com.wangyang.bioinfo.pojo;

import com.wangyang.bioinfo.pojo.base.BaseEntity;
import com.wangyang.bioinfo.pojo.enums.TaskStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

}
