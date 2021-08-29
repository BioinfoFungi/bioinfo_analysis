package com.wangyang.bioinfo.pojo.authorize;

import lombok.Data;

@Data
public class ResourceVO extends Resource{
    private int resourceRoleId;
    private int roleId;
}
