package com.wangyang.bioinfo.repository.base;

import com.wangyang.bioinfo.pojo.entity.base.BaseTerm;
import com.wangyang.bioinfo.pojo.entity.base.BaseTree;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseTreeRepository <BASETREE extends BaseTree> extends BaseRepository<BASETREE, Integer> {
}
