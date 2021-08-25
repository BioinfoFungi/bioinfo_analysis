package com.wangyang.bioinfo.pojo.authorize;

import lombok.Data;

import java.util.Set;

/**
 * @author wangyang
 * @date 2021/6/14
 */
@Data
public class LoginUser {
    private int id;
    private String username;
    private String avatar;
    private String email;
    private Integer gender;
    private String token;
}
