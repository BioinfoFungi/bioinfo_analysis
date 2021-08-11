package com.wangyang.bioinfo.service.impl;


import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.dto.DataCategoryIdDto;
import com.wangyang.bioinfo.pojo.enums.FileLocation;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.pojo.param.CancerStudyQuery;
import com.wangyang.bioinfo.pojo.param.FindCancer;
import com.wangyang.bioinfo.pojo.support.UploadResult;
import com.wangyang.bioinfo.pojo.trem.*;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVo;
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
    IWorkflowService experimentalStrategyService;

    @Autowired
    IAnalysisSoftwareService analysisSoftwareService;

    private  CancerStudy checkCancerStudy(CancerStudyParam cancerStudyParam){
        Cancer cancer = cancerService.findAndCheckByEnName(cancerStudyParam.getCancer());
        Study study = studyService.findAndCheckByEnName(cancerStudyParam.getStudy());
        DataOrigin dataOrigin = dataOriginService.findAndCheckByEnName(cancerStudyParam.getDataOrigin());

        Workflow workflow =experimentalStrategyService.findByEnName(cancerStudyParam.getWorkflow());
        AnalysisSoftware analysisSoftware=analysisSoftwareService.findByEnName(cancerStudyParam.getAnalysisSoftware());
        List<CancerStudy> cancerStudies = findDataByCategoryId(new DataCategoryIdDto(cancer.getId(),
                study.getId(),dataOrigin.getId(),
                workflow ==null?null: workflow.getId(),
                analysisSoftware==null?null:analysisSoftware.getId()));
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
            if(workflow !=null){
                cancerStudy.setWorkflowId(workflow.getId());
            }
        }
        String filename=dataOrigin.getEnName()+"_"+cancer.getEnName()+"_"+study.getEnName();
        if(workflow !=null){
            filename+="_"+ workflow.getEnName();
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
        List<CancerStudy> cancerStudies = findDataByCategoryId(new DataCategoryIdDto(cancerStudy.getCancerId(),
                cancerStudy.getStudyId(),cancerStudy.getDataOriginId(),
                cancerStudy.getWorkflowId()==null?null:cancerStudy.getWorkflowId(),
                cancerStudy.getAnalysisSoftwareId()==null?null:cancerStudy.getAnalysisSoftwareId()));
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




    @Override
    public  Page<CancerStudyVo> findCancerStudyVoStudy(FindCancer findCancer,Pageable pageable) {
        Cancer cancer = cancerService.findByEnName(findCancer.getCancer());
        Study study = studyService.findByEnName(findCancer.getStudy());
        DataOrigin dataOrigin = dataOriginService.findByEnName(findCancer.getDataOrigin());
        AnalysisSoftware analysisSoftware = analysisSoftwareService.findByEnName(findCancer.getAnalysisSoftware());
        Workflow workflow =experimentalStrategyService.findByEnName(findCancer.getWorkflow());;


        Page<CancerStudy> cancerStudyPage = pageBy(new DataCategoryIdDto(cancer==null?null:cancer.getId(),
                study==null?null:study.getId(),
                dataOrigin==null?null:dataOrigin.getId(),
                analysisSoftware==null?null:analysisSoftware.getId(),
                workflow ==null?null: workflow.getId(),
                findCancer.getFileName()==null?null:findCancer.getFileName()), pageable);
        Page<CancerStudyVo> cancerStudyVos = convertVo(cancerStudyPage,cancer,study,dataOrigin,analysisSoftware, workflow);
        return cancerStudyVos;
    }

    private Page<CancerStudyVo> convertVo(Page<CancerStudy> cancerStudyPage, Cancer cancer, Study study, DataOrigin dataOrigin, AnalysisSoftware analysisSoftware, Workflow workflow) {
        List<CancerStudy> cancerStudies = cancerStudyPage.getContent();

        Map<Integer, Workflow> strategyMap = null;
        if(workflow ==null) {
            Set<Integer> experimentalStrategyIds = ServiceUtil.fetchProperty(cancerStudies, CancerStudy::getWorkflowId);
            List<Workflow> experimentalStrategies = experimentalStrategyService.findAllById(experimentalStrategyIds);
            strategyMap= ServiceUtil.convertToMap(experimentalStrategies, Workflow::getId);
        }

        Map<Integer, AnalysisSoftware>  analysisSoftwareMap= null;
        if(analysisSoftware==null){
            Set<Integer> analysisSoftwareIds = ServiceUtil.fetchProperty(cancerStudies, CancerStudy::getAnalysisSoftwareId);
            List<AnalysisSoftware> analysisSoftwareList = analysisSoftwareService.findAllById(analysisSoftwareIds);
            analysisSoftwareMap= ServiceUtil.convertToMap(analysisSoftwareList, AnalysisSoftware::getId);
        }


        AnalysisSoftware finalAnalysisSoftware = analysisSoftware;
        Workflow finalWorkflow = workflow;
        Map<Integer, Workflow> finalStrategyMap = strategyMap;
        Map<Integer, AnalysisSoftware> finalAnalysisSoftwareMap = analysisSoftwareMap;

        return cancerStudyPage.map(cancerStudy -> {
            CancerStudyVo cancerStudyVo = new CancerStudyVo();
            cancerStudyVo.setCancer(cancer);
            cancerStudyVo.setDataOrigin(dataOrigin);
            cancerStudyVo.setStudy(study);
            if (finalAnalysisSoftware != null) {
                cancerStudyVo.setAnalysisSoftware(finalAnalysisSoftware);
            } else {

                cancerStudyVo.setAnalysisSoftware(finalAnalysisSoftwareMap.get(cancerStudy.getAnalysisSoftwareId()));
            }
            if (finalWorkflow != null) {
                cancerStudyVo.setWorkflow(finalWorkflow);
            } else {
                cancerStudyVo.setWorkflow(finalStrategyMap.get(cancerStudy.getWorkflowId()));
            }
            BeanUtils.copyProperties(cancerStudy, cancerStudyVo);
            return cancerStudyVo;
        });
    }

    @Override
    public  Page<CancerStudy> findCancerStudyStudy(FindCancer findCancer,Pageable pageable) {
        Cancer cancer = cancerService.findAndCheckByEnName(findCancer.getCancer());
        Study study = studyService.findAndCheckByEnName(findCancer.getStudy());
        DataOrigin dataOrigin = dataOriginService.findAndCheckByEnName(findCancer.getDataOrigin());
        AnalysisSoftware analysisSoftware = analysisSoftwareService.findAndCheckByEnName(findCancer.getAnalysisSoftware());
        Workflow workflow =experimentalStrategyService.findAndCheckByEnName(findCancer.getWorkflow());;

        DataCategoryIdDto dataCategoryIdDto = new DataCategoryIdDto(cancer == null ? null : cancer.getId(),
                study == null ? null : study.getId(),
                dataOrigin == null ? null : dataOrigin.getId(),
                analysisSoftware == null ? null : analysisSoftware.getId(),
                workflow == null ? null : workflow.getId());
        BeanUtils.copyProperties(findCancer,dataCategoryIdDto);
        Page<CancerStudy> cancerStudyPage = pageBy(dataCategoryIdDto, pageable);
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
    private  Specification<CancerStudy> buildSpecByQuery(CancerStudyQuery cancerStudyQuery){
        return (Specification<CancerStudy>) (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new LinkedList<>();
            if(cancerStudyQuery.getCancerId()!=null){
                Predicate predicate = criteriaBuilder.equal(root.get("cancerId"), cancerStudyQuery.getCancerId());
                predicates.add(predicate);
            }
            if(cancerStudyQuery.getDataOriginId()!=null){
                Predicate predicate = criteriaBuilder.equal(root.get("dataOriginId"), cancerStudyQuery.getDataOriginId());
                predicates.add(predicate);
            }
            if(cancerStudyQuery.getStudyId()!=null){
                Predicate predicate = criteriaBuilder.equal(root.get("studyId"), cancerStudyQuery.getStudyId());
                predicates.add(predicate);
            }


            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }

    @Override
    public Page<CancerStudy> pageCancerStudy(CancerStudyQuery cancerStudyQuery, Pageable pageable) {
        Page<CancerStudy> cancerStudies = cancerStudyRepository.findAll(buildSpecByQuery(cancerStudyQuery), pageable);
        return cancerStudies;

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


        Set<Integer> experimentalStrategyIds = ServiceUtil.fetchProperty(cancerStudies, CancerStudy::getWorkflowId);
        List<Workflow> experimentalStrategies = experimentalStrategyService.findAllById(experimentalStrategyIds);
        Map<Integer, Workflow> strategyMap= ServiceUtil.convertToMap(experimentalStrategies, Workflow::getId);


        Set<Integer> analysisSoftwareIds = ServiceUtil.fetchProperty(cancerStudies, CancerStudy::getAnalysisSoftwareId);
        List<AnalysisSoftware> analysisSoftwareList = analysisSoftwareService.findAllById(analysisSoftwareIds);
        Map<Integer, AnalysisSoftware>  analysisSoftwareMap= ServiceUtil.convertToMap(analysisSoftwareList, AnalysisSoftware::getId);


        List<CancerStudyVo> cancerStudyVos = cancerStudies.stream().map(cancerStudy -> {
            CancerStudyVo cancerStudyVo = new CancerStudyVo();
            cancerStudyVo.setCancer(cancerMap.get(cancerStudy.getCancerId()));
            cancerStudyVo.setStudy(studyMap.get(cancerStudy.getStudyId()));
            cancerStudyVo.setDataOrigin(dataOriginMap.get(cancerStudy.getDataOriginId()));
            BeanUtils.copyProperties(cancerStudy, cancerStudyVo);
            cancerStudyVo.setWorkflow(strategyMap.get(cancerStudy.getWorkflowId()));
            cancerStudyVo.setAnalysisSoftware(analysisSoftwareMap.get(cancerStudy.getAnalysisSoftwareId()));
            return cancerStudyVo;
        }).collect(Collectors.toList());

        return cancerStudyVos;
    }


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



        Set<Integer> experimentalStrategyIds = ServiceUtil.fetchProperty(cancerStudies, CancerStudy::getWorkflowId);
        List<Workflow> experimentalStrategies = experimentalStrategyService.findAllById(experimentalStrategyIds);
        Map<Integer, Workflow> strategyMap= ServiceUtil.convertToMap(experimentalStrategies, Workflow::getId);


        Set<Integer> analysisSoftwareIds = ServiceUtil.fetchProperty(cancerStudies, CancerStudy::getAnalysisSoftwareId);
        List<AnalysisSoftware> analysisSoftwareList = analysisSoftwareService.findAllById(analysisSoftwareIds);
        Map<Integer, AnalysisSoftware>  analysisSoftwareMap= ServiceUtil.convertToMap(analysisSoftwareList, AnalysisSoftware::getId);


        Page<CancerStudyVo> cancerStudyVos = fromCancerStudies.map(cancerStudy -> {
            CancerStudyVo cancerStudyVo = new CancerStudyVo();
            cancerStudyVo.setCancer(cancerMap.get(cancerStudy.getCancerId()));
            cancerStudyVo.setStudy(studyMap.get(cancerStudy.getStudyId()));
            cancerStudyVo.setDataOrigin(dataOriginMap.get(cancerStudy.getDataOriginId()));
            cancerStudyVo.setWorkflow(strategyMap.get(cancerStudy.getWorkflowId()));
            cancerStudyVo.setAnalysisSoftware(analysisSoftwareMap.get(cancerStudy.getAnalysisSoftwareId()));
            BeanUtils.copyProperties(cancerStudy,cancerStudyVo);
            return cancerStudyVo;
        });
        return cancerStudyVos;
    }
}
