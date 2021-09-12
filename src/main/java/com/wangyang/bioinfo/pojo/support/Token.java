package com.wangyang.bioinfo.pojo.support;

import lombok.Data;

/**
 * @author wangyang
 * @date 2021/6/11
 */
@Data
public class Token {
    private String token;
    private long exp;

    public Token(String token, long exp) {
        this.token = token;
        this.exp = exp;
    }
}
