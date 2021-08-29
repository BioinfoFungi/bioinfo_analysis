package com.wangyang.bioinfo.pojo.authorize;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserParam {
    @NotBlank(message = "username不能为空!")
    private String username;
    private String avatar;
    @NotBlank(message = "password不能为空!")
    private String password;
    @NotBlank(message = "email不能为空!")
    private String email;
    private Integer gender;
}
