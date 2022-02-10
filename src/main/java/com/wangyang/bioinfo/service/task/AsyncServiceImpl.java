package com.wangyang.bioinfo.service.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.rcaller.FunctionCall;
import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import com.wangyang.bioinfo.core.KeyLock;
import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import com.wangyang.bioinfo.service.base.ICrudService;
import com.wangyang.bioinfo.task.ICodeResult;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.base.BaseFile;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.util.*;
import com.wangyang.bioinfo.websocket.WebSocketServer;
import com.wangyang.bioinfo.pojo.entity.Task;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.dto.TaskProcess;
import com.wangyang.bioinfo.pojo.enums.TaskStatus;
import com.wangyang.bioinfo.pojo.entity.CancerStudy;
import com.wangyang.bioinfo.pojo.entity.Code;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import com.wangyang.bioinfo.repository.task.TaskRepository;
import com.wangyang.bioinfo.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        processMap= new ConcurrentHashMap<Integer, TaskProcess>();
        this.taskRepository=taskRepository;
        this.cancerStudyService=cancerStudyService;
        this.springWebSocketHandler=springWebSocketHandler;
        this.executorService=executorService;
        this.lock=lock;
    }
    private final Pattern patternVar = Pattern.compile("\\$\\{(.*?)\\}");


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
    class TestRun implements Runnable{
        private User user;
        private Task task;
        private Code code;
        private BaseEntity baseEntity;
        private ICrudService<BaseEntity, Integer> crudService;

        public TestRun(User user, Task task, Code code, BaseEntity baseEntity, ICrudService<BaseEntity, Integer> crudService) {
            this.user = user;
            this.task = task;
            this.code = code;
            this.baseEntity = baseEntity;
            this.crudService = crudService;
        }

        @Override
        public void run() {
            processCancerStudy(user,crudService,task, baseEntity,code);
        }
        public Integer getCodeId(){
            return code.getId();
        }
        public Integer getObjId(){
            return baseEntity.getId();
        }
    }

//    @Override
//    public void processCancerStudy1(User user, Task task, Code code, BaseFile baseFile,ICodeResult<? extends BaseFile> codeResult)  {
////        Runnable runnable = () -> {
////            processCancerStudy(user,task, code,baseFile, codeResult);
////        };
//
//        TestRun testRun = new TestRun(user,task,code,baseFile,codeResult);
//        executorService.submit(testRun);
//        processMap.put(task.getId(),new TaskProcess(task,testRun,null));
//
//    }

    @Override
    public void processCancerStudy1(User user, ICrudService<BaseEntity, Integer> crudService, Task task, BaseEntity baseEntity, Code code) {
//        executorService.setCorePoolSize(5);
        TestRun testRun = new TestRun(user,task,code,baseEntity,crudService);
        executorService.submit(testRun);

        processMap.put(task.getId(),new TaskProcess(task,testRun,null));
    }

    @Override
    @Async("taskExecutor")
    public void processCancerStudy2(User user,Task task, Code code,CancerStudy cancerStudy,CancerStudy cancerStudyProcess,Map<String, Object> map)  {
//        processCancerStudy(user,task,code,cancerStudy,cancerStudyProcess,map);
    }

    public void processCancerStudy(User user, ICrudService<BaseEntity, Integer> crudService, Task task, BaseEntity baseEntity, Code code)  {
        FileOutputStream logStream=null;
        File tempFile=null;
        File tempOutputFile = null;
        try {
            lock.lock("task"+task.getId());
            log.debug(">>>>>>>>>>>>>>>>>>>>>>processCancerStudy task{} 加锁",task.getId());
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");

//            Integer prerequisites = code.getPrerequisites();
//            CancerStudy perCancerStudy = cancerStudyService.findByParentIdAndCodeId(baseEntity.getId(), prerequisites);
//            // 该任务运行之前的任务没有运行需要先运行之前任务
//            if(prerequisites!=-1 && perCancerStudy ==null){
//                throw new BioinfoException("请先运行code："+prerequisites);
//            }

//            if(prerequisites!=-1){
//                Task preTask = taskRepository.findByObjIdAndCodeIdAndTaskType(baseEntity.getId(),prerequisites, codeResult.getTaskType());
//                if(preTask!=null && !preTask.getTaskStatus().equals(TaskStatus.FINISH)){
//                    throw new BioinfoException("请先确保code["+prerequisites+"]处于Finish状态");
//                }
//            }
            task.setThreadName(Thread.currentThread().getName());
            task.setTaskStatus(TaskStatus.RUNNING);
            task.setRunMsg(task.getRunMsg()+"\n"+Thread.currentThread().getName()+"开始分析！"+ new Date());

            String workDir = CacheStore.getValue("workDir");
            Path logPath = Paths.get(workDir, "log",task.getId()+".log");
            Files.createDirectories(logPath.getParent());
            if(!logPath.toFile().exists()){
                Files.createFile(logPath);
            }
            logStream = new FileOutputStream(logPath.toFile());



//            Map<String,String> map = new HashMap<>();
//            map.put("gse","GSE161784");

            Path path = Paths.get(workDir, "data");
            Files.createDirectories(path);
            tempFile = File.createTempFile("bioinfo-",".run");
            tempOutputFile = File.createTempFile("bioinfo-",".output");
            List<String> command = new ArrayList<>();
            StringBuffer stringBuffer = new StringBuffer();
            List<Field> fieldList = ObjectToCollection.setConditionFieldList(baseEntity);
            Set<String> vars = ServiceUtil.fetchProperty(fieldList, Field::getName);
            Map<String, String> map = ObjectToCollection.setConditionMap(baseEntity);
            map.put("workDir",path.toString());
            map.put("tempOutputFile",tempOutputFile.getAbsolutePath());
            buildFile(code,stringBuffer,command,map,tempFile,tempOutputFile.getAbsolutePath(),vars);
            task.setGenerateCode(stringBuffer.toString());
            taskRepository.save(task);

            CodeMsg codeMsg  = processBuilder(task.getId(),path.toFile(), command,logStream);
            if(codeMsg.getStatus()){
                task.setTaskStatus(TaskStatus.FINISH);
                task.setRunMsg(codeMsg.getRunMsg()+"\n"+Thread.currentThread().getName()+"分析结束！"+ new Date());
                buildOutput(task,baseEntity,tempOutputFile);

                taskRepository.save(task);
                crudService.save(baseEntity);

                springWebSocketHandler.sendMessageToUser(user.getUsername(), BaseResponse.ok(BaseResponse.MsgType.NOTIFY,"["+task.getName()+"]成功运行任务！",task));
            }else {
                task.setTaskStatus(TaskStatus.ERROR);
                task.setRunMsg(codeMsg.getRunMsg()+"\n"+Thread.currentThread().getName()+"分析结束！"+ new Date());
                taskRepository.save(task);
                springWebSocketHandler.sendMessageToUser(user.getUsername(), BaseResponse.ok(BaseResponse.MsgType.NOTIFY,"["+task.getName()+"]运行任务失败！",task));
            }
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        } catch (Exception e){

            e.printStackTrace();
            task.setTaskStatus(TaskStatus.ERROR);
            task.setRunMsg(task.getRunMsg()+"\n"+e.getMessage()+"分析结束！"+ new Date());
            taskRepository.save(task);
            springWebSocketHandler.sendMessageToUser(user.getUsername(),BaseResponse.ok(BaseResponse.MsgType.NOTIFY,"任务运行失败！",task));
            try {
                logStream.write(showErrorMsg(e.getMessage()).getBytes());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }finally {

            if(logStream!=null){
                try {
                    logStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(tempFile!=null){
                tempFile.delete();
            }
            if(tempOutputFile!=null){
                tempOutputFile.delete();
            }
            TaskProcess taskProcess = processMap.get(task.getId());
            if(taskProcess!=null){
                if(taskProcess.getProcess()!=null &&taskProcess.getProcess().isAlive()){
                    taskProcess.getProcess().destroy();
                }
                processMap.remove(task.getId());
            }
            lock.unlock("task"+task.getId());
            log.debug(">>>>>>>>>>>>>>>>>>>>>>processCancerStudy task{} 解锁",task.getId());
        }
    }


    private CodeMsg processBuilder(Integer taskId,
                                   File directory,
                                   List<String> command,
                                   OutputStream logStream) throws Exception {

        CodeMsg codeMsg = new CodeMsg();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(directory);
        processBuilder.command(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        // 添加Process
        TaskProcess taskProcess = processMap.get(taskId);
        taskProcess.setProcess(process);
        StringBuilder runMsg = new StringBuilder();

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
            String line;
            while ((line = reader.readLine()) != null) {
                if(!taskProcess.getFlag()){
                    process.destroy();
                    String msg = Thread.currentThread().getName()+": "+"手动停止进程,"+taskProcess.getTask().getName()+"\n";
                    logStream.write(msg.getBytes());
                    break;
                }
//                    if(line.startsWith("$")) {
//                        String[] strings = line.split(":");
//                        String key = strings[0].substring(1);
//                        String value = strings[1];
//                        resultMap.put(key,value);
//                    }
                if(!line.startsWith("Downloading")){
                    runMsg.append(line);
                }
                String msg = Thread.currentThread().getName()+": "+line+"\n";
//                    codeResult.getRealTimeMsg(user,msg);
                logStream.write(msg.getBytes());
                log.debug(line);
            }
        }
        process.waitFor();

//            Map<String,Map<String,String>> splitMap = new HashMap();
//            for (String key:resultMap.keySet()){
//                String subscript = key.substring(0, 3);
//                if(splitMap.get(subscript)==null){
//                    Map<String,String> midMap = new HashMap<>();
//                    splitMap.put(subscript,midMap);
//                }
//                splitMap.get(subscript).put(key.substring(3),resultMap.get(key));
//            }
//            List<Map<String, String>> resultList = splitMap.values().stream().collect(Collectors.toList());

        int exit = process.exitValue();
        if (exit != 0) {
            codeMsg.setStatus(false);
            throw new BioinfoException("failed to execute:" + processBuilder.command());
        }else {
            codeMsg.setStatus(true);
            codeMsg.setRunMsg(runMsg.toString());
        }
        return codeMsg;
    }

    public String getCommonFun(String path)throws IOException{
        Path rFunPath = FileUtil.getJarResources(path);
        String fun = new String(Files.readAllBytes(rFunPath), StandardCharsets.UTF_8);
        return fun;
    }
    /**
     *
     * @param code
     * @param stringBuffer
     * @param command
     * @param maps
     * @param tempFile
     * @param tempOutputFile
     * @param vars 对象的字段
     * @throws IOException
     */
    private void buildFile(Code code,
                           StringBuffer stringBuffer,
                           List<String> command,
                           Map<String, String> maps,
                           File tempFile,
                           String tempOutputFile,
                           Set<String> vars ) throws IOException {
        String content=null;
        if(code.getFileName()!=null){
            String bashPtah = CacheStore.getValue("workDir");
            Path codePath = Paths.get(bashPtah, code.getRelativePath());
            if(!codePath.toFile().exists()){
                throw new BioinfoException("code不存在！！");
            }
            byte[] bytes = Files.readAllBytes(codePath);
            content = new String(bytes, StandardCharsets.UTF_8);
        }

        if(code.getCodeType().equals(CodeType.R)){

            for (String key: maps.keySet()){
                String value = maps.get(key);
                stringBuffer.append(key+" <- \""+ value+"\"\n");
            }
            String rFun = getCommonFun("rFun.R");
            stringBuffer.append(rFun+"\n");

            stringBuffer.append(content);
            stringBuffer.append("\n");
            vars.forEach(var->{
                stringBuffer.append("if(exists(\""+var+"\") && is.character("+var+"))cat(paste0(\""+var+":\","+var+",\"\\n\"),append=T, file=\""+tempOutputFile+"\")\n");
            });
            command.add("Rscript");
            command.add(tempFile.getAbsolutePath());
        }else if (code.getCodeType().equals(CodeType.SHELL)){
            for (String key: maps.keySet()){
                String value = maps.get(key);
                stringBuffer.append(key+"=\""+ value+"\"\n");
            }
            stringBuffer.append(content);
            stringBuffer.append("\n");
            vars.forEach(var->{
                stringBuffer.append("echo \""+var+":\"$"+var+" >> "+tempOutputFile+"\n");
            });

            command.add("bash");
            command.add(tempFile.getAbsolutePath());
        }else if (code.getCodeType().equals(CodeType.PYTHON)){

        }

        try (FileOutputStream fop = new FileOutputStream(tempFile)) {
            fop.write(stringBuffer.toString().getBytes());
            fop.flush();
        }
    }

    private BaseEntity buildOutput( Task task,BaseEntity baseEntity,File tempOutputFile) throws IOException {
//        List<String> variable = getVariable(code.getCodeOutput());
//        if (variable.size()==0)return null;
        List<String> lines = Files.readAllLines(tempOutputFile.toPath());
//        if(variable.size()!=lines.size()){
//            String vars = variable.stream().collect(Collectors.joining(","));
//            throw new BioinfoException(vars+"必须输出！！");
//        }
        Map<String,String> map = new HashMap<>();
        List<String> listPic = new ArrayList<>();

        Boolean flag=false;
        String content="";
        for (String line : lines){
            if(line.equals(""))continue;

            if(line.startsWith("---start---")){
                flag=true;
                content="";
                continue;
            }
            if(line.startsWith("---end---")){
                listPic.add(content);
                flag=false;
                continue;
            }
            if(flag){
                content+=line;
//                if(!picMap.containsKey(num)){
//                    picMap.put(num,line);
//                }else {
//                    picMap.put(num, picMap.get(num)+"\n"+line);
//                }
            }else {
                String[] split = line.split(":");
                if (split.length==2){
                    map.put(split[0],split[1]);
                }
            }
        }

        task.setSvgJson(JSONArray.toJSONString(listPic));

        List<Method> methods = ObjectToCollection.getAllMethods(baseEntity);
        for (Method method : methods){
            String methodName = method.getName();
            String filedName = methodName.substring(3,4).toLowerCase()+methodName.substring(4);
            if(methodName.startsWith("set")&&map.keySet().contains(filedName)){
                try {
                    Class<?> classes = method.getParameterTypes()[0];
                    if(classes.toString().equals(String.class.toString())){
                        method.invoke(baseEntity,map.get(filedName));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return baseEntity;
    }


    private String showErrorMsg(String msg){
        return Thread.currentThread().getName()+"[Error]: "+msg+"\n";
    }

    private List<String> getVariable(String codeOutput) {
        List<String> list = new ArrayList<>();
        if(codeOutput==null)return list;
        Matcher m = patternVar.matcher(codeOutput);
        while (m.find()) {
            list.add(m.group(1));
        }
        return list;
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
//            codeMsg.setIsUpdate(true);
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
//            codeMsg.setResult(jsonObject.toString());
//            codeMsg.setCancerStudyParam(cancerStudyParam);
            codeMsg.setRunMsg(byteArrayOutputStream.toString());
            codeMsg.setStatus(true);


//            springWebSocketHandler.sendMessageToUsers(new TextMessage(byteArrayOutputStream.toByteArray()));
            System.out.println(byteArrayOutputStream);
            return codeMsg;
        } catch (Exception e) {
            codeMsg.setRunMsg(byteArrayOutputStream.toString());
//            codeMsg.setException(e.getMessage());
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
