package com.wangyang.bioinfo.pojo.authorize;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author wangyang
 * @date 2021/6/14
 */
@Data
public class LoginUser {
    private Integer id;
    private String username;
    private String avatar;
    private String email;
    private Integer gender;
    private String token;
    private List<Role> roles;

}
