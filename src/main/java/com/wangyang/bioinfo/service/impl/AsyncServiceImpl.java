package com.wangyang.bioinfo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.rcaller.FunctionCall;
import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import com.wangyang.bioinfo.core.KeyLock;
import com.wangyang.bioinfo.handle.ICodeResult;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.websocket.WebSocketServer;
import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.dto.TaskProcess;
import com.wangyang.bioinfo.pojo.enums.TaskStatus;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.pojo.trem.Cancer;
import com.wangyang.bioinfo.repository.TaskRepository;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.util.BeanUtil;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.CacheStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.TextMessage;

import javax.swing.text.html.parser.Entity;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class AsyncServiceImpl implements IAsyncService  {


    private final TaskRepository taskRepository;
    private final ICancerStudyService cancerStudyService;
    private final WebSocketServer springWebSocketHandler;
    private final ThreadPoolTaskExecutor executorService;
    private final Map<Integer, TaskProcess> processMap;
    private final KeyLock<String> lock;
    public AsyncServiceImpl(TaskRepository taskRepository,
                            ICancerStudyService cancerStudyService,
                            WebSocketServer springWebSocketHandler,
                            ThreadPoolTaskExecutor executorService,
                            KeyLock<String> lock){
        processMap= new HashMap<>();
        this.taskRepository=taskRepository;
        this.cancerStudyService=cancerStudyService;
        this.springWebSocketHandler=springWebSocketHandler;
        this.executorService=executorService;
        this.lock=lock;
    }



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
    public void processCancerStudy1(User user, Task task, Code code, BaseFile baseFile,ICodeResult<? extends BaseFile> codeResult)  {
        Runnable runnable = () -> {
            try {
                processCancerStudy(user,task, code,baseFile, codeResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        executorService.submit(runnable);
        processMap.put(task.getId(),new TaskProcess(task,runnable,null));

    }
    @Override
    @Async("taskExecutor")
    public void processCancerStudy2(User user,Task task, Code code,CancerStudy cancerStudy,CancerStudy cancerStudyProcess,Map<String, Object> map)  {
//        processCancerStudy(user,task,code,cancerStudy,cancerStudyProcess,map);
    }


    interface A{
        void a(CodeMsg codeMsg);
        CodeType b();
    }

    public void processCancerStudy(User user,Task task, Code code,BaseFile baseFile,ICodeResult  codeResult)  {
        try {
            lock.lock("task"+task.getId());
            log.debug(">>>>>>>>>>>>>>>>>>>>>>processCancerStudy task{} 加锁",task.getId());
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
            task.setThreadName(Thread.currentThread().getName());
            task.setTaskStatus(TaskStatus.RUNNING);
            task.setRunMsg(task.getRunMsg()+"\n"+Thread.currentThread().getName()+"开始分析！"+ new Date());
            taskRepository.save(task);
            /*****************************************************************/

            Map map = codeResult.getMap(baseFile);
            CodeMsg codeMsg  = processBuilder(task,code, map);
            codeResult.call(code,user,codeMsg,baseFile);

            /*****************************************************************/
            task.setResult(codeMsg.getResult());
            task.setRunMsg(codeMsg.getRunMsg());
            task.setTaskStatus(TaskStatus.FINISH);
            task.setRunMsg(task.getRunMsg()+"\n"+Thread.currentThread().getName()+"分析结束！"+ new Date());
            taskRepository.save(task);
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            springWebSocketHandler.sendMessageToUser(user.getUsername(),JSONObject.toJSON(task).toString());
        } finally {
            lock.unlock("task"+task.getId());
            log.debug(">>>>>>>>>>>>>>>>>>>>>>processCancerStudy task{} 解锁",task.getId());
        }
    }

    @Override
    public Task shutdownProcess(int taskId){

        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(!optionalTask.isPresent()){
            throw new BioinfoException("Task is not found!");
        }
        Task task = optionalTask.get();
        if(processMap.containsKey(task.getId())){
            TaskProcess taskProcess = processMap.get(task.getId());

            try {
                lock.lock("task-finish"+task.getId());
                log.debug("<<<<<<<<<<<<<<<<<<<<<<<shutdownProcess task{} 加锁",task.getId());
                if(taskProcess.getProcess() !=null && taskProcess.getProcess().isAlive()){
//                    taskProcess.getProcess().destroyForcibly();
//                    taskProcess.getProcess().destroy();
                    taskProcess.setFlag(false);
                    log.info("结束 {}",taskProcess.getTask().getName());
                    taskProcess.getTask().setRunMsg(taskProcess.getTask().getRunMsg()+"\nshutdownProcess by user");
                }
                executorService.getThreadPoolExecutor().getQueue().remove(taskProcess.getRunnable());
                processMap.remove(task.getId());
                task.setTaskStatus(TaskStatus.INTERRUPT);
                taskRepository.save(task);
            } finally {
                lock.unlock("task-finish"+task.getId());
                log.debug("<<<<<<<<<<<<<<<<<<<<<<<shutdownProcess task{} 解锁",task.getId());
            }
            return taskProcess.getTask();
        }
        throw new BioinfoException("该任务已经结束！");
    }

    private CodeMsg processBuilder(Task task, Code code, Map<String,Object> maps){
        CodeMsg codeMsg = new CodeMsg();
        File  tempFile=null;
        FileOutputStream outputStream =null;
        try {
            String workDir = CacheStore.getValue("workDir");
            Path path = Paths.get(workDir, "data");
            maps.put("workDir",path.toString());
            Files.createDirectories(path);
            tempFile = File.createTempFile("bioinfo-",".run");
            List<String> command = new ArrayList<>();
            StringBuffer stringBuffer = new StringBuffer();
            buildFile(code,stringBuffer,command,maps);
            command.add(tempFile.getAbsolutePath());

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
            processBuilder.command(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            // 添加Process
            TaskProcess taskProcess = processMap.get(task.getId());
            taskProcess.setProcess(process);

            StringBuilder result = new StringBuilder();

            Path logPath = Paths.get(workDir, "log",task.getId()+".log");
            Files.createDirectories(logPath.getParent());
            if(!logPath.toFile().exists()){
                Files.createFile(logPath);
            }
            Map<String,String> resultMap = new HashMap<>();
            outputStream = new FileOutputStream(logPath.toFile());
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                String line;
                while ((line = reader.readLine()) != null) {
                    if(!taskProcess.getFlag()){
                        process.destroy();
                        break;
                    }
                    if(line.startsWith("$")) {
                        String[] strings = line.split(":");
                        String key = strings[0].substring(1);
                        String value = strings[1];
                        resultMap.put(key,value);
                    }
                    if(!line.startsWith("Downloading")){
                        result.append(line);
                    }
                    outputStream.write((Thread.currentThread().getName()+": "+line+"\n").getBytes());
                    log.debug(line);


                }
            }
            process.waitFor();
            if(resultMap.containsKey("update")){
                codeMsg.setIsUpdate(true);
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
                // 获取结果 cat("$absolutePath:",paste0(workDir,"/",filename,".gz"))
                codeMsg.setResultMap(resultMap);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            codeMsg.setStatus(false);
        } finally {
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
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return codeMsg;
    }


    private void buildFile(Code code,StringBuffer stringBuffer,List<String> command,Map<String, Object> maps) {
        if(code.getCodeType().equals(CodeType.R)){
            for (String key: maps.keySet()){
                Object obj = maps.get(key);
                if(obj instanceof String && obj!=null){
                    stringBuffer.append(key+" <- \""+ obj+"\"\n");
                }else if (obj instanceof Cancer){
                    stringBuffer.append(key+" <- \""+ ((Cancer) obj).getEnName()+"\"\n");
                }
            }
            stringBuffer.append("\n");
            command.add("Rscript");
        }else if (code.getCodeType().equals(CodeType.SHELL)){
            for (String key: maps.keySet()){
                Object obj = maps.get(key);
                if(obj instanceof String && obj!=null){
                    stringBuffer.append(key+"=\""+ obj+"\"\n");
                }else if (obj instanceof Cancer){
                    stringBuffer.append(key+"=\""+ ((Cancer) obj).getEnName()+"\"\n");
                }
            }
            stringBuffer.append("\n");
            command.add("bash");
        }else if (code.getCodeType().equals(CodeType.PYTHON)){

        }
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
//            codeMsg.setCancerStudyParam(cancerStudyParam);
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
