package com.wangyang.bioinfo.service.impl;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import com.wangyang.bioinfo.handle.SpringWebSocketHandler;
import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.enums.TaskStatus;
import com.wangyang.bioinfo.pojo.file.Attachment;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.repository.RCodeRepository;
import com.wangyang.bioinfo.repository.TaskRepository;
import com.wangyang.bioinfo.service.ICancerStudyService;
import com.wangyang.bioinfo.service.ICodeService;
import com.wangyang.bioinfo.service.ITaskService;
import com.wangyang.bioinfo.service.base.AbstractBaseFileService;
import com.wangyang.bioinfo.util.BioinfoException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/22
 */
@Service
@Slf4j
public class ICodeServiceImpl  extends AbstractBaseFileService<Code>
        implements ICodeService {

    @Autowired
    RCodeRepository rCodeRepository;
    @Autowired
    ICancerStudyService cancerStudyService;
    @Autowired
    SpringWebSocketHandler springWebSocketHandler;
    @Autowired
    TaskRepository taskRepository;

    private Code findOneBy(int dataOriginId,int studyId){
        List<Code> codes = rCodeRepository.findAll(new Specification<Code>() {
            @Override
            public Predicate toPredicate(Root<Code> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("dataOriginId"),dataOriginId),
                        criteriaBuilder.equal(root.get("studyId"),studyId))).getRestriction();
            }
        });
        if (codes.size()==0){
            return null;
        }
        return codes.get(0);
    }
//
    private Code findOneCheckBy(int dataOriginId,int studyId){
        Code code = findOneBy(dataOriginId, studyId);
        if(code==null){
            throw new BioinfoException("查找对象不存在!");
        }
        return code;
    }
//
//
//    public void processByCancerStudyId(Integer cancerStudyId,  ServletOutputStream outputStream)  {
//
//        CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(cancerStudyId);
//        Code code = findOneCheckBy(cancerStudy.getDataOriginId(), cancerStudy.getStudyId());
//        String absolutePath = cancerStudy.getAbsolutePath();
//        String codeAbsolutePath = code.getAbsolutePath();
//        String processedAbsolutePath = cancerStudy.getAbsolutePath()+"_p."+cancerStudy.getFileType();
//        String processedRelativePath = cancerStudy.getRelativePath()+"_p."+cancerStudy.getFileType();;
//
//        Task task = new Task();
//        task.setCodeId(code.getId());
//        task.setName(cancerStudy.getEnName());
////        taskService.add(task);
//
//        task.setTaskStatus(TaskStatus.RUNNING);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        RCaller rcaller = RCaller.create();
//        rcaller.redirectROutputToStream(outputStream);
//
//        RCode rcode = RCode.create();
//        rcode.R_source(codeAbsolutePath);
//        rcode.addString("res", new String());
//        rcode.addRCode("res <- processFile('"+absolutePath+"','"+processedAbsolutePath+"')");
//        log.info("processFile('"+absolutePath+"','"+processedAbsolutePath+"')");
//        rcaller.setRCode(rcode);
//        rcaller.runAndReturnResultOnline("res");
//
//        cancerStudy.setProcessedAbsolutePath(processedAbsolutePath);
//        cancerStudy.setProcessedRelativePath(processedRelativePath);
//        cancerStudyService.save(cancerStudy);
//        task.setTaskStatus(TaskStatus.FINISH);
//
//    }
//
//    @Override
    @Async("taskExecutor")
    public void processAsyncByCancerStudy(Task task,CancerStudy cancerStudy)  {
        Code code = findOneCheckBy(cancerStudy.getDataOriginId(), cancerStudy.getStudyId());
        String absolutePath = cancerStudy.getAbsolutePath();
        String codeAbsolutePath = code.getAbsolutePath();
        String processedAbsolutePath = cancerStudy.getAbsolutePath()+"_p."+cancerStudy.getFileType();
        String processedRelativePath = cancerStudy.getRelativePath()+"_p."+cancerStudy.getFileType();;
        //设置task状态 RUNNING
        task.setCodeId(code.getId());
        task.setTaskStatus(TaskStatus.RUNNING);
        taskRepository.save(task);

//        try {
//            Thread.sleep(1000*50);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        RCaller rcaller = RCaller.create();
        RCode rcode = RCode.create();
        rcode.R_source(codeAbsolutePath);
        rcode.addString("res", new String());
        rcode.addRCode("res <- processFile('"+absolutePath+"','"+processedAbsolutePath+"')");
        log.info("processFile('"+absolutePath+"','"+processedAbsolutePath+"')");
        rcaller.setRCode(rcode);
        rcaller.runAndReturnResultOnline("res");

        cancerStudy.setProcessedAbsolutePath(processedAbsolutePath);
        cancerStudy.setProcessedRelativePath(processedRelativePath);
        cancerStudyService.save(cancerStudy);

        //设置task状态 FINISH
        task.setTaskStatus(TaskStatus.FINISH);
        taskRepository.save(task);
    }
}

