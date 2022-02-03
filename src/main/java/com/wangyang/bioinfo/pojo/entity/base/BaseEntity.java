package com.wangyang.bioinfo.pojo.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.univocity.parsers.annotations.Parsed;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wangyang
 * @date 2021/6/13
 */
@MappedSuperclass
@Data
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "myid")
    @GenericGenerator(name = "myid", strategy = "com.wangyang.bioinfo.support.ManualInsertGenerator")
    @Parsed
    protected Integer id;
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone="GMT+8")
    private Date createDate=new Date();

    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone="GMT+8")
    private Date updateDate=new Date();
}
