package com.wangyang.bioinfo.service.impl;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import com.wangyang.bioinfo.handle.SpringWebSocketHandler;
import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.enums.TaskStatus;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVO;
import com.wangyang.bioinfo.pojo.vo.TermMappingVo;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.util.BeanUtil;
import com.wangyang.bioinfo.util.ObjectToCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AsyncServiceImpl implements IAsyncService {


    @Autowired
    ITaskService taskService;
    @Autowired
    ICancerStudyService cancerStudyService;
    @Autowired
    SpringWebSocketHandler springWebSocketHandler;



    @Async("taskExecutor")
    @Override
    public void processCancerStudy(Task task, Code code,CancerStudy cancerStudyProcess,Map<String, Object> map)  {
        /***************************************************************/
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
        springWebSocketHandler.sendMessageToUsers(new TextMessage(Thread.currentThread().getName()+": start! >>>>>>>>>>>>>>>>>>>"));
        task.setThreadName(Thread.currentThread().getName());
        task.setTaskStatus(TaskStatus.RUNNING);
        taskService.save(task);
        /*****************************************************************/

        CodeMsg codeMsg = rCall(code, map);
        if(codeMsg.getCancerStudyParam()!=null){
            CancerStudy cancerStudy = cancerStudyService.convert(codeMsg.getCancerStudyParam());
            BeanUtil.copyProperties(cancerStudy,cancerStudyProcess);
            cancerStudyService.saveCancerStudy(cancerStudy);
        }

        /*****************************************************************/
        task.setRunMsg(codeMsg.getRunMsg());
        task.setTaskStatus(TaskStatus.FINISH);
        taskService.save(task);
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        springWebSocketHandler.sendMessageToUsers(new TextMessage(Thread.currentThread().getName()+": end! <<<<<<<<<<<<<<"));
        /*****************************************************************/
    }

    private CodeMsg rCall(Code code, Map<String,Object> maps){
        CodeMsg codeMsg = new CodeMsg();
        RCaller rcaller = RCaller.create();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        rcaller.redirectROutputToStream(byteArrayOutputStream);
        RCode rcode = RCode.create();
        rcaller.setRCode(rcode);
        for (String key: maps.keySet()){
            if(maps.get(key) instanceof String){
                rcode.addString(key,(String) maps.get(key));
            }
        }
        rcode.R_source(code.getAbsolutePath());
        rcode.addRCode("res <- main()");
        rcaller.runAndReturnResultOnline("res");
        ArrayList<String> varNames = rcaller.getParser().getNames();

        try {
            CancerStudyParam cancerStudyParam =null;
            Method[] methods = CancerStudyParam.class.getMethods();
            for (Method method :methods){
                String name = method.getName();
                if(name.startsWith("set")){
                    int length = name.length();
                    String basename = name.substring(3,length);
                    String preName = basename.substring(0, 1).toLowerCase();
                    String var = preName+basename.substring(1,basename.length());
                    if(varNames.contains(var)){
                        if(cancerStudyParam==null){
                            cancerStudyParam= new CancerStudyParam();
                        }
                        String[] array = rcaller.getParser().getAsStringArray(var);
                        method.invoke(cancerStudyParam,array[0]);
                    }
                }
            }

            codeMsg.setCancerStudyParam(cancerStudyParam);
            codeMsg.setRunMsg(byteArrayOutputStream.toString());
            codeMsg.setStatus(true);
//            springWebSocketHandler.sendMessageToUsers(new TextMessage(byteArrayOutputStream.toByteArray()));
//            System.out.println(byteArrayOutputStream);
            return codeMsg;
        } catch (Exception e) {
            codeMsg.setRunMsg(byteArrayOutputStream.toString());
            codeMsg.setException(e.getMessage());
//            springWebSocketHandler.sendMessageToUsers(new TextMessage(byteArrayOutputStream.toByteArray()));
//            springWebSocketHandler.sendMessageToUsers(new TextMessage(e.getMessage()));
            e.printStackTrace();
            codeMsg.setStatus(false);
            return codeMsg;
        }finally {
            rcaller.stopRCallerOnline();
        }
    }

}
