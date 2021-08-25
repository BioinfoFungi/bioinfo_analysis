package com.wangyang.bioinfo.pojo.authorize;

import com.wangyang.bioinfo.pojo.authorize.APIUser;
import lombok.Data;

import java.util.Set;

@Data
public class ApiUserDetailDTO extends APIUser {
    Set<Role> roles;
}
