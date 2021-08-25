package com.wangyang.bioinfo.pojo.vo;

import com.wangyang.bioinfo.pojo.authorize.User;
import lombok.Data;

import java.util.Date;

/**
 * @author wangyang
 * @date 2021/6/13
 */
@Data
public class CommentVo {
    private Date createDate;
    private Date updateDate;
    private int id;
    private int userId;
    private int projectId;
    private String content;
    private User user;
}
