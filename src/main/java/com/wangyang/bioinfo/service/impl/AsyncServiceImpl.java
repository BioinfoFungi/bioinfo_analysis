package com.wangyang.bioinfo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.rcaller.FunctionCall;
import com.github.rcaller.FunctionParameter;
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

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
    public void processCancerStudy(Task task, Code code,CancerStudy cancerStudy,CancerStudy cancerStudyProcess,Map<String, Object> map)  {
        /***************************************************************/
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
//        springWebSocketHandler.sendMessageToUsers(new TextMessage(Thread.currentThread().getName()+": start! >>>>>>>>>>>>>>>>>>>"));
        task.setThreadName(Thread.currentThread().getName());
        task.setTaskStatus(TaskStatus.RUNNING);
        taskService.save(task);
        /*****************************************************************/

//        CodeMsg codeMsg = rCall(code, map);
        CodeMsg codeMsg  = processBuilder(code, map);;
        try {
            if(codeMsg.getCancerStudyParam()!=null){
                if(codeMsg.getIsUpdate()){
                    cancerStudyService.saveCancerStudy(cancerStudy);
                }else {
                    CancerStudy covertCancerStudy = cancerStudyService.convert(codeMsg.getCancerStudyParam());
                    BeanUtil.copyProperties(covertCancerStudy,cancerStudyProcess);
                    cancerStudyService.saveCancerStudy(cancerStudyProcess);
                }
            }
            if(codeMsg.getStatus()){
                task.setIsSuccess(true);
            }
        } catch (Exception e) {
            task.setIsSuccess(false);
            e.printStackTrace();
            codeMsg.setRunMsg(codeMsg.getRunMsg()+e.getMessage());
        }

        /*****************************************************************/
        task.setResult(codeMsg.getResult());
        task.setRunMsg(codeMsg.getRunMsg());
        task.setTaskStatus(TaskStatus.FINISH);

        taskService.save(task);
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        springWebSocketHandler.sendMessageToUsers(new TextMessage(JSONObject.toJSON(task).toString()));
        /*****************************************************************/
    }


    private CodeMsg processBuilder(Code code, Map<String,Object> maps){
        CodeMsg codeMsg = new CodeMsg();
        File  tempFile=null;
        try {
            tempFile = File.createTempFile("bioinfo-", ".R");
            StringBuffer stringBuffer = new StringBuffer();
            for (String key: maps.keySet()){
                if(maps.get(key) instanceof String && maps.get(key)!=null){
                    stringBuffer.append(key+" <- \""+ maps.get(key)+"\"\n");
                }
            }
            stringBuffer.append("\n");

            byte[] bytes = Files.readAllBytes(Paths.get(code.getAbsolutePath()));
            String content = new String(bytes, StandardCharsets.UTF_8);
            stringBuffer.append(content);
            try (FileOutputStream fop = new FileOutputStream(tempFile)) {
                fop.write(stringBuffer.toString().getBytes());
                fop.flush();
            }


            ProcessBuilder processBuilder = new ProcessBuilder();
            List<String> command = new ArrayList<>();
            command.add("Rscript");
            command.add(tempFile.getAbsolutePath());
            processBuilder.command(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            StringBuilder result = new StringBuilder();

            Map<String,String> resultMap = new HashMap<>();

            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                String line;
                while ((line = reader.readLine()) != null) {

                    if(line.startsWith("$")) {
                        String[] strings = line.split(":");
                        String key = strings[0].substring(1);
                        String value = strings[1];
                        resultMap.put(key,value);
                    }
                    result.append(line);
                    System.out.println(line);
                }
            }

            CancerStudyParam cancerStudyParam =null;



            Method[] methods = CancerStudyParam.class.getMethods();
            for (Method method :methods){
                String name = method.getName();
                if(name.startsWith("set")){
                    int length = name.length();
                    String basename = name.substring(3,length);
                    String preName = basename.substring(0, 1).toLowerCase();
                    String var = preName+basename.substring(1);
                    if(resultMap.containsKey(var)){
                        if(cancerStudyParam==null){
                            cancerStudyParam= new CancerStudyParam();
                        }
                        method.invoke(cancerStudyParam,resultMap.get(var).replaceAll(" ",""));
                    }
                }
            }


            process.waitFor();
            int exit = process.exitValue();
            if (exit != 0) {
                codeMsg.setStatus(false);
                codeMsg.setRunMsg(result.toString());
//                throw new IOException("failed to execute:" + processBuilder.command() + " with result:" + result);
            }
            codeMsg.setResult("");
            codeMsg.setRunMsg(result.toString());
            codeMsg.setCancerStudyParam(cancerStudyParam);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }finally {
            if(tempFile!=null){
                tempFile.delete();
            }
        }
        codeMsg.setStatus(true);
        return codeMsg;
    }


    private CodeMsg rCall(Code code, Map<String,Object> maps){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CodeMsg codeMsg = new CodeMsg();
        RCaller rcaller = RCaller.create();
        rcaller.redirectROutputToStream(byteArrayOutputStream);
        RCode rcode = RCode.create();
        rcaller.setRCode(rcode);

        for (String key: maps.keySet()){
            if(maps.get(key) instanceof String){
                rcode.addString(key,(String) maps.get(key));
            }
        }
        rcode.R_source(code.getAbsolutePath());
        FunctionCall fc = new FunctionCall();
        fc.setFunctionName("main");
//        fc.addParameter(new FunctionParameter("formula", "y~x", FunctionParameter.PARAM_OBJECT));
//        fc.addParameter(new FunctionParameter("data", "mydata", FunctionParameter.PARAM_OBJECT));

        rcode.addFunctionCall("result", fc);
        rcaller.runAndReturnResultOnline("result");
        ArrayList<String> varNames = rcaller.getParser().getNames();
        if(varNames.contains("update")){
            codeMsg.setIsUpdate(true);
        }
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
            JSONObject jsonObject = new JSONObject();
            varNames.forEach(var->{
                String[] array = rcaller.getParser().getAsStringArray(var);
                if(array.length==1){
                    jsonObject.put(var,array[0]);
                }else {
                    jsonObject.put(var,array);
                }

            });
            codeMsg.setResult(jsonObject.toString());
            codeMsg.setCancerStudyParam(cancerStudyParam);
            codeMsg.setRunMsg(byteArrayOutputStream.toString());
            codeMsg.setStatus(true);


//            springWebSocketHandler.sendMessageToUsers(new TextMessage(byteArrayOutputStream.toByteArray()));
            System.out.println(byteArrayOutputStream);
            return codeMsg;
        } catch (Exception e) {
            codeMsg.setRunMsg(byteArrayOutputStream.toString());
            codeMsg.setException(e.getMessage());
//            springWebSocketHandler.sendMessageToUsers(new TextMessage(byteArrayOutputStream.toByteArray()));
//            springWebSocketHandler.sendMessageToUsers(new TextMessage(e.getMessage()));
            e.printStackTrace();
            codeMsg.setStatus(false);
            return codeMsg;
        }
        finally {
            rcaller.stopRCallerOnline();
        }
    }

}
