package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.trem.Study;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.param.StudyParam;
import com.wangyang.bioinfo.service.base.IBaseTermService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/26
 */
public interface IStudyService extends IBaseTermService<Study> {
    Study addStudy(StudyParam studyParam, User user);
    Study delStudy(int id);
    Study findStudyById(int id);
    Study findStudyByEnName(String name);


    @Override
    List<Study> listAll();
    Page<Study> pageStudy(Pageable pageable);
}
