package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Cancer;
import com.wangyang.bioinfo.pojo.CancerStudy;
import com.wangyang.bioinfo.pojo.User;
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
    Cancer addCancer(CancerParam cancerParam, User user);
    Cancer delCancer(int id);
    Cancer findCancerById(int id);
    Cancer findCancerByEnName(String name);
    Cancer findAndCheckByEnName(String name);
    List<Cancer> findAllById(Collection<Integer> id);
    @Override
    List<Cancer> listAll();
    Page<Cancer> pageCancer(Pageable pageable);
}
