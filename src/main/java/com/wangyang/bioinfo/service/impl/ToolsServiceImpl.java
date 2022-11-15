package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.entity.snakemake.Snakemake;
import com.wangyang.bioinfo.pojo.entity.tools.Tools;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.repository.ToolsRepository;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import com.wangyang.bioinfo.service.ISnakemakeService;
import com.wangyang.bioinfo.service.IToolsService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class ToolsServiceImpl extends AbstractCrudService<Tools,Integer> implements IToolsService {

    private ToolsRepository toolsRepository;
    public ToolsServiceImpl(ToolsRepository toolsRepository) {
        super(toolsRepository);
        this.toolsRepository = toolsRepository;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
