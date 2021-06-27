package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.DataOrigin;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.param.DataOriginParam;
import com.wangyang.bioinfo.repository.DataOriginRepository;
import com.wangyang.bioinfo.service.IDataOriginService;
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

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Service
public class DataOriginServiceImpl extends BaseTermServiceImpl<DataOrigin> implements IDataOriginService {
    @Autowired
    DataOriginRepository dataOriginRepository;

    @Override
    public DataOrigin addDataOrigin(DataOriginParam dataOriginParam, User user) {
        DataOrigin dataOrigin = findDataOriginByEnName(dataOriginParam.getEnName());
        if(dataOrigin==null){
            dataOrigin = new DataOrigin();
        }
        dataOrigin.setUserId(user.getId());
        BeanUtils.copyProperties(dataOriginParam,dataOrigin);

        return dataOriginRepository.save(dataOrigin);
    }

    @Override
    public DataOrigin delDataOrigin(int id) {
        return null;
    }

    @Override
    public DataOrigin findDataOriginById(int id) {
        return null;
    }

    @Override
    public DataOrigin findDataOriginByEnName(String name) {
        List<DataOrigin> dataOrigins = dataOriginRepository.findAll(new Specification<DataOrigin>() {
            @Override
            public Predicate toPredicate(Root<DataOrigin> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("enName"),name)).getRestriction();
            }
        });
        if(dataOrigins.size()==0){
            return null;
        }
        return dataOrigins.get(0);
    }

    @Override
    public DataOrigin findAndCheckByEnName(String name) {
        DataOrigin dataOrigin = findDataOriginByEnName(name);
        if(dataOrigin==null){
            throw new BioinfoException("要查找的对象DataOrigin不存在！");

        }
        return dataOrigin;
    }

    @Override
    public List<DataOrigin> findAllById(Collection<Integer> id) {
        return dataOriginRepository.findAllById(id);
    }

    @Override
    public List<DataOrigin> listAll() {
        return dataOriginRepository.findAll();
    }

    @Override
    public Page<DataOrigin> pageDataOrigin(Pageable pageable) {
        return dataOriginRepository.findAll(pageable);
    }
}
