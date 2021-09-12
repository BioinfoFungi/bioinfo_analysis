package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.CancerStudy;
import com.wangyang.bioinfo.pojo.entity.Code;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.pojo.param.CancerStudyQuery;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVO;
import com.wangyang.bioinfo.service.base.ITermMappingService;
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
public interface ICancerStudyService extends ITermMappingService<CancerStudy> {

    CancerStudy saveCancerStudy(CancerStudyParam cancerStudyParam, User user);
    CancerStudy updateCancerStudy(Integer id,CancerStudyParam cancerStudyParam, User user);
    CancerStudy saveCancerStudy(CancerStudy cancerStudy, User user);

//    CancerStudy updateCancerStudy(Integer id, CancerStudy cancerStudyParam, User user);

    CancerStudy saveCancerStudy(CancerStudy cancerStudy);

    CancerStudy upload(@NonNull MultipartFile file, CancerStudyParam cancerStudyParam);

    CancerStudy findCancerStudyById(int id);
//    Page<CancerStudyVo> pageCancerStudyVo(CancerStudyQuery findCancer, Pageable pageable);

    CancerStudyVO convertVo(CancerStudy cancerStudy);
    CancerStudy findByParACodeId(Integer parentId, Integer codeId,Integer analysisSoftwareId);

    List<CancerStudy> findAllById(Collection<Integer> id);

    List<CancerStudy> listBy(CancerStudyQuery cancerStudyQuery);

    Page<CancerStudy> pageCancerStudy(Pageable pageable);

    Page<CancerStudy>  pageBy(CancerStudyQuery cancerStudyQuery, Pageable pageable);

//    Page<CancerStudy> pageCancerStudyById(CancerStudyQueryId cancerStudyQueryId, Pageable pageable);

    List<CancerStudy> listByCancerId(Integer cancerId);


    Page<CancerStudyVO> convertVo(Page<CancerStudy> fromCancerStudies);

    List<CancerStudyVO> convertVo(List<CancerStudy> fromCancerStudies);

    /**
     * 查找请求的CancerStudy是否生成
     * @param id
     * @return
     */
    CancerStudy findByParentIdAndCodeId(Integer id, Integer codeId);


    List<CancerStudy> findByCode(Code code);

    Page<CancerStudy> pageByCodeId(Integer id,CancerStudyQuery cancerStudyQuery, Pageable pageable);

    CancerStudy checkExist(CancerStudy cancerStudy);
}
