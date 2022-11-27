package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.entity.Menu;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.repository.MenuRepository;
import com.wangyang.bioinfo.repository.base.BaseTreeRepository;
import com.wangyang.bioinfo.service.IMenuService;
import com.wangyang.bioinfo.service.base.AbstractBaseTreeServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl extends AbstractBaseTreeServiceImpl<Menu> implements IMenuService {

    private MenuRepository menuRepository;
    public MenuServiceImpl(MenuRepository menuRepository) {
        super(menuRepository);
        this.menuRepository =menuRepository;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
