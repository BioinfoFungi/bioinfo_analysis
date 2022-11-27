package com.wangyang.bioinfo.pojo.dto;

import com.wangyang.bioinfo.pojo.entity.Menu;
import lombok.Data;

import java.util.List;

@Data
public class PermMenu {
    private List<Menu> menus;
    private List<String> perms;
}
