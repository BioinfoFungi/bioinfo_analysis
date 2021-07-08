package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Attachment;
import com.wangyang.bioinfo.pojo.CancerStudy;
import com.wangyang.bioinfo.pojo.param.AttachmentParam;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.pojo.param.CancerStudyQuery;
import com.wangyang.bioinfo.pojo.param.FindCancer;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVo;
import com.wangyang.bioinfo.service.base.IBaseFileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/26
 */
public interface ICancerStudyService extends IBaseFileService<CancerStudy> {
    CancerStudy addCancerStudy(CancerStudyParam cancerStudyParam);
    CancerStudy upload(@NonNull MultipartFile file, CancerStudyParam cancerStudyParam);
    CancerStudy delCancerStudy(int id);
    CancerStudy findCancerStudyById(int id);
    CancerStudy findCancerStudyByAndThree(int cancerId,int studyId,int dataOriginId);
    CancerStudy findCancerStudyByAndThree(FindCancer findCancer);
    List<CancerStudy> findAllById(Collection<Integer> id);
    List<CancerStudy> listAll();
    Page<CancerStudy> pageCancerStudy(Pageable pageable);
    Page<CancerStudy> pageCancerStudy(CancerStudyQuery cancerStudyQuery, Pageable pageable);
    Page<CancerStudyVo> convertProjectVo( Page<CancerStudy> cancerStudies);

}
