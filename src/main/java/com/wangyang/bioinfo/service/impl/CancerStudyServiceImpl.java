package com.wangyang.bioinfo.service.impl;


import com.wangyang.bioinfo.handle.FileHandlers;
import com.wangyang.bioinfo.pojo.*;
import com.wangyang.bioinfo.pojo.enums.AttachmentType;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.pojo.param.CancerStudyQuery;
import com.wangyang.bioinfo.pojo.param.FindCancer;
import com.wangyang.bioinfo.pojo.support.UploadResult;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVo;
import com.wangyang.bioinfo.repository.CancerStudyRepository;
import com.wangyang.bioinfo.service.ICancerService;
import com.wangyang.bioinfo.service.ICancerStudyService;
import com.wangyang.bioinfo.service.IDataOriginService;
import com.wangyang.bioinfo.service.IStudyService;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.FilenameUtils;
import com.wangyang.bioinfo.util.ServiceUtil;
import com.wangyang.bioinfo.util.StringCacheStore;
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

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Service
public class CancerStudyServiceImpl implements ICancerStudyService {
    @Autowired
    CancerStudyRepository cancerStudyRepository;
    @Autowired
    ICancerService cancerService;
    @Autowired
    IStudyService studyService;
    @Autowired
    IDataOriginService dataOriginService;

    @Autowired
    FileHandlers fileHandlers;

    @Override
    public CancerStudy addCancerStudy(CancerStudyParam cancerStudyParam) {
        Cancer cancer = cancerService.findAndCheckByEnName(cancerStudyParam.getCancer());
        Study study = studyService.findAndCheckByEnName(cancerStudyParam.getStudy());
        DataOrigin dataOrigin = dataOriginService.findAndCheckByEnName(cancerStudyParam.getDataOrigin());
        CancerStudy cancerStudy = findCancerStudyByAndThree(cancer.getId(), study.getId(), dataOrigin.getId());
        if(cancerStudy==null){
            cancerStudy = new CancerStudy();
            cancerStudy.setCancerId(cancer.getId());
            cancerStudy.setStudyId(study.getId());
            cancerStudy.setDataOriginId(dataOrigin.getId());
        }
        BeanUtils.copyProperties(cancerStudyParam,cancerStudy);
        if(cancerStudyParam.getPath().startsWith("http")){
            cancerStudy.setNetworkPath(cancerStudyParam.getPath());
        }else {
            cancerStudy.setLocalPath(StringCacheStore.getValue("workDir")+"/data/"+cancerStudyParam.getPath());
            cancerStudy.setNetworkPath("data/"+cancerStudyParam.getPath());
        }



        if(cancerStudyParam.getFileType()==null){
            String extension = FilenameUtils.getExtension(cancerStudyParam.getPath());
            if(extension.equals("")){
                throw new BioinfoException("路径必须添加后缀名！");
            }
            cancerStudy.setFileType(extension);
        }

        if(cancerStudyParam.getFileName()==null){
            String basename = FilenameUtils.getBasename(cancerStudyParam.getPath());
            cancerStudy.setFileName(basename);
        }
//        if(cancerStudyParam.getLocalPath()!=null){
//            cancerStudy.setLocalPath(cancerStudyParam.getLocalPath());
//        }
//        if(cancerStudyParam.getNetworkPath()!=null){
//            cancerStudy.setNetworkPath(cancerStudyParam.getNetworkPath());
//        }

        return cancerStudyRepository.save(cancerStudy);
    }

    @Override
    public CancerStudy upload(MultipartFile file, CancerStudyParam cancerStudyParam) {
        Cancer cancer = cancerService.findAndCheckByEnName(cancerStudyParam.getCancer());
        Study study = studyService.findAndCheckByEnName(cancerStudyParam.getStudy());
        DataOrigin dataOrigin = dataOriginService.findAndCheckByEnName(cancerStudyParam.getDataOrigin());
        CancerStudy cancerStudy = findCancerStudyByAndThree(cancer.getId(), study.getId(), dataOrigin.getId());
        if(cancerStudy==null){
            cancerStudy = new CancerStudy();
            cancerStudy.setCancerId(cancer.getId());
            cancerStudy.setStudyId(study.getId());
            cancerStudy.setDataOriginId(dataOrigin.getId());
        }

        UploadResult uploadResult = fileHandlers.upload(file, AttachmentType.LOCAL,"data",cancerStudyParam.getFileName(),cancerStudyParam.getFileType());

        BeanUtils.copyProperties(cancerStudyParam,cancerStudy);

        cancerStudy.setLocalPath(uploadResult.getFullPath());
        cancerStudy.setPath(uploadResult.getFilePath());
        cancerStudy.setNetworkPath(uploadResult.getFilePath());
        cancerStudy.setSize(uploadResult.getSize());

        if(cancerStudyParam.getFileName()==null){
            cancerStudy.setFileName(uploadResult.getFilename());
        }
        if(cancerStudyParam.getFileType()==null){
            cancerStudy.setFileType(uploadResult.getSuffix());

        }
        return cancerStudyRepository.save(cancerStudy);
    }

    @Override
    public CancerStudy delCancerStudy(int id) {
        return null;
    }

    @Override
    public CancerStudy findCancerStudyById(int id) {
        return null;
    }

    @Override
    public CancerStudy findCancerStudyByAndThree(int cancerId, int studyId, int dataOriginId) {
        List<CancerStudy> cancerStudies = cancerStudyRepository.findAll(new Specification<CancerStudy>() {
            @Override
            public Predicate toPredicate(Root<CancerStudy> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("cancerId"), cancerId),
                        criteriaBuilder.equal(root.get("studyId"), studyId),
                        criteriaBuilder.equal(root.get("dataOriginId"), dataOriginId));
                return criteriaQuery.where(predicate).getRestriction();
            }
        });
        if(cancerStudies.size()==0){
            return null;
        }
        return cancerStudies.get(0);
    }

    @Override
    public CancerStudy findCancerStudyByAndThree(FindCancer findCancer) {
        Cancer cancer = cancerService.findAndCheckByEnName(findCancer.getCancer());
        Study study = studyService.findAndCheckByEnName(findCancer.getStudy());
        DataOrigin dataOrigin = dataOriginService.findAndCheckByEnName(findCancer.getDataOrigin());
        return findCancerStudyByAndThree(cancer.getId(),study.getId(),dataOrigin.getId());
    }

    @Override
    public List<CancerStudy> findAllById(Collection<Integer> id) {
        return null;
    }

    @Override
    public List<CancerStudy> listAll() {
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
    public Page<CancerStudyVo> convertProjectVo(Page<CancerStudy> fromCancerStudies) {

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



        Page<CancerStudyVo> cancerStudyVos = fromCancerStudies.map(cancerStudy -> {
            CancerStudyVo cancerStudyVo = new CancerStudyVo();
            cancerStudyVo.setCancer(cancerMap.get(cancerStudy.getCancerId()));
            cancerStudyVo.setStudy(studyMap.get(cancerStudy.getStudyId()));
            cancerStudyVo.setDataOrigin(dataOriginMap.get(cancerStudy.getDataOriginId()));
            BeanUtils.copyProperties(cancerStudy,cancerStudyVo);
            return cancerStudyVo;
        });
        return cancerStudyVos;
    }
}