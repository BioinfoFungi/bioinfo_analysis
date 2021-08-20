package com.wangyang.bioinfo.service.impl;


import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.enums.FileLocation;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.pojo.param.CancerStudyQuery;
import com.wangyang.bioinfo.pojo.support.UploadResult;
import com.wangyang.bioinfo.pojo.trem.*;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVo;
import com.wangyang.bioinfo.repository.CancerStudyRepository;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.service.base.BaseDataCategoryServiceImpl;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.File2Tsv;
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




    private  CancerStudy checkCancerStudy(CancerStudyParam cancerStudyParam){
        /**
         * Term进行了缓存
         */
        Cancer cancer = cancerService.findAndCheckByEnName(cancerStudyParam.getCancer());
        Study study = studyService.findAndCheckByEnName(cancerStudyParam.getStudy());
        DataOrigin dataOrigin = dataOriginService.findAndCheckByEnName(cancerStudyParam.getDataOrigin());
        DataCategory dataCategory =dataCategoryService.findAndCheckByEnName(cancerStudyParam.getDataCategory());
        AnalysisSoftware analysisSoftware=analysisSoftwareService.findAndCheckByEnName(cancerStudyParam.getAnalysisSoftware());

        CancerStudy cancerStudyDto = new CancerStudy();
        BeanUtils.copyProperties(cancerStudyParam,cancerStudyDto);
        cancerStudyDto.setCancerId(cancer == null ? null : cancer.getId());
        cancerStudyDto.setStudyId(study == null ? null : study.getId());
        cancerStudyDto.setDataOriginId(dataOrigin == null ? null : dataOrigin.getId());
        cancerStudyDto.setAnalysisSoftwareId(analysisSoftware == null ? null : analysisSoftware.getId());
        cancerStudyDto.setDataCategoryId(dataCategory == null ? null : dataCategory.getId());

        List<CancerStudy> cancerStudies = findDataByCategoryId(cancerStudyDto,null);
        if(cancerStudies.size()>1){
            throw new BioinfoException("查找到文件数目大于1!");
        }
        CancerStudy cancerStudy;
        if(cancerStudies.size()==1){
            cancerStudy = cancerStudies.get(0);
        }else {
            cancerStudy = new CancerStudy();
            cancerStudy.setCancerId(cancer.getId());
            cancerStudy.setStudyId(study.getId());
            cancerStudy.setDataOriginId(dataOrigin.getId());
            if(analysisSoftware!=null){
                cancerStudy.setAnalysisSoftwareId(analysisSoftware.getId());
            }
            if(dataCategory !=null){
                cancerStudy.setDataCategoryId(dataCategory.getId());
            }
        }
        String filename=dataOrigin.getEnName()+"_"+cancer.getEnName()+"_"+study.getEnName();
        if(dataCategory !=null){
            filename+="_"+ dataCategory.getEnName();
        }
        if(analysisSoftware!=null){
            filename+="_"+analysisSoftware.getEnName();
        }
        cancerStudy.setFileName(filename);

        BeanUtils.copyProperties(cancerStudyParam,cancerStudy);
        return cancerStudy;
    }

    @Override
    public CancerStudy saveCancerStudy(CancerStudyParam cancerStudyParam, User user) {
        CancerStudy cancerStudy = checkCancerStudy(cancerStudyParam);
        cancerStudy.setUserId(user.getId());
        return saveAndCheckFile(cancerStudy);
    }

    @Override
    public CancerStudy saveCancerStudy(CancerStudy cancerStudy, User user) {
        List<CancerStudy> cancerStudies = findDataByCategoryId(cancerStudy,null);
        if(cancerStudies.size()>1){
            throw new BioinfoException("查找到文件数目大于1!");
        }
        if(cancerStudies.size()==1){
            cancerStudy.setId(cancerStudies.get(0).getId());
        }
        cancerStudy.setUserId(user.getId());
        return saveAndCheckFile(cancerStudy);
    }

    @Override
    public CancerStudy upload(MultipartFile file, CancerStudyParam cancerStudyParam) {
        CancerStudy cancerStudy = checkCancerStudy(cancerStudyParam);
        UploadResult uploadResult = fileHandlers.uploadFixed(file,"data" ,FileLocation.LOCAL);
        return super.upload(uploadResult,cancerStudy);
    }

    @Override
    public CancerStudy delCancerStudy(int id) {
        return null;
    }

    @Override
    public CancerStudy findCancerStudyById(int id) {
        Optional<CancerStudy> cancerStudyOptional = cancerStudyRepository.findById(id);
        if(!cancerStudyOptional.isPresent()){
            throw new BioinfoException("Cancer Study 对象不存在!");
        }
        return cancerStudyOptional.get();
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
    public  Page<CancerStudy> pageCancerStudy(CancerStudyQuery cancerStudyQuery, Pageable pageable) {
        Cancer cancer = cancerService.findAndCheckByEnName(cancerStudyQuery.getCancer());
        Study study = studyService.findAndCheckByEnName(cancerStudyQuery.getStudy());
        DataOrigin dataOrigin = dataOriginService.findAndCheckByEnName(cancerStudyQuery.getDataOrigin());
        AnalysisSoftware analysisSoftware = analysisSoftwareService.findAndCheckByEnName(cancerStudyQuery.getAnalysisSoftware());
        DataCategory dataCategory =dataCategoryService.findAndCheckByEnName(cancerStudyQuery.getDataCategory());;

//        DataCategoryIdDto dataCategoryIdDto = new DataCategoryIdDto(cancer == null ? null : cancer.getId(),
//                study == null ? null : study.getId(),
//                dataOrigin == null ? null : dataOrigin.getId(),
//                dataCategory == null ? null : dataCategory.getId(),
//                analysisSoftware == null ? null : analysisSoftware.getId());
//        BeanUtils.copyProperties(findCancer,dataCategoryIdDto);

        CancerStudy cancerStudyParam = new CancerStudy();
        BeanUtils.copyProperties(cancerStudyQuery,cancerStudyParam);
        if(cancer != null ){
            cancerStudyParam.setCancerId(cancer.getId());
        }
        if(study != null ){
            cancerStudyParam.setStudyId(study.getId());
        }
        if(dataOrigin != null ){
            cancerStudyParam.setDataOriginId(dataOrigin.getId());
        }
        if(analysisSoftware != null ){
            cancerStudyParam.setAnalysisSoftwareId( analysisSoftware.getId());
        }
        if(dataCategory != null ){
            cancerStudyParam.setDataCategoryId(dataCategory.getId());
        }
        Page<CancerStudy> cancerStudyPage = pageBy(cancerStudyParam, cancerStudyQuery.getKeyword(),pageable);
        return cancerStudyPage;
    }



    @Override
    public List<CancerStudy> findAllById(Collection<Integer> id) {
        return null;
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
    public List<CancerStudyVo> convertVo(List<CancerStudy> cancerStudies) {
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


        List<CancerStudyVo> cancerStudyVos = cancerStudies.stream().map(cancerStudy -> {
            CancerStudyVo cancerStudyVo = new CancerStudyVo();
            cancerStudyVo.setCancer(cancerMap.get(cancerStudy.getCancerId()));
            cancerStudyVo.setStudy(studyMap.get(cancerStudy.getStudyId()));
            cancerStudyVo.setDataOrigin(dataOriginMap.get(cancerStudy.getDataOriginId()));
            BeanUtils.copyProperties(cancerStudy, cancerStudyVo);
            cancerStudyVo.setDataCategory(strategyMap.get(cancerStudy.getDataCategoryId()));
            cancerStudyVo.setAnalysisSoftware(analysisSoftwareMap.get(cancerStudy.getAnalysisSoftwareId()));
            return cancerStudyVo;
        }).collect(Collectors.toList());

        return cancerStudyVos;
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
    public Page<CancerStudyVo> convertVo(Page<CancerStudy> fromCancerStudies) {

        List<CancerStudy> cancerStudies = fromCancerStudies.getContent();
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


        Page<CancerStudyVo> cancerStudyVos = fromCancerStudies.map(cancerStudy -> {
            CancerStudyVo cancerStudyVo = new CancerStudyVo();
            cancerStudyVo.setCancer(cancerMap.get(cancerStudy.getCancerId()));
            cancerStudyVo.setStudy(studyMap.get(cancerStudy.getStudyId()));
            cancerStudyVo.setDataOrigin(dataOriginMap.get(cancerStudy.getDataOriginId()));
            cancerStudyVo.setDataCategory(strategyMap.get(cancerStudy.getDataCategoryId()));
            cancerStudyVo.setAnalysisSoftware(analysisSoftwareMap.get(cancerStudy.getAnalysisSoftwareId()));
            BeanUtils.copyProperties(cancerStudy,cancerStudyVo);
            return cancerStudyVo;
        });
        return cancerStudyVos;
    }


    /**
     * 导入cancer study
     * @param filePath
     * @return
     */
    @Override
    public List<CancerStudy> initData(String filePath) {
        cancerStudyRepository.deleteAll();
        List<CancerStudyParam> cancerStudyParams = File2Tsv.tsvToBean(CancerStudyParam.class, filePath);
        List<CancerStudy> cancerStudies = cancerStudyParams.stream().map(cancerStudyParam -> {
            CancerStudy cancerStudy = checkCancerStudy(cancerStudyParam);
            return saveAndCheckFile(cancerStudy);
        }).collect(Collectors.toList());
        return cancerStudies;
    }
}
