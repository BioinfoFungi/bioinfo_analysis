package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.handle.ICodeResult;
import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.enums.TaskStatus;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.pojo.file.Annotation;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.TaskParam;
import com.wangyang.bioinfo.pojo.param.TaskQuery;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVO;
import com.wangyang.bioinfo.pojo.vo.TermMappingVo;
import com.wangyang.bioinfo.repository.TaskRepository;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import com.wangyang.bioinfo.util.BeanUtil;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.ObjectToCollection;
import com.wangyang.bioinfo.util.CacheStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/7/24
 */
@Service
@Slf4j
public class TaskServiceImpl extends AbstractCrudService<Task,Integer>
        implements  ITaskService{

    private final TaskRepository taskRepository;
    private final ICancerStudyService cancerStudyService;
    private final ICodeService codeService;
    private final IAsyncService asyncService;
    private final IOrganizeFileService organizeFileService;
    private final IAnnotationService annotationService;
    //TUDO
    private int QUEUE_CAPACITY= 300;

    public TaskServiceImpl(TaskRepository taskRepository,
                           ICancerStudyService cancerStudyService,
                           ICodeService codeService,
                           IAsyncService asyncService,
                           IOrganizeFileService organizeFileService,
                           IAnnotationService annotationService) {
        super(taskRepository);
        this.taskRepository=taskRepository;
        this.cancerStudyService=cancerStudyService;
        this.codeService=codeService;
        this.asyncService=asyncService;
        this.organizeFileService=organizeFileService;
        this.annotationService=annotationService;
    }


    @Override
    public Page<Task> page(TaskQuery taskQuery, Pageable pageable) {
        return taskRepository.findAll(buildSpec(taskQuery),pageable);
    }


    @Override
    public List<Task> findByCodeId(Integer codeId){
        return taskRepository.findAll(new Specification<Task>() {
            @Override
            public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("codeId"),codeId)).getRestriction();
            }
        });
    }

    public Task findByCanSIdACodeId(Integer cancerStudyId,Integer codeId,TaskType taskType){
        List<Task> tasks = taskRepository.findAll(new Specification<Task>() {
            @Override
            public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(
                        criteriaBuilder.equal(root.get("objId"),cancerStudyId),
                        criteriaBuilder.equal(root.get("codeId"),codeId),
                        criteriaBuilder.equal(root.get("taskType"),taskType)
                ).getRestriction();
            }
        });
        return tasks.size()==0?null:tasks.get(0);
    }

    @Override
    public List<Task> delByCodeId(Integer codeId){
        List<Task> taskList = findByCodeId(codeId);
        taskRepository.deleteAll(taskList);
        return taskList;
    }


    @Override
    public List<Task> findByCanSId(Integer canSId){
        return taskRepository.findAll(new Specification<Task>() {
            @Override
            public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("objId"),canSId)).getRestriction();
            }
        });
    }

    @Override
    public List<Task> delByCanSId(Integer canSId){
        List<Task> taskList = findByCanSId(canSId);
        taskRepository.deleteAll(taskList);
        return taskList;
    }

    private Specification<Task> buildSpec(TaskQuery taskQuery) {
        return (Specification<Task>) (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new LinkedList<>();
            if(taskQuery.getCancerStudyId()!=null){
                predicates.add(criteriaBuilder.equal(root.get("objId"),taskQuery.getCancerStudyId()));
            }

            return query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0]))).getRestriction();
        };
    }

    public  boolean runCheck(Task task){
        if(task!=null && !task.getTaskStatus().equals(TaskStatus.FINISH) && !task.getTaskStatus().equals(TaskStatus.INTERRUPT) ){
            return true;
        }else{
            return false;
        }
    }

    //    @Override
    public Task addAnnotationTask(TaskParam taskParam, User user) {
        ICodeResult<Annotation> annotationICodeResult  = new ICodeResult<Annotation>() {
            private Annotation annotation;
            @Override
            public void call(Code code, User user, CodeMsg codeMsg) {
                annotation.setAbsolutePath(codeMsg.getCancerStudyParam().getAbsolutePath());
                annotationService.save(annotation);
            }

            @Override
            public Map<String, Object> getMap() {
                Map<String, Object> map = new HashMap<>();
                map.putAll(ObjectToCollection.setConditionObjMap(annotation));
                return map;
            }

            @Override
            public TaskType getTaskType() {
                return TaskType.ANNOTATION;
            }

            @Override
            public Annotation getObj(int id){
                this.annotation = annotationService.findById(id);
                return this.annotation;
            }

        };
        return taskInit(user,taskParam,annotationICodeResult);
    }

    ICodeResult<CancerStudy> codeResult = new ICodeResult<CancerStudy>() {
        private CancerStudy cancerStudy;
        @Override
        public TaskType getTaskType() {
            return TaskType.CANCER_STUDY;
        }
        @Override
        public CancerStudy getObj(int id) {
            this.cancerStudy = cancerStudyService.findById(id);
            return cancerStudy;
        }
        @Override
        public Map<String, Object> getMap() {
            CancerStudyVO mappingVo = cancerStudyService.convertVo(cancerStudy);
            List<OrganizeFile> organizeFiles = organizeFileService.listAll();
            Map<String, Object> map = new HashMap<>();
            map.putAll(ObjectToCollection.setConditionObjMap(mappingVo));
            map.putAll(organizeFiles.stream().collect(Collectors.toMap(OrganizeFile::getEnName, OrganizeFile::getAbsolutePath)));
            return map;
        }
        @Override
        public void call(Code code, User user, CodeMsg codeMsg) {
            if (codeMsg.getCancerStudyParam() != null) {
                CancerStudy covertCancerStudy = cancerStudyService.convert(codeMsg.getCancerStudyParam());
                if (codeMsg.getIsUpdate()) {
                    BeanUtil.copyProperties(covertCancerStudy, cancerStudy);
                    cancerStudyService.saveCancerStudy(cancerStudy);
                } else {
                    CancerStudy cancerStudyProcess = cancerStudyService.findByParACodeId(cancerStudy.getId(), code.getId());
                    if (cancerStudyProcess == null) {
                        cancerStudyProcess = new CancerStudy();
                    }
                    cancerStudyProcess.setCancerId(cancerStudy.getCancerId());
                    cancerStudyProcess.setStudyId(cancerStudy.getStudyId());
                    cancerStudyProcess.setDataOriginId(cancerStudy.getDataOriginId());
                    cancerStudyProcess.setDataCategoryId(cancerStudy.getDataCategoryId());
                    cancerStudyProcess.setAnalysisSoftwareId(cancerStudy.getAnalysisSoftwareId());
                    cancerStudyProcess.setUserId(user.getId());
                    cancerStudyProcess.setCodeId(code.getId());
                    cancerStudyProcess.setParentId(cancerStudy.getId());
                    BeanUtil.copyProperties(covertCancerStudy, cancerStudyProcess);
                    cancerStudyService.saveCancerStudy(cancerStudyProcess);
                }
            }
        }
    };
    @Override
    public Task addTask(TaskParam taskParam, User user) {

        return taskInit(user,taskParam,codeResult);
    }

    @Override
    public Task runTask(Integer id, User user) {
        return runTask(id,user,codeResult);
    }

    public Task runTask(Integer id, User user,ICodeResult codeResult) {
        Task task = findById(id);
        if(runCheck(task)){
            throw new BioinfoException(task.getName()+" 已经运行或在队列中！");
        }
        Code code = codeService.findById(task.getCodeId());
        codeResult.getObj(task.getObjId());
        task.setTaskStatus(TaskStatus.UNTRACKING);
        task.setRunMsg(Thread.currentThread().getName()+"准备开始分析！"+ new Date());
        task= super.save(task);
        asyncService.processCancerStudy1(user,task,code,codeResult);
        return task;
    }


    private Task taskInit(User user,TaskParam taskParam, ICodeResult codeResult) {
        BaseFile baseFile = codeResult.getObj(taskParam.getObjId());
        Code code = codeService.findById(taskParam.getCodeId());
        TaskType taskType = codeResult.getTaskType();

        Task task = findByCanSIdACodeId(baseFile.getId(), code.getId(),taskType);

        if (runCheck(task)) {
            throw new BioinfoException(task.getName() + " 已经运行或在队列中！");
        }
        if (task == null) {
            task = new Task();
        }
        task.setObjId(baseFile.getId());
        task.setTaskType(taskType);
        task.setCodeId(code.getId());
        task.setTaskStatus(TaskStatus.UNTRACKING);
        task.setUserId(user.getId());
        task.setName(baseFile.getFileName()+code.getName());
        task.setRunMsg(Thread.currentThread().getName() + "准备开始分析！" + new Date());
        task=taskRepository.save(task);

        //交给thread
        asyncService.processCancerStudy1(user,task,code,codeResult);
        return task;
    }

    @Override
    public Task shutdownProcess(int taskId){
        Task task = asyncService.shutdownProcess(taskId);
        return task;
    }



    @Override
    public String getLogFiles(@NonNull Integer taskId,@NonNull  Integer lines){
        File file = new File(CacheStore.getValue("workDir"),"log/"+taskId+".log");
        if(!file.exists()){
            return "文件不存在！";
        }
        List<String> linesArray = new ArrayList<>();
        final StringBuilder result = new StringBuilder();
        long count = 0;
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            long length = randomAccessFile.length();
            if (length == 0L) {
                return StringUtils.EMPTY;
            } else {
                long pos = length - 1;
                while (pos > 0) {
                    pos--;
                    randomAccessFile.seek(pos);
                    if (randomAccessFile.readByte() == '\n') {
                        String line = randomAccessFile.readLine();
                        linesArray.add(new String(line.getBytes(StandardCharsets.ISO_8859_1),
                                StandardCharsets.UTF_8));
                        count++;
                        if (count == lines) {
                            break;
                        }
                    }
                }
                if (pos == 0) {
                    randomAccessFile.seek(0);
                    linesArray.add(new String(
                            randomAccessFile.readLine().getBytes(StandardCharsets.ISO_8859_1),
                            StandardCharsets.UTF_8));
                }
            }
        } catch (Exception e) {
            throw new BioinfoException("读取日志失败");
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Collections.reverse(linesArray);
        linesArray.forEach(line -> result.append(line).append("\n"));
        return result.toString();
    }



    //    @Override
//    public Task addTaskByCancerStudyId(Integer cancerStudyId){
//        CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(cancerStudyId);
//        Task task = new Task();
////        task.setName(cancerStudy.getEnName());
//        task.setObjId(cancerStudy.getId());
//
//        int size = executor.getThreadPoolExecutor().getQueue().size();
//        if(size==QUEUE_CAPACITY) {
//            System.out.println("线程池队列已满" + size);
//            task.setTaskStatus(TaskStatus.UNTRACKING);
//        }else {
//            task.setTaskStatus(TaskStatus.PENDING);
//            asyncService.processAsyncByCancerStudy(task,cancerStudy);
//        }
//        return super.save(task);
//    }
//

//    @Override
//    public Task findByCodeAndObj(int codeId, int objId, TaskType taskType){
//        List<Task> tasks = taskRepository.findAll(new Specification<Task>() {
//            @Override
//            public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                return criteriaQuery.where(criteriaBuilder.equal(root.get("codeId"),codeId),
//                        criteriaBuilder.equal(root.get("objId"),objId),
//                        criteriaBuilder.equal(root.get("taskType"),taskType)).getRestriction();
//            }
//        });
//        if(tasks.size()==0){
//            return null;
//        }
//        return tasks.get(0);
//    }

//    @Override
//    public Task addTask(Integer id, Integer cancerStudyId){
//        int size = executor.getThreadPoolExecutor().getQueue().size();
//        log.info("pool size {}",size);
//        CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(cancerStudyId);
//        Code code = codeService.findById(id);
//        Task task = findByCodeAndObj(code.getId(), cancerStudy.getId(),TaskType.CANCER_STUDY);
//        if(task!=null && (task.getTaskStatus() == TaskStatus.PENDING || task.getTaskStatus()==TaskStatus.RUNNING)){
//            springWebSocketHandler.sendMessageToUsers(new TextMessage("Task 正在运行或者已经添加到队列！"));
//            throw new BioinfoException("Task 正在运行或者已经添加到队列！");
//        }
//        if(task==null){
//            task = new Task();
//            task.setTaskType(TaskType.CANCER_STUDY);
//        }
//        task.setObjId(cancerStudy.getId());
//        task.setCodeId(code.getId());
//        task.setTaskStatus(TaskStatus.PENDING);
//
//        if(size==QUEUE_CAPACITY) {
//            System.out.println("线程池队列已满" + size);
//            task.setTaskStatus(TaskStatus.UNTRACKING);
//            task = taskRepository.save(task);
//        }else {
//            task = taskRepository.save(task);
//            // 将task传给线程， 由对应的线程执行更新操作
//            codeService.runRCode(task, code, cancerStudy);
//        }
//        return task;
//    }
//
//    @Override
//    public Task runCancerStudyTask(int id){
//        Task task = findById(id);
//        return runCancerStudyTask(task);
//    }
//
//
//    public Task runCancerStudyTask(Task task){
//        CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(task.getObjId());
//        Code code = codeService.findById(task.getCodeId());
//        codeService.runRCode(task, code, cancerStudy);
//        return task;
//    }

    @Override
    public List<Task> listPending(){
        return taskRepository.findAll(new Specification<Task>() {
            @Override
            public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("taskStatus"),TaskStatus.PENDING),
                        criteriaBuilder.equal(root.get("taskStatus"),TaskStatus.RUNNING)
                )).getRestriction();
            }
        });
    }

    @Override
    public  List<Task> addTas2Queue(){
        List<Task> tasks = listPending();
        if(tasks.size()>QUEUE_CAPACITY){
            tasks = tasks.subList(0,QUEUE_CAPACITY);
            List<Task> unDetail = tasks.subList(0,QUEUE_CAPACITY);
            System.out.println("未处理的Task:"+ unDetail.size());
        }
        if(tasks.size()!=0){
            tasks.forEach(task -> {
                log.info("服务器运行需要处理的task codeId：{}",task.getCodeId());
//                runCancerStudyTask(task);
//                CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(task.getObjId());
//                codeService.processAsyncByCancerStudy(task,cancerStudy);
            });
        }
        return tasks;
    }
}
