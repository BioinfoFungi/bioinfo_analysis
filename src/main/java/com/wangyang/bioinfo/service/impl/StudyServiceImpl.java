package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.Cancer;
import com.wangyang.bioinfo.pojo.Study;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.param.StudyParam;
import com.wangyang.bioinfo.repository.StudyRepository;
import com.wangyang.bioinfo.service.IStudyService;
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
public class StudyServiceImpl extends BaseTermServiceImpl<Study> implements IStudyService {
    @Autowired
    StudyRepository studyRepository;

    @Override
    public Study addStudy(StudyParam studyParam, User user) {
        Study study = findStudyByEnName(studyParam.getEnName());
        if(study==null){
            study = new Study();
        }
        study.setUserId(user.getId());
        BeanUtils.copyProperties(studyParam,study);
        return studyRepository.save(study);
    }

    @Override
    public Study delStudy(int id) {
        return null;
    }

    @Override
    public Study findStudyById(int id) {
        Optional<Study> studyOptional = studyRepository.findById(id);
        if(!studyOptional.isPresent()){
            throw new BioinfoException("要操作的Study不存在！");
        }
        Study study = studyOptional.get();
        return study;
    }

    @Override
    public Study findStudyByEnName(String name) {
        List<Study> studyList = studyRepository.findAll(new Specification<Study>() {
            @Override
            public Predicate toPredicate(Root<Study> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("enName"),name)).getRestriction();
            }
        });
        if(studyList.size()==0){
           return null;
        }
        return studyList.get(0);
    }

    @Override
    public Study findAndCheckByEnName(String name) {
        Study study = findStudyByEnName(name);
        if(study==null){
            throw new BioinfoException("查找的Study对象不存在！");
        }
        return study;
    }

    @Override
    public List<Study> findAllById(Collection<Integer> id) {
        List<Study> studies = studyRepository.findAllById(id);
        return studies;
    }

    @Override
    public List<Study> listAll() {
        return studyRepository.findAll();
    }

    @Override
    public Page<Study> pageStudy(Pageable pageable) {
        return studyRepository.findAll(pageable);
    }
}
