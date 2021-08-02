package com.wangyang.bioinfo.service.impl;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import com.wangyang.bioinfo.handle.SpringWebSocketHandler;
import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.enums.TaskStatus;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.repository.RCodeRepository;
import com.wangyang.bioinfo.repository.TaskRepository;
import com.wangyang.bioinfo.service.ICancerStudyService;
import com.wangyang.bioinfo.service.ICodeService;
import com.wangyang.bioinfo.service.base.BaseFileService;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.OOBMessage;
import com.wangyang.bioinfo.util.ObjectToMap;
import lombok.extern.slf4j.Slf4j;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RFileInputStream;
import org.rosuda.REngine.Rserve.RSession;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @author wangyang
 * @date 2021/7/22
 */
@Service
@Slf4j
public class ICodeServiceImpl  extends BaseFileService<Code>
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


    @Override
    @Async("taskExecutor")
    public void rCodePlot(Integer id, Integer cancerStudyId){
        CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(cancerStudyId);
        Map<String, String> conditionMap = ObjectToMap.setConditionMap(cancerStudy);
        rCodePlot(id,conditionMap);
    }


    @Override
    public void rCodePlot(Integer id, Map<String,String> maps){
        Code code = findById(id);
        String absolutePath = code.getAbsolutePath();
        RConnection connection=null;

        try {
            connection = new RConnection();
            OOBMessage oobMessage = new OOBMessage(springWebSocketHandler);
            connection.setOOB(oobMessage);
            REXP cap = connection.capabilities();
            if (cap == null) {
                System.err.println("ERROR: Rserve is not running in OCAP mode");
                return;
            }
//            connection.eval("library(httpgd)");
//            connection.eval("library(ggplot2)");
//            for (String key: maps.keySet()){
//                connection.assign(key,maps.get(key));
//            }
//            REXP eval = connection.eval("hgd_inline({plot.new(); source(\"" +  absolutePath+ "\") })");
//            String s = eval.asString();

            String testCode = "{message('Hello!'); print(R.version.string) }";
            RList occ = new RList(new REXP[] { cap, new REXPString(testCode) });
            REXP res = connection.callOCAP(new REXPLanguage(occ));

            final String s = res.asString();
//            springWebSocketHandler.sendMessageToUsers(new TextMessage(s));
        } catch (RserveException  e) {
            e.printStackTrace();
            throw new BioinfoException(e.getMessage());
        } catch (REngineException e) {
            e.printStackTrace();
            throw new BioinfoException(e.getMessage());
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally {
            if(connection!=null){
                connection.close();
            }
        }
    }
}

