package com.wangyang.bioinfo.pojo;

import lombok.Data;

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
    private int gender;
    private String token;
}
