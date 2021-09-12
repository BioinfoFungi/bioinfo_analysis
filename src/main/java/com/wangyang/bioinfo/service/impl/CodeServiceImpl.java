package com.wangyang.bioinfo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.wangyang.bioinfo.handle.FileHandlers;
import com.wangyang.bioinfo.pojo.entity.*;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.pojo.param.CodeParam;
import com.wangyang.bioinfo.pojo.param.CodeQuery;
import com.wangyang.bioinfo.pojo.param.TermMappingParam;
import com.wangyang.bioinfo.repository.CancerStudyRepository;
import com.wangyang.bioinfo.repository.CodeRepository;
import com.wangyang.bioinfo.repository.TaskRepository;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.service.base.BaseFileService;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.CacheStore;
import com.wangyang.bioinfo.util.File2Tsv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/7/22
 */
@Service
@Slf4j
@Transactional
public class CodeServiceImpl extends BaseFileService<Code>
        implements ICodeService {


    private  final CodeRepository codeRepository;
    private final TaskRepository taskRepository;
    private final CancerStudyRepository cancerStudyRepository;
    private  final ICancerService cancerService;
    private  final IStudyService studyService;
    private  final IDataOriginService dataOriginService;
    private  final IDataCategoryService dataCategoryService;
    private  final IAnalysisSoftwareService analysisSoftwareService;
    public CodeServiceImpl(FileHandlers fileHandlers,
                           CodeRepository codeRepository,
                           TaskRepository taskRepository,
                           CancerStudyRepository cancerStudyRepository,
                           ICancerService cancerService,
                           IStudyService studyService,
                           IDataOriginService dataOriginService,
                           IDataCategoryService dataCategoryService,
                           IAnalysisSoftwareService analysisSoftwareService) {
        super(fileHandlers,codeRepository);
        this.codeRepository=codeRepository;
        this.taskRepository = taskRepository;
        this.cancerStudyRepository = cancerStudyRepository;
        this.cancerService =cancerService;
        this.studyService=studyService;
        this.dataOriginService=dataOriginService;
        this.dataCategoryService=dataCategoryService;
        this.analysisSoftwareService=analysisSoftwareService;
    }

    @Override
    public Page<Code> pageBy(CodeQuery codeQuery, Pageable pageable) {
        Code code = new Code();
        BeanUtils.copyProperties(codeQuery,code);
        Page<Code> codePage = pageBy(code, codeQuery.getKeyWard(), pageable);
        return codePage;
    }

    @Override
    public Code saveBy(CodeParam codeParam, User user) {
        Code code = new Code();
        BeanUtils.copyProperties(codeParam,code);
        code.setUserId(user.getId());
        return  codeRepository.save(code);
    }

    @Override
    public Code updateBy(Integer id, CodeParam codeParam, User user) {
        Code code = findById(id);
        BeanUtils.copyProperties(codeParam,code,"id");
        return codeRepository.save(code);
    }


    @Override
    public List<Code> findExecute(Integer id) {
        Optional<CancerStudy> cancerStudyOptional = cancerStudyRepository.findById(id);
        if(!cancerStudyOptional.isPresent()){
            throw new BioinfoException("cancerStudy is not exist！");
        }
        List<Code> codes = codeRepository.findAll();
        CancerStudy cancerStudy = cancerStudyOptional.get();
        List<Code> codeList = codes.stream().filter(code ->{
            return  (code.getStudy() == null ? true : code.getStudy().contains(cancerStudy.getStudyId())) &&
                    (code.getCancer() == null ? true : code.getCancer().contains(cancerStudy.getCancerId())) &&
                    (code.getAnalysisSoftware() == null ? true : code.getAnalysisSoftware().contains(cancerStudy.getAnalysisSoftwareId())) &&
                    (code.getDataOrigin() == null ? true : code.getDataOrigin().contains(cancerStudy.getDataOriginId())) &&
                            (code.getDataCategory() == null ? true : code.getDataCategory().contains(cancerStudy.getDataCategoryId()));
        }).collect(Collectors.toList());
        return codeList;
    }




    @Override
    public Code delBy(Integer id) {
        List<Task> tasks = taskRepository.findByCodeId(id);
        taskRepository.deleteAll(tasks);
        return super.delBy(id);
    }


    @Override
    public CodeType checkCodeType(String path) {
        if(path.endsWith(".R")){
            return CodeType.R;
        }else if(path.endsWith(".sh")){
            return CodeType.SHELL;
        }else if(path.endsWith(".py")){
            return CodeType.PYTHON;
        }
        throw new BioinfoException("文件类型不支持!");
    }
    @Override
    @Transactional
    public List<Code> initData(String filePath, Boolean isEmpty) {
        if(isEmpty){
            truncateTable();
        }
        List<CodeParam> codeParams = File2Tsv.tsvToBean(CodeParam.class, filePath);
        if(codeParams==null){
            throw new BioinfoException(filePath+" 不存在！");
        }
        return codeParams.stream().map(bean->{
            if(bean.getRelativePath()!=null && bean.getAbsolutePath()==null){
                String workDir = CacheStore.getValue("workDir");
                Path path = Paths.get(workDir, bean.getRelativePath());
                bean.setAbsolutePath(path.toString());
            }
            Code code = covert(bean);
            saveAndCheckFile(code);
            return code;
        }).collect(Collectors.toList());
    }

    private Code covert(CodeParam codeParams) {
        Code code = new Code();
        BeanUtils.copyProperties(codeParams,code);
        if(codeParams.getCodeOutput()!=null){

        }
        return code;
    }

    private void checkTerm(Code bean) {
        Set<Integer> cancer = bean.getCancer()!=null?bean.getCancer():new HashSet<>();
        Set<Integer> study = bean.getStudy()!=null?bean.getStudy():new HashSet<>();
        Set<Integer> dataOrigin = bean.getDataOrigin()!=null?bean.getDataOrigin():new HashSet<>();
        Set<Integer> analysisSoftware = bean.getAnalysisSoftware()!=null?bean.getAnalysisSoftware():new HashSet<>();
        Set<Integer> dataCategory = bean.getDataCategory()!=null?bean.getDataCategory():new HashSet<>();
        if(bean.getCodeOutput()!=null){
            if(bean.getTaskType().equals(TaskType.CANCER_STUDY)){
                String json = bean.getCodeOutput();
                json = json.replace("\"","");
                List<CancerStudy> cancerStudies = JSONArray.parseArray(json, CancerStudy.class);
                cancerStudies.forEach(cancerStudy -> {
                    if(cancerStudy.getCancerId()!=null){
                        cancer.add(cancerStudy.getCancerId());
                    }
                    if(cancerStudy.getStudyId()!=null){
                        study.add(cancerStudy.getStudyId());
                    }
                    if(cancerStudy.getDataCategoryId()!=null){
                        dataCategory.add(cancerStudy.getDataCategoryId());
                    }
                    if(cancerStudy.getDataOriginId()!=null){
                        dataOrigin.add(cancerStudy.getDataOriginId());
                    }
                    if(cancerStudy.getAnalysisSoftwareId()!=null){
                        analysisSoftware.add(cancerStudy.getAnalysisSoftwareId());
                    }
                });
            }
        }

        List<Cancer> cancerList = cancerService.findAllById(cancer);

        List<Study> studyList = studyService.findAllById(study);
        List<DataCategory> dataCategoryList = dataCategoryService.findAllById(dataOrigin);
        List<AnalysisSoftware> analysisSoftwareList = analysisSoftwareService.findAllById(analysisSoftware);
        List<DataOrigin> dataOriginList = dataOriginService.findAllById(dataCategory);


    }

}

