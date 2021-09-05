package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.trem.DataOrigin;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.param.DataOriginParam;
import com.wangyang.bioinfo.service.base.IBaseTermService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/26
 */
public interface IDataOriginService extends IBaseTermService<DataOrigin> {
    DataOrigin add(DataOriginParam dataOriginParam,User user);
    DataOrigin findDataOriginById(int id);
    DataOrigin findDataOriginByEnName(String name);

    @Override
    List<DataOrigin> listAll();
    Page<DataOrigin> pageDataOrigin(Pageable pageable);

    DataOrigin update(Integer id, DataOriginParam dataOriginParam, User user);
}
