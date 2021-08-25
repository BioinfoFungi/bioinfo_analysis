package com.wangyang.bioinfo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.rcaller.FunctionCall;
import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import com.wangyang.bioinfo.handle.SpringWebSocketHandler;
import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.dto.TaskProcess;
import com.wangyang.bioinfo.pojo.enums.TaskStatus;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.pojo.trem.Cancer;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.util.BeanUtil;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.StringCacheStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@Slf4j
public class AsyncServiceImpl implements IAsyncService {


    @Autowired
    ITaskService taskService;
    @Autowired
    ICancerStudyService cancerStudyService;
    @Autowired
    SpringWebSocketHandler springWebSocketHandler;
    @Autowired
    private ThreadPoolTaskExecutor executorService;

    final Map<Integer, TaskProcess> processMap = new HashMap<>();

//
//    public class ControlSubThread implements Runnable {
//
////        private Thread worker;
//        private final AtomicBoolean running = new AtomicBoolean(false);
////        private int interval;
//        Task task;
//        Code code;
//        CancerStudy cancerStudy;
//        CancerStudy cancerStudyProcess;
//        Map<String, Object> map;
//
//        public ControlSubThread(Task task, Code code, CancerStudy cancerStudy, CancerStudy cancerStudyProcess, Map<String, Object> map) {
//            this.task = task;
//            this.code = code;
//            this.cancerStudy = cancerStudy;
//            this.cancerStudyProcess = cancerStudyProcess;
//            this.map = map;
//        }
//        //        public ControlSubThread(int sleepInterval) {
////            interval = sleepInterval;
////        }
//
////        public void start() {
////            worker = new Thread(this);
////            worker.start();
////        }
//
//        public void stop() {
//            running.set(false);
//        }
//
//        public void run() {
//            running.set(true);
//            while (running.get()) {
//                try {
//                    Thread.sleep(5000);
//                    System.out.printf("aaaaaaaaaaaaaaaaaaaa");
//                } catch (InterruptedException e){
//                    Thread.currentThread().interrupt();
//                    System.out.println(
//                            "Thread was interrupted, Failed to complete operation");
//                }
//                // do something here
//            }
//        }
//    }
//    class TasKRun implements Runnable{
//        Task task;
//        Code code;
//        CancerStudy cancerStudy;
//        CancerStudy cancerStudyProcess;
//        Map<String, Object> map;
//
//        public TasKRun(Task task, Code code, CancerStudy cancerStudy, CancerStudy cancerStudyProcess, Map<String, Object> map) {
//            this.task = task;
//            this.code = code;
//            this.cancerStudy = cancerStudy;
//            this.cancerStudyProcess = cancerStudyProcess;
//            this.map = map;
//        }
//        @Override
//        public void run() {
//            try {
//                processCancerStudy(task, code, cancerStudy, cancerStudyProcess, map);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void processCancerStudy1(Task task, Code code, CancerStudy cancerStudy, CancerStudy cancerStudyProcess, Map<String, Object> map)  {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    processCancerStudy(task, code, cancerStudy, cancerStudyProcess, map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        executorService.submit(runnable);
////        ControlSubThread controlSubThread = new ControlSubThread(task, code, cancerStudy, cancerStudyProcess, map);
//        executorService.submit(controlSubThread);
        //        controlSubThread.stop();
    }
    @Override
    @Async("taskExecutor")
    public void processCancerStudy2(Task task, Code code,CancerStudy cancerStudy,CancerStudy cancerStudyProcess,Map<String, Object> map)  {
        processCancerStudy(task,code,cancerStudy,cancerStudyProcess,map);
    }




    public void processCancerStudy(Task task, Code code,CancerStudy cancerStudy,CancerStudy cancerStudyProcess,Map<String, Object> map)  {
        /***************************************************************/
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
//        springWebSocketHandler.sendMessageToUsers(new TextMessage(Thread.currentThread().getName()+": start! >>>>>>>>>>>>>>>>>>>"));
        task.setThreadName(Thread.currentThread().getName());
        task.setTaskStatus(TaskStatus.RUNNING);
        task.setRunMsg(task.getRunMsg()+"\n"+Thread.currentThread().getName()+"开始分析！"+ new Date());
        taskService.save(task);
        /*****************************************************************/

//        CodeMsg codeMsg = rCall(code, map);
        CodeMsg codeMsg  = processBuilder(task,code, map);;
        try {
            if(codeMsg.getCancerStudyParam()!=null){
                CancerStudy covertCancerStudy = cancerStudyService.convert(codeMsg.getCancerStudyParam());
                if(codeMsg.getIsUpdate()){
                    BeanUtil.copyProperties(covertCancerStudy,cancerStudy);
                    cancerStudyService.saveCancerStudy(cancerStudy);
                }else {
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
        task.setRunMsg(task.getRunMsg()+"\n"+Thread.currentThread().getName()+"分析结束！"+ new Date());
        taskService.save(task);
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        springWebSocketHandler.sendMessageToUsers(new TextMessage(JSONObject.toJSON(task).toString()));
        /*****************************************************************/
    }

    @Override
    public Task shutdownProcess(int taskId){
        Task task = taskService.findById(taskId);
        if(processMap.containsKey(task.getId())){
            TaskProcess taskProcess = processMap.get(task.getId());
            if(taskProcess.getProcess().isAlive()){
                taskProcess.getProcess().destroy();
                processMap.remove(task.getId());
                log.info("结束 {}",taskProcess.getTask().getName());
                taskProcess.getTask().setRunMsg(taskProcess.getTask().getRunMsg()+"\nshutdownProcess by user");
            }
            return taskProcess.getTask();
        }
        if(executorService.getActiveCount()==0 && !task.getTaskStatus().equals(TaskStatus.FINISH)){

            task.setTaskStatus(TaskStatus.FINISH);
            taskService.save(task);
            throw new BioinfoException("出错了，线程已经结束，状态没有更改！");
        }
        throw new BioinfoException("不能结束该进程！");
    }

    private CodeMsg processBuilder(Task task,Code code, Map<String,Object> maps){
        CodeMsg codeMsg = new CodeMsg();
        File  tempFile=null;
        try {
            String workDir = StringCacheStore.getValue("workDir");
            Path path = Paths.get(workDir, "data");
            Files.createDirectories(path);
            tempFile = File.createTempFile("bioinfo-", ".R");
            maps.put("workDir",path.toString());
            StringBuffer stringBuffer = new StringBuffer();
            for (String key: maps.keySet()){
                Object obj = maps.get(key);
                if(obj instanceof String && obj!=null){
                    stringBuffer.append(key+" <- \""+ obj+"\"\n");
                }else if (obj instanceof Cancer){
                    stringBuffer.append(key+" <- \""+ ((Cancer) obj).getEnName()+"\"\n");
                }
            }
            stringBuffer.append("\n");
            if(code.getAbsolutePath()!=null){
                byte[] bytes = Files.readAllBytes(Paths.get(code.getAbsolutePath()));
                String content = new String(bytes, StandardCharsets.UTF_8);
                stringBuffer.append(content);
                try (FileOutputStream fop = new FileOutputStream(tempFile)) {
                    fop.write(stringBuffer.toString().getBytes());
                    fop.flush();
                }
            }


            ProcessBuilder processBuilder = new ProcessBuilder();

            processBuilder.directory(path.toFile());
            List<String> command = new ArrayList<>();
            command.add("Rscript");
            command.add(tempFile.getAbsolutePath());
            processBuilder.command(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            processMap.put(task.getId(),new TaskProcess(task,process));
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
                    if(!line.startsWith("Downloading")){
                        result.append(line);
                    }
                    log.info(line);

                }
            }
            process.waitFor();
            if(resultMap.containsKey("update")){
                codeMsg.setIsUpdate(true);
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



            int exit = process.exitValue();
            if (exit != 0) {
                codeMsg.setStatus(false);
                codeMsg.setRunMsg("failed to execute:" + processBuilder.command() + " with result:" + result);
//                throw new IOException("failed to execute:" + processBuilder.command() + " with result:" + result);
            }else {
                codeMsg.setStatus(true);
                codeMsg.setResult("");
                codeMsg.setRunMsg(result.toString());
                codeMsg.setCancerStudyParam(cancerStudyParam);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            codeMsg.setStatus(false);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            codeMsg.setStatus(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            codeMsg.setStatus(false);
        }finally {
            if(tempFile!=null){
                tempFile.delete();
            }
            TaskProcess taskProcess = processMap.get(task.getId());
            if(taskProcess!=null){
                if(taskProcess.getProcess().isAlive()){
                    taskProcess.getProcess().destroy();
                }
                processMap.remove(task.getId());
            }
        }

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
