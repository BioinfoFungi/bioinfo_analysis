package com.wangyang.bioinfo.service.impl;


import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.pojo.param.CancerStudyQuery;
import com.wangyang.bioinfo.pojo.trem.*;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVO;
import com.wangyang.bioinfo.pojo.vo.TermMappingVo;
import com.wangyang.bioinfo.repository.CancerStudyRepository;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.service.base.BaseDataCategoryServiceImpl;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.ServiceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Service
public class CancerStudyServiceImpl
        extends BaseDataCategoryServiceImpl<CancerStudy>
        implements ICancerStudyService {
    @Autowired
    CancerStudyRepository cancerStudyRepository;
    @Autowired
    ICancerService cancerService;
    @Autowired
    IStudyService studyService;
    @Autowired
    IDataOriginService dataOriginService;

    @Autowired
    IDataCategoryService dataCategoryService;

    @Autowired
    IAnalysisSoftwareService analysisSoftwareService;

    @Autowired
    ITaskService taskService;




    @Override
    public CancerStudy saveCancerStudy(CancerStudyParam cancerStudyParam, User user) {
        if(cancerStudyParam.getCancer()==null | cancerStudyParam.getStudy()==null | cancerStudyParam.getDataOrigin()==null){
            throw new BioinfoException("cancer，study，dataOrigin,AbsolutePath 不能为空！");
        }
        CancerStudy cancerStudy = super.convert(cancerStudyParam);
        return  add(cancerStudy,user);
    }

    @Override
    public CancerStudy updateCancerStudy(Integer id, CancerStudyParam cancerStudyParam, User user) {
        if(cancerStudyParam.getCancer()==null | cancerStudyParam.getStudy()==null | cancerStudyParam.getDataOrigin()==null){
            throw new BioinfoException("cancer，study，dataOrigin,AbsolutePath 不能为空！");
        }
        CancerStudy cancerStudy = super.convert(cancerStudyParam);
        return  update(id,cancerStudy,user);
    }




    @Override
    public CancerStudy saveCancerStudy(CancerStudy cancerStudy, User user) {
        cancerStudy.setUserId(user.getId());
        return saveAndCheckFile(cancerStudy);
    }

    @Override
    public CancerStudy saveCancerStudy(CancerStudy cancerStudy) {
        return saveAndCheckFile(cancerStudy);
    }
//    @Override
//    public CancerStudy updateCancerStudy(Integer id, CancerStudy cancerStudyParam, User user) {
//        CancerStudy cancerStudy = findById(id);
//        BeanUtils.copyProperties(cancerStudyParam,cancerStudy,"id");
//        cancerStudy.setUserId(user.getId());
//        return saveAndCheckFile(cancerStudy);
//    }

    @Override
    public CancerStudy upload(MultipartFile file, CancerStudyParam cancerStudyParam) {
        CancerStudy cancerStudy = super.convert(cancerStudyParam);
        return upload(file,cancerStudy);
    }




    @Override
    public CancerStudy findCancerStudyById(int id) {
        Optional<CancerStudy> cancerStudyOptional = cancerStudyRepository.findById(id);
        if(!cancerStudyOptional.isPresent()){
            throw new BioinfoException("Cancer Study 对象不存在!");
        }
        return cancerStudyOptional.get();
    }


    @Override
    public CancerStudy findByParACodeId(Integer parentId, Integer codeId){
        List<CancerStudy> cancerStudies = cancerStudyRepository.findAll(new Specification<CancerStudy>() {
            @Override
            public Predicate toPredicate(Root<CancerStudy> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(
                        criteriaBuilder.equal(root.get("parentId"),parentId),
                        criteriaBuilder.equal(root.get("codeId"),codeId)
                ).getRestriction();
            }
        });
        return cancerStudies.size()==0?null:cancerStudies.get(0);
    }


//    @Override
//    public  Page<CancerStudyVo> pageCancerStudyVo(CancerStudyQuery findCancer, Pageable pageable) {
//        Cancer cancer = cancerService.findByEnName(findCancer.getCancer());
//        Study study = studyService.findByEnName(findCancer.getStudy());
//        DataOrigin dataOrigin = dataOriginService.findByEnName(findCancer.getDataOrigin());
//        AnalysisSoftware analysisSoftware = analysisSoftwareService.findByEnName(findCancer.getAnalysisSoftware());
//        DataCategory dataCategory =dataCategoryService.findByEnName(findCancer.getDataCategory());;
//
//
////        Page<CancerStudy> cancerStudyPage = pageBy(new DataCategoryIdDto(cancer==null?null:cancer.getId(),
////                study==null?null:study.getId(),
////                dataOrigin==null?null:dataOrigin.getId(),
////                analysisSoftware==null?null:analysisSoftware.getId(),
////                dataCategory ==null?null: dataCategory.getId(),
////                findCancer.getFileName()==null?null:findCancer.getFileName()), pageable);
//        CancerStudy cancerStudyParam = new CancerStudy();
//        BeanUtils.copyProperties(findCancer,cancerStudyParam);
//        cancerStudyParam.setCancerId(cancer == null ? null : cancer.getId());
//        cancerStudyParam.setStudyId(study == null ? null : study.getId());
//        cancerStudyParam.setDataOriginId(dataOrigin == null ? null : dataOrigin.getId());
//        cancerStudyParam.setAnalysisSoftwareId(analysisSoftware == null ? null : analysisSoftware.getId());
//        cancerStudyParam.setDataCategoryId(dataCategory == null ? null : dataCategory.getId());
//        Page<CancerStudy> cancerStudyPage = pageBy(cancerStudyParam, findCancer.getKeyword(),pageable);
//
//        Page<CancerStudyVo> cancerStudyVos = convertVo(cancerStudyPage,cancer,study,dataOrigin,analysisSoftware, dataCategory);
//        return cancerStudyVos;
//    }


    




    @Override
    public List<CancerStudy> findAllById(Collection<Integer> id) {
        return null;
    }


    @Override
    public Page<CancerStudy> pageBy(CancerStudyQuery cancerStudyQuery, Pageable pageable) {
        CancerStudy cancerStudy = super.convert(cancerStudyQuery);
        Page<CancerStudy> cancerStudies = pageBy(cancerStudy, cancerStudyQuery.getKeyWard(), pageable);
        return cancerStudies;
    }

    @Override
    public Page<CancerStudy> pageCancerStudy(Pageable pageable) {

        return null;
    }
    @Override
    public List<CancerStudy> listByCancerId(Integer cancerId){
        return cancerStudyRepository.findAll(new Specification<CancerStudy>() {
            @Override
            public Predicate toPredicate(Root<CancerStudy> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("cancerId"),cancerId)).getRestriction();
            }
        });
    }


    @Override
    public CancerStudyVO  convertVo(CancerStudy cancerStudy) {
        return super.convertVo(cancerStudy, CancerStudyVO.class);
    }

    @Override
    public List<TermMappingVo> convertVo(List<CancerStudy> cancerStudies) {
        Set<Integer> cancerIds = ServiceUtil.fetchProperty(cancerStudies, CancerStudy::getCancerId);
        List<Cancer> cancers = cancerService.findAllById(cancerIds);
        Map<Integer, Cancer> cancerMap = ServiceUtil.convertToMap(cancers, Cancer::getId);


        Set<Integer> studyIds = ServiceUtil.fetchProperty(cancerStudies, CancerStudy::getStudyId);
        List<Study> studies = studyService.findAllById(studyIds);
        Map<Integer, Study> studyMap = ServiceUtil.convertToMap(studies, Study::getId);


        Set<Integer> dataOriginIds = ServiceUtil.fetchProperty(cancerStudies, CancerStudy::getDataOriginId);
        List<DataOrigin> dataOrigins = dataOriginService.findAllById(dataOriginIds);
        Map<Integer, DataOrigin> dataOriginMap = ServiceUtil.convertToMap(dataOrigins, DataOrigin::getId);


        Set<Integer> experimentalStrategyIds = ServiceUtil.fetchProperty(cancerStudies, CancerStudy::getDataCategoryId);
        List<DataCategory> experimentalStrategies = dataCategoryService.findAllById(experimentalStrategyIds);
        Map<Integer, DataCategory> strategyMap= ServiceUtil.convertToMap(experimentalStrategies, DataCategory::getId);


        Set<Integer> analysisSoftwareIds = ServiceUtil.fetchProperty(cancerStudies, CancerStudy::getAnalysisSoftwareId);
        List<AnalysisSoftware> analysisSoftwareList = analysisSoftwareService.findAllById(analysisSoftwareIds);
        Map<Integer, AnalysisSoftware>  analysisSoftwareMap= ServiceUtil.convertToMap(analysisSoftwareList, AnalysisSoftware::getId);


        List<TermMappingVo> termMappingVos = cancerStudies.stream().map(cancerStudy -> {
            TermMappingVo termMappingVo = new TermMappingVo();
            termMappingVo.setCancer(cancerMap.get(cancerStudy.getCancerId()));
            termMappingVo.setStudy(studyMap.get(cancerStudy.getStudyId()));
            termMappingVo.setDataOrigin(dataOriginMap.get(cancerStudy.getDataOriginId()));
            BeanUtils.copyProperties(cancerStudy, termMappingVo);
            termMappingVo.setDataCategory(strategyMap.get(cancerStudy.getDataCategoryId()));
            termMappingVo.setAnalysisSoftware(analysisSoftwareMap.get(cancerStudy.getAnalysisSoftwareId()));
            return termMappingVo;
        }).collect(Collectors.toList());

        return termMappingVos;
    }

//    private Page<CancerStudyVo> convertVo(Page<CancerStudy> cancerStudyPage, Cancer cancer, Study study, DataOrigin dataOrigin, AnalysisSoftware analysisSoftware, DataCategory dataCategory) {
//        List<CancerStudy> cancerStudies = cancerStudyPage.getContent();
//
//        Map<Integer, DataCategory> strategyMap = null;
//        if(dataCategory ==null) {
//            Set<Integer> experimentalStrategyIds = ServiceUtil.fetchProperty(cancerStudies, CancerStudy::getDataCategoryId);
//            List<DataCategory> experimentalStrategies = dataCategoryService.findAllById(experimentalStrategyIds);
//            strategyMap= ServiceUtil.convertToMap(experimentalStrategies, DataCategory::getId);
//        }
//
//        Map<Integer, AnalysisSoftware>  analysisSoftwareMap= null;
//        if(analysisSoftware==null){
//            Set<Integer> analysisSoftwareIds = ServiceUtil.fetchProperty(cancerStudies, CancerStudy::getAnalysisSoftwareId);
//            List<AnalysisSoftware> analysisSoftwareList = analysisSoftwareService.findAllById(analysisSoftwareIds);
//            analysisSoftwareMap= ServiceUtil.convertToMap(analysisSoftwareList, AnalysisSoftware::getId);
//        }
//
//
//        AnalysisSoftware finalAnalysisSoftware = analysisSoftware;
//        DataCategory finalDataCategory = dataCategory;
//        Map<Integer, DataCategory> finalStrategyMap = strategyMap;
//        Map<Integer, AnalysisSoftware> finalAnalysisSoftwareMap = analysisSoftwareMap;
//
//        return cancerStudyPage.map(cancerStudy -> {
//            CancerStudyVo cancerStudyVo = new CancerStudyVo();
//            cancerStudyVo.setCancer(cancer);
//            cancerStudyVo.setDataOrigin(dataOrigin);
//            cancerStudyVo.setStudy(study);
//            if (finalAnalysisSoftware != null) {
//                cancerStudyVo.setAnalysisSoftware(finalAnalysisSoftware);
//            } else {
//
//                cancerStudyVo.setAnalysisSoftware(finalAnalysisSoftwareMap.get(cancerStudy.getAnalysisSoftwareId()));
//            }
//            if (finalDataCategory != null) {
//                cancerStudyVo.setDataCategory(finalDataCategory);
//            } else {
//                cancerStudyVo.setDataCategory(finalStrategyMap.get(cancerStudy.getDataCategoryId()));
//            }
//            BeanUtils.copyProperties(cancerStudy, cancerStudyVo);
//            return cancerStudyVo;
//        });
//    }


    @Override
    public CancerStudy delBy(Integer id) {
        taskService.delByCanSId(id);
        return super.delBy(id);
    }

    @Override
    public Page<CancerStudyVO> convertVo(Page<CancerStudy> fromCancerStudies) {
        return super.convertVo(fromCancerStudies,CancerStudyVO.class);
    }

    /**
     * 导入cancer study
     * @param filePath
     * @return
     */
    @Override
    public List<CancerStudy> initData(String filePath) {
        return super.initData(filePath,CancerStudyParam.class);
    }
}
