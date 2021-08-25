package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.trem.Cancer;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.param.CancerParam;
import com.wangyang.bioinfo.repository.CancerRepository;
import com.wangyang.bioinfo.service.ICancerService;
import com.wangyang.bioinfo.service.base.BaseTermServiceImpl;
import com.wangyang.bioinfo.util.BioinfoException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Service
public class CancerServiceImpl extends BaseTermServiceImpl<Cancer> implements ICancerService {
    @Autowired
    CancerRepository cancerRepository;

    @Override
    public Cancer addCancer(CancerParam cancerParam, User user) {
        Cancer cancer = findCancerByEnName(cancerParam.getEnName());
        if(cancer==null){
            cancer = new Cancer();
        }
        cancer.setUserId(user.getId());
        BeanUtils.copyProperties(cancerParam,cancer);
        return cancerRepository.save(cancer);
    }

    @Override
    public Cancer delCancer(int id) {
        return null;
    }

    @Override
    public Cancer findCancerById(int id) {
        Optional<Cancer> cancerOptional = cancerRepository.findById(id);
        if(!cancerOptional.isPresent()){
            throw new BioinfoException("要操作的Cancer不存在！");
        }
        Cancer cancer = cancerOptional.get();
        return cancer;
    }

    @Override
    public Cancer findCancerByEnName(String name) {
        List<Cancer> cancerList = cancerRepository.findAll(new Specification<Cancer>() {
            @Override
            public Predicate toPredicate(Root<Cancer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("enName"),name)).getRestriction();
            }
        });
        if(cancerList.size()==0){
            return null;
        }
        return cancerList.get(0);
    }


    @Override
    public List<Cancer> findAllById(Collection<Integer> id) {
        List<Cancer> cancers = cancerRepository.findAllById(id);
        return cancers;
    }

    @Override
    public List<Cancer> listAll() {
        return cancerRepository.findAll();
    }

    @Override
    public Page<Cancer> pageCancer(Pageable pageable) {
        Page<Cancer> cancers = cancerRepository.findAll(pageable);
        return cancers;
    }
}
