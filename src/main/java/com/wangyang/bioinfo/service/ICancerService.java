package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.entity.Cancer;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.param.CancerParam;
import com.wangyang.bioinfo.service.base.IBaseTermService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/26
 */
public interface ICancerService extends IBaseTermService<Cancer> {
    Cancer add(CancerParam cancerParam, User user);
    Cancer findCancerById(int id);
    Cancer findCancerByEnName(String name);
    List<Cancer> findAllById(Collection<Integer> id);
    @Override
    List<Cancer> listAll();
    Page<Cancer> pageCancer(Pageable pageable);

    Cancer update(Integer id, CancerParam cancerParam, User user);
}
