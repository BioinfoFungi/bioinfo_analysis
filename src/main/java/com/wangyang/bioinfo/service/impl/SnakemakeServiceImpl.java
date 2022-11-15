package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.entity.Sample;
import com.wangyang.bioinfo.pojo.entity.snakemake.Snakemake;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.repository.SnakemakeRepository;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import com.wangyang.bioinfo.service.ISnakemakeService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class SnakemakeServiceImpl extends AbstractCrudService<Snakemake,Integer> implements ISnakemakeService {

    private SnakemakeRepository snakemakeRepository;
    public SnakemakeServiceImpl(SnakemakeRepository snakemakeRepository) {
        super(snakemakeRepository);
        this.snakemakeRepository = snakemakeRepository;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
