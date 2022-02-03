package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.entity.Study;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.enums.CrudType;
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
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Service
@Transactional
public class StudyServiceImpl extends BaseTermServiceImpl<Study> implements IStudyService {
    @Autowired
    private final StudyRepository studyRepository;

    public StudyServiceImpl(StudyRepository studyRepository) {
        super(studyRepository);
        this.studyRepository=studyRepository;
    }

    @Override
    public Study add(StudyParam studyParam, User user) {
        Study study = new Study();
        BeanUtils.copyProperties(studyParam,study);
        study.setUserId(user.getId());
        return studyRepository.save(study);
    }

    @Override
    public Study update(Integer id, StudyParam studyParam, User user) {
        Study study = findById(id);
        BeanUtils.copyProperties(studyParam,study);
        study.setUserId(user.getId());
        return studyRepository.save(study);
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
    public List<Study> listAll() {
        return studyRepository.findAll();
    }

    @Override
    public Page<Study> pageStudy(Pageable pageable) {
        return studyRepository.findAll(pageable);
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
