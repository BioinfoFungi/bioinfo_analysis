package com.wangyang.bioinfo.pojo.authorize;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "1")
@Data
public class APIUser extends BaseAuthorize{
    private String authorize;
}
