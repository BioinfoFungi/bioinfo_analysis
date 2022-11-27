package com.wangyang.bioinfo.pojo.entity.base;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class BaseTree extends BaseEntity{
    protected Integer parentId;
    protected Integer orderNum;
}
