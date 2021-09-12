package com.wangyang.bioinfo.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.handle.FileHandlers;
import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.entity.Code;
import com.wangyang.bioinfo.pojo.entity.Task;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.pojo.entity.CancerStudy;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.pojo.param.CancerStudyQuery;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVO;
import com.wangyang.bioinfo.repository.CancerStudyRepository;
import com.wangyang.bioinfo.repository.CodeRepository;
import com.wangyang.bioinfo.repository.TaskRepository;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.service.base.TermMappingServiceImpl;
import com.wangyang.bioinfo.util.BioinfoException;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Service
@Transactional
public class CancerStudyServiceImpl
        extends TermMappingServiceImpl<CancerStudy>
        implements ICancerStudyService {

    private  final CancerStudyRepository cancerStudyRepository;
    private  final ICancerService cancerService;
    private  final IStudyService studyService;
    private  final IDataOriginService dataOriginService;
    private  final IDataCategoryService dataCategoryService;
    private  final IAnalysisSoftwareService analysisSoftwareService;
    private  final TaskRepository taskRepository;
    private final CodeRepository codeRepository;
    public CancerStudyServiceImpl(FileHandlers fileHandlers,
                                  CancerStudyRepository cancerStudyRepository,
                                  ICancerService cancerService,
                                  IStudyService studyService,
                                  IDataOriginService dataOriginService,
                                  IDataCategoryService dataCategoryService,
                                  IAnalysisSoftwareService analysisSoftwareService,
                                  TaskRepository taskRepository,
                                  CodeRepository codeRepository) {
        super(fileHandlers, cancerStudyRepository,cancerService,studyService,dataOriginService,dataCategoryService,analysisSoftwareService);
        this.cancerStudyRepository=cancerStudyRepository;
        this.cancerService =cancerService;
        this.studyService=studyService;
        this.dataOriginService=dataOriginService;
        this.dataCategoryService=dataCategoryService;
        this.analysisSoftwareService=analysisSoftwareService;
        this.taskRepository=taskRepository;
        this.codeRepository = codeRepository;
    }




    @Override
    public CancerStudy saveCancerStudy(CancerStudyParam cancerStudyParam, User user) {
        checkAtLeastNotNull(cancerStudyParam);
//        if(cancerStudyParam.getCancer()==null | cancerStudyParam.getStudy()==null | cancerStudyParam.getDataOrigin()==null){
//            throw new BioinfoException("cancer，study，dataOrigin,AbsolutePath 不能为空！");
//        }
        CancerStudy cancerStudy = super.convert(cancerStudyParam);
        return  add(cancerStudy,user);
    }

    private void checkAtLeastNotNull(CancerStudyParam cancerStudyParam) {
        if(cancerStudyParam.getCancer()==null &&
                cancerStudyParam.getStudy()==null&&
                cancerStudyParam.getDataOrigin()==null&&
                cancerStudyParam.getAnalysisSoftware()==null&&
                cancerStudyParam.getDataCategory()==null){
            throw new BioinfoException("CancerStudy Term应该至少有一项不为空！！！");
        }
        if(cancerStudyParam.getParam()!=null && !cancerStudyParam.equals("")) {
            try {
                String param = cancerStudyParam.getParam();
                param = param.replace("\""," ");
                JSONObject.parseObject(param);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BioinfoException(e.getMessage());
            }
        }
    }

    @Override
    public CancerStudy updateCancerStudy(Integer id, CancerStudyParam cancerStudyParam, User user) {
        checkAtLeastNotNull(cancerStudyParam);
        CancerStudy cancerStudy = findById(id);
        CancerStudy convertCancerStudy = super.convert(cancerStudyParam,cancerStudy);
        return   add(convertCancerStudy,user);
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
    public CancerStudy findByParACodeId(Integer parentId, Integer codeId,Integer analysisSoftwareId){
        List<CancerStudy> cancerStudies = cancerStudyRepository.findAll(new Specification<CancerStudy>() {
            @Override
            public Predicate toPredicate(Root<CancerStudy> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(
                        criteriaBuilder.equal(root.get("parentId"),parentId),
                        criteriaBuilder.equal(root.get("codeId"),codeId),
                        criteriaBuilder.equal(root.get("analysisSoftwareId"),analysisSoftwareId)

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
        Set<String> sets = new HashSet<>();
        sets.add("gse");
        sets.add("description");
        Page<CancerStudy> cancerStudies = cancerStudyRepository.findAll(buildSpecByQuery(cancerStudy, cancerStudyQuery.getKeyword(), sets), pageable);
        return cancerStudies;
    }

    @Override
    public List<CancerStudy> listBy(CancerStudyQuery cancerStudyQuery){
        CancerStudy cancerStudy = super.convert(cancerStudyQuery);
        Set<String> sets = new HashSet<>();
        sets.add("gse");
        sets.add("description");
        List<CancerStudy> cancerStudies = cancerStudyRepository.findAll(buildSpecByQuery(cancerStudy, cancerStudyQuery.getKeyword(), sets));
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
        List<Task> tasks = taskRepository.findByObjIdAndTaskType(id, TaskType.CANCER_STUDY);
        taskRepository.deleteAll(tasks);
        return super.delBy(id);
    }

    @Override
    public Page<CancerStudyVO> convertVo(Page<CancerStudy> fromCancerStudies) {
        return super.convertVo(fromCancerStudies,CancerStudyVO.class);
    }

    @Override
    public List<CancerStudyVO> convertVo(List<CancerStudy> fromCancerStudies) {
        return super.convertVo(fromCancerStudies,CancerStudyVO.class);
    }

    /**
     * 导入cancer study
     * @param filePath
     * @return
     */
    @Override
    public List<CancerStudy> initData(String filePath,Boolean isEmpty) {
        return super.initData(filePath,CancerStudyParam.class,isEmpty);
    }



    @Override
    public CancerStudy findByParentIdAndCodeId(Integer id, Integer codeId) {
        return cancerStudyRepository.findByParentIdAndCodeId(id,codeId);
    }

    public  Specification<CancerStudy> specification(Code code,CancerStudyQuery cancerStudyQuery){
        Specification<CancerStudy> specification = new Specification<CancerStudy>() {
            @Override
            public Predicate toPredicate(Root<CancerStudy> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (code.getCancer() != null) {
                    predicates.add(criteriaBuilder.in(root.get("cancerId")).value(code.getCancer()));
                }
                if (code.getDataCategory() != null) {
                    predicates.add(criteriaBuilder.in(root.get("dataCategoryId")).value(code.getDataCategory()));
                }
                if (code.getDataOrigin() != null) {
                    predicates.add(criteriaBuilder.in(root.get("dataOriginId")).value(code.getDataOrigin()));
                }
                if (code.getStudy() != null) {
                    predicates.add(criteriaBuilder.in(root.get("studyId")).value(code.getStudy()));
                }
                if (code.getAnalysisSoftware() != null) {
                    predicates.add(criteriaBuilder.in(root.get("analysisSoftwareId")).value(code.getAnalysisSoftware()));
                }

                if(cancerStudyQuery!=null){
                    if(cancerStudyQuery.getKeyword()!=null){
                        String likeCondition = String
                                .format("%%%s%%", StringUtils.strip(cancerStudyQuery.getKeyword()));

                        Predicate predicate = criteriaBuilder.like(root.get("gse"), cancerStudyQuery.getKeyword());
                        predicates.add(predicate);
                    }
                }
                return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
            }
        };
        return specification;
    }
    @Override
    public List<CancerStudy> findByCode(Code code) {
        return cancerStudyRepository.findAll(specification(code,null));
    }

    @Override
    public Page<CancerStudy> pageByCodeId(Integer id,CancerStudyQuery cancerStudyQuery,Pageable pageable) {
        Optional<Code> code = codeRepository.findById(id);
        if(!code.isPresent()){
            throw new BioinfoException("code is not exist！");
        }
        return cancerStudyRepository.findAll(specification(code.get(),cancerStudyQuery),pageable);
    }

    @Override
    public CancerStudy checkExist(CancerStudy cancerStudy) {
        List<CancerStudy> cancerStudies = cancerStudyRepository.findAll(new Specification<CancerStudy>() {
            @Override
            public Predicate toPredicate(Root<CancerStudy> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (cancerStudy.getCancerId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("cancerId"),cancerStudy.getCancerId()));
                }
                if (cancerStudy.getDataCategoryId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("dataCategoryId"),cancerStudy.getDataCategoryId()));
                }
                if (cancerStudy.getDataOriginId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("dataOriginId"),cancerStudy.getDataOriginId()));
                }
                if (cancerStudy.getStudyId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("studyId"),cancerStudy.getStudyId()));
                }
                if (cancerStudy.getAnalysisSoftwareId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("analysisSoftwareId"),cancerStudy.getAnalysisSoftwareId()));
                }
                if (cancerStudy.getGse() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("gse"),cancerStudy.getGse()));
                }
                if (cancerStudy.getCodeId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("codeId"),cancerStudy.getCodeId()));
                }
                if (cancerStudy.getParentId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("parentId"),cancerStudy.getParentId()));
                }
                return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
            }
        });
        return cancerStudies.size()==0?null:cancerStudies.get(0);
    }
}