package com.wangyang.bioinfo.pojo.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wangyang
 * @date 2021/6/13
 */
@MappedSuperclass
@Data
public class BaseEntity implements Serializable {
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone="GMT+8")
    private Date createDate=new Date();

    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone="GMT+8")
    private Date updateDate=new Date();
}
