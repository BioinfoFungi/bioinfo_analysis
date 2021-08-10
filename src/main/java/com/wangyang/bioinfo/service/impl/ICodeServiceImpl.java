package com.wangyang.bioinfo.service.impl;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import com.wangyang.bioinfo.handle.SpringWebSocketHandler;
import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.enums.TaskStatus;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.pojo.param.CancerParam;
import com.wangyang.bioinfo.pojo.trem.Study;
import com.wangyang.bioinfo.repository.RCodeRepository;
import com.wangyang.bioinfo.repository.TaskRepository;
import com.wangyang.bioinfo.service.ICancerStudyService;
import com.wangyang.bioinfo.service.ICodeService;
import com.wangyang.bioinfo.service.IStudyService;
import com.wangyang.bioinfo.service.base.BaseFileService;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.ObjectToMap;
import lombok.extern.slf4j.Slf4j;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
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
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

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
    @Autowired
    IStudyService studyService;

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
    @Async("taskExecutor")
    public void runRCode(Integer id, Integer cancerStudyId){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
        springWebSocketHandler.sendMessageToUsers(new TextMessage(Thread.currentThread().getName()+": start! >>>>>>>>>>>>>>>>>>>"));
        CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(cancerStudyId);
        Map<String, String> maps = ObjectToMap.setConditionMap(cancerStudy);
        Code code = findById(id);
        RCaller rcaller = RCaller.create();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        rcaller.redirectROutputToStream(byteArrayOutputStream);
        RCode rcode = RCode.create();
        rcaller.setRCode(rcode);
        for (String key: maps.keySet()){
               rcode.addString(key,maps.get(key));
        }

        rcode.R_source(code.getAbsolutePath());

        rcode.addRCode("res <- main()");

        rcaller.runAndReturnResultOnline("res");
        String[] param = rcaller.getParser().getAsStringArray("res");
        rcaller.stopRCallerOnline();

        System.out.println(byteArrayOutputStream);
        TextMessage textMessage = new TextMessage(byteArrayOutputStream.toByteArray());
        springWebSocketHandler.sendMessageToUsers(textMessage);

        CancerStudy processCancerStudy = new CancerStudy();
        User user = new User();
        user.setId(-1);
        processCancerStudy.setCancerId(cancerStudy.getCancerId());
        processCancerStudy.setDataOriginId(cancerStudy.getDataOriginId());
        Study study = studyService.findByEnName(param[0]);
        processCancerStudy.setStudyId(study.getId());
        processCancerStudy.setAbsolutePath(param[1]);
        cancerStudyService.saveCancerStudy(processCancerStudy,user);
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        springWebSocketHandler.sendMessageToUsers(new TextMessage(Thread.currentThread().getName()+": end! <<<<<<<<<<<<<<"));
    }


    @Override
    public void rCodePlot(Integer id, Map<String,String> maps){
        Code code = findById(id);
        String absolutePath = code.getAbsolutePath();
        RConnection connection=null;

        try {
            connection = new RConnection();
//            OOBMessage oobMessage = new OOBMessage(springWebSocketHandler);
//            connection.setOOB(oobMessage);
//            REXP cap = connection.capabilities();
//            if (cap == null) {
//                System.err.println("ERROR: Rserve is not running in OCAP mode");
//                return;
//            }
//            connection.eval("library(httpgd)");
//            connection.eval("library(ggplot2)");
//            for (String key: maps.keySet()){
//                connection.assign(key,maps.get(key));
//            }
//            REXP eval = connection.eval("hgd_inline({plot.new(); source(\"" +  absolutePath+ "\") })");
//            String s = eval.asString();

            String testCode = "{message('Hello!'); print(R.version.string) }";
//            RList occ = new RList(new REXP[] { cap, new REXPString(testCode) });
//            REXP res = connection.callOCAP(new REXPLanguage(occ));

//            final String s = res.asString();
//            springWebSocketHandler.sendMessageToUsers(new TextMessage(s));
        } catch (RserveException  e) {
            e.printStackTrace();
            throw new BioinfoException(e.getMessage());
        } catch (REngineException e) {
            e.printStackTrace();
            throw new BioinfoException(e.getMessage());
        } finally {
            if(connection!=null){
                connection.close();
            }
        }
    }
}

