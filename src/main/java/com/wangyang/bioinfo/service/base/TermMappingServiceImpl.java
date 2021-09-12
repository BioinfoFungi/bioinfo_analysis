package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.handle.FileHandlers;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.*;
import com.wangyang.bioinfo.pojo.enums.FileLocation;
import com.wangyang.bioinfo.pojo.entity.base.TermMapping;
import com.wangyang.bioinfo.pojo.param.TermMappingParam;
import com.wangyang.bioinfo.pojo.support.UploadResult;
import com.wangyang.bioinfo.pojo.vo.TermMappingVo;
import com.wangyang.bioinfo.repository.base.BaseTermMappingRepository;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.CacheStore;
import com.wangyang.bioinfo.util.File2Tsv;
import com.wangyang.bioinfo.util.ServiceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/7/25
 */
public class TermMappingServiceImpl<TERMMAPPING extends TermMapping>
        extends BaseFileService<TERMMAPPING>
        implements ITermMappingService<TERMMAPPING> {

    private  final BaseTermMappingRepository<TERMMAPPING> baseTermMappingRepository;
    private  final ICancerService cancerService;
    private  final IStudyService studyService;
    private  final IDataOriginService dataOriginService;
    private  final IDataCategoryService dataCategoryService;
    private  final IAnalysisSoftwareService analysisSoftwareService;
    private  final FileHandlers fileHandlers;

    public TermMappingServiceImpl(FileHandlers fileHandlers,
                                  BaseTermMappingRepository<TERMMAPPING> baseTermMappingRepository,
                                  ICancerService cancerService,
                                  IStudyService studyService,
                                  IDataOriginService dataOriginService,
                                  IDataCategoryService dataCategoryService,
                                  IAnalysisSoftwareService analysisSoftwareService) {
        super(fileHandlers, baseTermMappingRepository);
        this.fileHandlers=fileHandlers;
        this.baseTermMappingRepository=baseTermMappingRepository;
        this.cancerService =cancerService;
        this.studyService=studyService;
        this.dataOriginService=dataOriginService;
        this.dataCategoryService=dataCategoryService;
        this.analysisSoftwareService=analysisSoftwareService;

    }


//    protected Specification<TERMMAPPING> buildSpecBy(TERMMAPPING termMapping,String keywords) {
//        return (Specification<TERMMAPPING>) (root, query, criteriaBuilder) ->{
//            List<Predicate> predicates = toPredicate(termMapping, root, query, criteriaBuilder);
//            if(keywords!=null){
//                String likeCondition = String
//                        .format("%%%s%%", StringUtils.strip(keywords));
//                Predicate fileName = criteriaBuilder.like(root.get("fileName"), likeCondition);
//
//                predicates.add(criteriaBuilder.or(fileName));
//            }
//            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
//        };
//    }
//
//
//
//    @Override
//    public Page<TERMMAPPING> pageBy(TERMMAPPING termMapping, String keyWard,Pageable pageable) {
//        Page<TERMMAPPING> categories = baseTermMappingRepository.findAll(buildSpecBy(termMapping,keyWard), pageable);
//        return categories;
//    }



//    @Override
//    public  <MAPPING extends TermMappingDTO>Page<TERMMAPPING> pageDTOBy(TermMappingDTO termMappingDTO, Pageable pageable) {
//        TermMappingDTO mappingDTO = convert2Id(termMappingDTO);
//        pagedddBy(mappingDTO, pageable);
////        Page<TERMMAPPING> cancerStudyPage = pageBy(mappingDTO, pageable);
//        return cancerStudyPage;
//    }
//    @Override
//    public List<TERMMAPPING> listBy(TERMMAPPING termMapping,String keyWard) {
//        List<TERMMAPPING> categories = baseTermMappingRepository.findAll(buildSpecBy(termMapping,keyWard));
//        return categories;
//    }
//    public TermMappingDTO convert2Id(TermMappingDTO termMappingDTO){
//        Cancer cancer = cancerService.findAndCheckByEnName(termMappingDTO.getCancer());
//        Study study = studyService.findAndCheckByEnName(termMappingDTO.getStudy());
//        DataOrigin dataOrigin = dataOriginService.findAndCheckByEnName(termMappingDTO.getDataOrigin());
//        AnalysisSoftware analysisSoftware = analysisSoftwareService.findAndCheckByEnName(termMappingDTO.getAnalysisSoftware());
//        DataCategory dataCategory =dataCategoryService.findAndCheckByEnName(termMappingDTO.getDataCategory());;
//
////        DataCategoryIdDto dataCategoryIdDto = new DataCategoryIdDto(cancer == null ? null : cancer.getId(),
////                study == null ? null : study.getId(),
////                dataOrigin == null ? null : dataOrigin.getId(),
////                dataCategory == null ? null : dataCategory.getId(),
////                analysisSoftware == null ? null : analysisSoftware.getId());
////        BeanUtils.copyProperties(findCancer,dataCategoryIdDto);
//
//        if(cancer != null ){
//            termMappingDTO.setCancerId(cancer.getId());
//        }
//        if(study != null ){
//            termMappingDTO.setStudyId(study.getId());
//        }
//        if(dataOrigin != null ){
//            termMappingDTO.setDataOriginId(dataOrigin.getId());
//        }
//        if(analysisSoftware != null ){
//            termMappingDTO.setAnalysisSoftwareId( analysisSoftware.getId());
//        }
//        if(dataCategory != null ){
//            termMappingDTO.setDataCategoryId(dataCategory.getId());
//        }
//        return termMappingDTO;
//    }

//    private TERMMAPPING checkCancerStudy(TermMappingParamDTO termMappingParamDTO, TERMMAPPING termMapping){
//        /**
//         * Term进行了缓存
//         */
//        Cancer cancer = cancerService.findAndCheckByEnName(termMappingParamDTO.getCancer());
//        Study study = studyService.findAndCheckByEnName(termMappingParamDTO.getStudy());
//        DataOrigin dataOrigin = dataOriginService.findAndCheckByEnName(termMappingParamDTO.getDataOrigin());
//        DataCategory dataCategory =dataCategoryService.findAndCheckByEnName(termMappingParamDTO.getDataCategory());
//        AnalysisSoftware analysisSoftware=analysisSoftwareService.findAndCheckByEnName(termMappingParamDTO.getAnalysisSoftware());
//
////        CancerStudy cancerStudyDto = new CancerStudy();
////        BeanUtils.copyProperties(cancerStudyParam,cancerStudyDto);
////        cancerStudyDto.setCancerId(cancer == null ? null : cancer.getId());
////        cancerStudyDto.setStudyId(study == null ? null : study.getId());
////        cancerStudyDto.setDataOriginId(dataOrigin == null ? null : dataOrigin.getId());
////        cancerStudyDto.setAnalysisSoftwareId(analysisSoftware == null ? null : analysisSoftware.getId());
////        cancerStudyDto.setDataCategoryId(dataCategory == null ? null : dataCategory.getId());
//
////        List<CancerStudy> cancerStudies = findDataByCategoryId(cancerStudyDto,null);
////        if(cancerStudies.size()>1){
////            throw new BioinfoException("查找到文件数目大于1!");
////        }
////        CancerStudy cancerStudy;
////        if(cancerStudies.size()==1){
////            cancerStudy = cancerStudies.get(0);
////        }else {
////
////        }
////        CancerStudy cancerStudy = new CancerStudy();
//        if(cancer==null | study==null | dataOrigin==null| termMappingParamDTO.getAbsolutePath()==null){
//            throw new BioinfoException("cancer，study，dataOrigin,AbsolutePath 不能为空！");
//        }
//        termMapping.setCancerId(cancer.getId());
//        termMapping.setStudyId(study.getId());
//        termMapping.setDataOriginId(dataOrigin.getId());
//        if(analysisSoftware!=null){
//            termMapping.setAnalysisSoftwareId(analysisSoftware.getId());
//        }
//        if(dataCategory !=null){
//            termMapping.setDataCategoryId(dataCategory.getId());
//        }
//        String filename=dataOrigin.getEnName()+"_"+cancer.getEnName()+"_"+study.getEnName();
//        if(dataCategory !=null){
//            filename+="_"+ dataCategory.getEnName();
//        }
//        if(analysisSoftware!=null){
//            filename+="_"+analysisSoftware.getEnName();
//        }
//        termMapping.setFileName(filename);
//
//        BeanUtils.copyProperties(termMappingParamDTO,termMapping);
//        return termMapping;
//    }

    public TERMMAPPING add(TERMMAPPING termmapping, User user) {
        termmapping.setUserId(user.getId());
        return saveAndCheckFile(termmapping);
    }
//    public TERMMAPPING update(Integer id, TERMMAPPING termMappingParam, User user) {
//        TERMMAPPING termmapping = findById(id);
//        termmapping.setUserId(user.getId());
//        BeanUtils.copyProperties(termMappingParam,termmapping,"id");
//        return saveAndCheckFile(termmapping);
//    }

//    public TERMMAPPING saveTermMapping(TermMappingParamDTO termMappingParamDTO, User user) {
//        TERMMAPPING termMapping = getInstance();
//        termMapping = checkCancerStudy(termMappingParamDTO,termMapping);
//        termMapping.setUserId(user.getId());
//        return saveAndCheckFile(termMapping);
//    }
//    public TERMMAPPING updateCancerStudy(Integer id, TermMappingParamDTO termMappingParamDTO, User user) {
//        TERMMAPPING termmapping = findById(id);
//        termmapping.setUserId(user.getId());
//        termmapping = checkCancerStudy(termMappingParamDTO,termmapping);
//        return saveAndCheckFile(termmapping);
//    }

    public TERMMAPPING upload(MultipartFile file, TERMMAPPING termmapping) {
        UploadResult uploadResult = fileHandlers.uploadFixed(file,"data" , FileLocation.LOCAL);
        return super.upload(uploadResult,termmapping);
    }


    public List<TERMMAPPING> initData(String filePath,Class<? extends TermMappingParam> clz,Boolean isEmpty) {
        if (!Paths.get(filePath).toFile().exists()){
            throw new BioinfoException("["+filePath+"]不存在！！");
        }
        if(isEmpty){
            truncateTable();
        }
//        baseTermMappingRepository.deleteAll();
        List<? extends TermMappingParam> termMappingParamDTOS = File2Tsv.tsvToBean(clz, filePath);
        List<TERMMAPPING> termmappings = baseTermMappingRepository.findAll();
        List<TERMMAPPING> importTermMappings = termMappingParamDTOS.stream().map(cancerStudyParam -> {
            TERMMAPPING termmapping = convert(cancerStudyParam);
            if(termmapping.getAbsolutePath()==null && termmapping.getRelativePath()!=null){
                String workDir = CacheStore.getValue("workDir");
                Path path = Paths.get(workDir, termmapping.getRelativePath());
                termmapping.setAbsolutePath(path.toString());
            }
            return termmapping;
        }).collect(Collectors.toList());
        importTermMappings.removeAll(termmappings);
        if(importTermMappings.size()!=0){
            importTermMappings.forEach(termmapping -> saveAndCheckFile(termmapping));
        }
        return importTermMappings;
    }




//    @Override
//    public List<? extends TermMappingVo> convertVo(List<TERMMAPPING> cancerStudies) {
//        Set<Integer> cancerIds = ServiceUtil.fetchProperty(cancerStudies, TERMMAPPING::getCancerId);
//        List<Cancer> cancers = cancerService.findAllById(cancerIds);
//        Map<Integer, Cancer> cancerMap = ServiceUtil.convertToMap(cancers, Cancer::getId);
//
//
//        Set<Integer> studyIds = ServiceUtil.fetchProperty(cancerStudies, TERMMAPPING::getStudyId);
//        List<Study> studies = studyService.findAllById(studyIds);
//        Map<Integer, Study> studyMap = ServiceUtil.convertToMap(studies, Study::getId);
//
//
//        Set<Integer> dataOriginIds = ServiceUtil.fetchProperty(cancerStudies, TERMMAPPING::getDataOriginId);
//        List<DataOrigin> dataOrigins = dataOriginService.findAllById(dataOriginIds);
//        Map<Integer, DataOrigin> dataOriginMap = ServiceUtil.convertToMap(dataOrigins, DataOrigin::getId);
//
//
//        Set<Integer> experimentalStrategyIds = ServiceUtil.fetchProperty(cancerStudies, TERMMAPPING::getDataCategoryId);
//        List<DataCategory> experimentalStrategies = dataCategoryService.findAllById(experimentalStrategyIds);
//        Map<Integer, DataCategory> strategyMap= ServiceUtil.convertToMap(experimentalStrategies, DataCategory::getId);
//
//
//        Set<Integer> analysisSoftwareIds = ServiceUtil.fetchProperty(cancerStudies, TERMMAPPING::getAnalysisSoftwareId);
//        List<AnalysisSoftware> analysisSoftwareList = analysisSoftwareService.findAllById(analysisSoftwareIds);
//        Map<Integer, AnalysisSoftware>  analysisSoftwareMap= ServiceUtil.convertToMap(analysisSoftwareList, AnalysisSoftware::getId);
//
//
//        List<TermMappingVo> termMappingVos = cancerStudies.stream().map(cancerStudy -> {
//            TermMappingVo termMappingVo = new TermMappingVo();
//            termMappingVo.setCancer(cancerMap.get(cancerStudy.getCancerId()));
//            termMappingVo.setStudy(studyMap.get(cancerStudy.getStudyId()));
//            termMappingVo.setDataOrigin(dataOriginMap.get(cancerStudy.getDataOriginId()));
//            BeanUtils.copyProperties(cancerStudy, termMappingVo);
//            termMappingVo.setDataCategory(strategyMap.get(cancerStudy.getDataCategoryId()));
//            termMappingVo.setAnalysisSoftware(analysisSoftwareMap.get(cancerStudy.getAnalysisSoftwareId()));
//            return termMappingVo;
//        }).collect(Collectors.toList());
//
//        return termMappingVos;
//    }

//    @Override
    @Override
    public TermMappingVo  convertVo(TERMMAPPING termmapping) {
        TermMappingVo TermMappingVo = convertVo(termmapping, TermMappingVo.class);
        return TermMappingVo;
    }

    /**
     * id -> obj
     * @param fromCancerStudies
     * @param clz
     * @param <VO>
     * @return
     */
    public <VO extends TermMappingVo> Page<VO> convertVo(Page<TERMMAPPING> fromCancerStudies,Class<VO> clz){
        List<TERMMAPPING> termmappings = fromCancerStudies.getContent();
        Set<Integer> cancerIds = ServiceUtil.fetchProperty(termmappings, TermMapping::getCancerId);
        List<Cancer> cancers = cancerService.findAllById(cancerIds);
        Map<Integer, Cancer> cancerMap = ServiceUtil.convertToMap(cancers, Cancer::getId);


        Set<Integer> studyIds = ServiceUtil.fetchProperty(termmappings, TermMapping::getStudyId);
        List<Study> studies = studyService.findAllById(studyIds);
        Map<Integer, Study> studyMap = ServiceUtil.convertToMap(studies, Study::getId);


        Set<Integer> dataOriginIds = ServiceUtil.fetchProperty(termmappings, TermMapping::getDataOriginId);
        List<DataOrigin> dataOrigins = dataOriginService.findAllById(dataOriginIds);
        Map<Integer, DataOrigin> dataOriginMap = ServiceUtil.convertToMap(dataOrigins, DataOrigin::getId);



        Set<Integer> experimentalStrategyIds = ServiceUtil.fetchProperty(termmappings, TermMapping::getDataCategoryId);
        List<DataCategory> experimentalStrategies = dataCategoryService.findAllById(experimentalStrategyIds);
        Map<Integer, DataCategory> strategyMap= ServiceUtil.convertToMap(experimentalStrategies, DataCategory::getId);


        Set<Integer> analysisSoftwareIds = ServiceUtil.fetchProperty(termmappings, TermMapping::getAnalysisSoftwareId);
        List<AnalysisSoftware> analysisSoftwareList = analysisSoftwareService.findAllById(analysisSoftwareIds);
        Map<Integer, AnalysisSoftware>  analysisSoftwareMap= ServiceUtil.convertToMap(analysisSoftwareList, AnalysisSoftware::getId);

        Page<VO> cancerStudyVos = fromCancerStudies.map(cancerStudy -> {
            try {
                VO termMappingVo =  clz.newInstance();
                termMappingVo.setCancer(cancerMap.get(cancerStudy.getCancerId()));
                termMappingVo.setStudy(studyMap.get(cancerStudy.getStudyId()));
                termMappingVo.setDataOrigin(dataOriginMap.get(cancerStudy.getDataOriginId()));
                termMappingVo.setDataCategory(strategyMap.get(cancerStudy.getDataCategoryId()));
                termMappingVo.setAnalysisSoftware(analysisSoftwareMap.get(cancerStudy.getAnalysisSoftwareId()));
                BeanUtils.copyProperties(cancerStudy, termMappingVo);
                return termMappingVo;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        });
        return cancerStudyVos;
    }


    public <VO extends TermMappingVo> List<VO> convertVo(List<TERMMAPPING> termmappings,Class<VO> clz){
        Set<Integer> cancerIds = ServiceUtil.fetchProperty(termmappings, TermMapping::getCancerId);
        List<Cancer> cancers = cancerService.findAllById(cancerIds);
        Map<Integer, Cancer> cancerMap = ServiceUtil.convertToMap(cancers, Cancer::getId);


        Set<Integer> studyIds = ServiceUtil.fetchProperty(termmappings, TermMapping::getStudyId);
        List<Study> studies = studyService.findAllById(studyIds);
        Map<Integer, Study> studyMap = ServiceUtil.convertToMap(studies, Study::getId);


        Set<Integer> dataOriginIds = ServiceUtil.fetchProperty(termmappings, TermMapping::getDataOriginId);
        List<DataOrigin> dataOrigins = dataOriginService.findAllById(dataOriginIds);
        Map<Integer, DataOrigin> dataOriginMap = ServiceUtil.convertToMap(dataOrigins, DataOrigin::getId);



        Set<Integer> experimentalStrategyIds = ServiceUtil.fetchProperty(termmappings, TermMapping::getDataCategoryId);
        List<DataCategory> experimentalStrategies = dataCategoryService.findAllById(experimentalStrategyIds);
        Map<Integer, DataCategory> strategyMap= ServiceUtil.convertToMap(experimentalStrategies, DataCategory::getId);


        Set<Integer> analysisSoftwareIds = ServiceUtil.fetchProperty(termmappings, TermMapping::getAnalysisSoftwareId);
        List<AnalysisSoftware> analysisSoftwareList = analysisSoftwareService.findAllById(analysisSoftwareIds);
        Map<Integer, AnalysisSoftware>  analysisSoftwareMap= ServiceUtil.convertToMap(analysisSoftwareList, AnalysisSoftware::getId);

        List<VO> cancerStudyVos = termmappings.stream().map(cancerStudy -> {
            try {
                VO termMappingVo =  clz.newInstance();
                termMappingVo.setCancer(cancerMap.get(cancerStudy.getCancerId()));
                termMappingVo.setStudy(studyMap.get(cancerStudy.getStudyId()));
                termMappingVo.setDataOrigin(dataOriginMap.get(cancerStudy.getDataOriginId()));
                termMappingVo.setDataCategory(strategyMap.get(cancerStudy.getDataCategoryId()));
                termMappingVo.setAnalysisSoftware(analysisSoftwareMap.get(cancerStudy.getAnalysisSoftwareId()));
                BeanUtils.copyProperties(cancerStudy, termMappingVo);
                return termMappingVo;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        return cancerStudyVos;
    }

    /**
     * obj -> id
     * @return
     */
    public <PARAM extends TermMappingParam> TERMMAPPING convert(PARAM param,TERMMAPPING termmapping){
        BeanUtils.copyProperties(param,termmapping,"id");
        Cancer cancer = cancerService.findAndCheckByEnName(param.getCancer());
        Study study = studyService.findAndCheckByEnName(param.getStudy());
        DataOrigin dataOrigin = dataOriginService.findAndCheckByEnName(param.getDataOrigin());
        DataCategory dataCategory =dataCategoryService.findAndCheckByEnName(param.getDataCategory());
        AnalysisSoftware analysisSoftware=analysisSoftwareService.findAndCheckByEnName(param.getAnalysisSoftware());
        if(cancer!=null){
            termmapping.setCancerId( cancer.getId());
        }
        if(study!=null){
            termmapping.setStudyId( study.getId());
        }
        if(dataOrigin!=null){
            termmapping.setDataOriginId(dataOrigin.getId());
        }
        if(analysisSoftware!=null){
            termmapping.setAnalysisSoftwareId(analysisSoftware.getId());
        }
        if(dataCategory!=null){
            termmapping.setDataCategoryId(dataCategory.getId());
        }

        return termmapping;
    }
    @Override
    public <PARAM extends TermMappingParam> TERMMAPPING convert(PARAM param){
        TERMMAPPING termmapping = getInstance();
        return convert(param,termmapping);
    }

    @Override
    public <VO extends TermMappingVo> VO convertVo(TERMMAPPING termmapping, Class<VO> clz){

        VO termMappingVo = null;
        try {
            Cancer cancer = cancerService.findById(termmapping.getCancerId());
            Study study = studyService.findById(termmapping.getStudyId());
            DataOrigin dataOrigin = dataOriginService.findById(termmapping.getDataOriginId());
            DataCategory dataCategory = dataCategoryService.findById(termmapping.getDataCategoryId());
            AnalysisSoftware analysisSoftware = analysisSoftwareService.findById(termmapping.getAnalysisSoftwareId());

            termMappingVo = clz.newInstance();
            BeanUtils.copyProperties(termmapping, termMappingVo);
            termMappingVo.setCancer(cancer);
            termMappingVo.setStudy(study);
            termMappingVo.setDataOrigin(dataOrigin);
            termMappingVo.setDataCategory(dataCategory);
            termMappingVo.setAnalysisSoftware(analysisSoftware);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return termMappingVo;
    }

//    @Override
//    public TERMMAPPING delBy(int id) {
//        TERMMAPPING termmapping = findById(id);
//        baseTermMappingRepository.deleteById(termmapping.getId());
//        return termmapping;
//    }

}
