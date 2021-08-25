package com.wangyang.bioinfo.pojo.authorize;

import com.wangyang.bioinfo.pojo.authorize.User;
import lombok.Data;

import java.util.Set;

@Data
public class UserDetailDTO extends User {
    Set<Role> roles;
}
