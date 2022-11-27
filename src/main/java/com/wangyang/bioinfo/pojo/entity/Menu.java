package com.wangyang.bioinfo.pojo.entity;

import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import com.wangyang.bioinfo.pojo.entity.base.BaseTree;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity(name = "t_menu")
public class Menu extends BaseTree {
    private String name;
    private String router;
    private String perms;
    private Integer type;
    private String icon;
    private String viewPath;
    private Boolean keepalive;
    private Boolean isShow;
    public Menu(){}
    public Menu(Integer id,Integer parentId,Integer orderNum,String name, String router, String perms, Integer type, String icon, String viewPath, Boolean keepalive, Boolean isShow) {
        this.id=id;
        this.parentId=parentId;
        this.orderNum=orderNum;
        this.name = name;
        this.router = router;
        this.perms = perms;
        this.type = type;
        this.icon = icon;
        this.viewPath = viewPath;
        this.keepalive = keepalive;
        this.isShow = isShow;
    }
}
