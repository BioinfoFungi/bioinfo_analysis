package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.handle.FileHandlers;
import com.wangyang.bioinfo.pojo.entity.CancerStudy;
import com.wangyang.bioinfo.pojo.entity.Task;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.entity.Code;
import com.wangyang.bioinfo.pojo.param.CodeParam;
import com.wangyang.bioinfo.pojo.param.CodeQuery;
import com.wangyang.bioinfo.repository.CancerStudyRepository;
import com.wangyang.bioinfo.repository.CodeRepository;
import com.wangyang.bioinfo.repository.TaskRepository;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.service.base.BaseFileService;
import com.wangyang.bioinfo.util.BioinfoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public CodeServiceImpl(FileHandlers fileHandlers,
                           CodeRepository codeRepository,
                           TaskRepository taskRepository,
                           CancerStudyRepository cancerStudyRepository) {
        super(fileHandlers,codeRepository);
        this.codeRepository=codeRepository;
        this.taskRepository = taskRepository;
        this.cancerStudyRepository = cancerStudyRepository;
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

}

