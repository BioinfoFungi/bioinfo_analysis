package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.handle.SpringWebSocketHandler;
import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.enums.TaskStatus;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.repository.TaskRepository;
import com.wangyang.bioinfo.service.ICancerStudyService;
import com.wangyang.bioinfo.service.ICodeService;
import com.wangyang.bioinfo.service.ITaskService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import com.wangyang.bioinfo.util.BioinfoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/24
 */
@Service
@Slf4j
public class TaskServiceImpl extends AbstractCrudService<Task,Integer>
                implements  ITaskService{

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    ICancerStudyService cancerStudyService;
    @Autowired
    ICodeService codeService;
    @Autowired
    ThreadPoolTaskExecutor executor;
    @Autowired
    SpringWebSocketHandler springWebSocketHandler;

    //TUDO
    private int QUEUE_CAPACITY= 300;


    @Override
    public Task addTaskByCancerStudyId(Integer cancerStudyId){
        CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(cancerStudyId);
        Task task = new Task();
//        task.setName(cancerStudy.getEnName());
        task.setObjId(cancerStudy.getId());

        int size = executor.getThreadPoolExecutor().getQueue().size();
        if(size==QUEUE_CAPACITY) {
            System.out.println("线程池队列已满" + size);
            task.setTaskStatus(TaskStatus.UNTRACKING);
        }else {
            task.setTaskStatus(TaskStatus.PENDING);
            codeService.processAsyncByCancerStudy(task,cancerStudy);
        }
        return super.save(task);
    }


    @Override
    public Task findByCodeAndObj(int codeId, int objId, TaskType taskType){
        List<Task> tasks = taskRepository.findAll(new Specification<Task>() {
            @Override
            public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("codeId"),codeId),
                        criteriaBuilder.equal(root.get("objId"),objId),
                        criteriaBuilder.equal(root.get("taskType"),taskType)).getRestriction();
            }
        });
        if(tasks.size()==0){
            return null;
        }
        return tasks.get(0);
    }

    @Override
    public Task addTask(Integer id, Integer cancerStudyId){
        int size = executor.getThreadPoolExecutor().getQueue().size();
        log.info("pool size {}",size);
        CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(cancerStudyId);
        Code code = codeService.findById(id);
        Task task = findByCodeAndObj(code.getId(), cancerStudy.getId(),TaskType.CANCER_STUDY);
        if(task!=null && (task.getTaskStatus() == TaskStatus.PENDING || task.getTaskStatus()==TaskStatus.RUNNING)){
            springWebSocketHandler.sendMessageToUsers(new TextMessage("Task 正在运行或者已经添加到队列！"));
            throw new BioinfoException("Task 正在运行或者已经添加到队列！");
        }
        if(task==null){
            task = new Task();
            task.setTaskType(TaskType.CANCER_STUDY);
        }
        task.setObjId(cancerStudy.getId());
        task.setCodeId(code.getId());
        task.setTaskStatus(TaskStatus.PENDING);
//        taskRepository.save(task);
        if(size==QUEUE_CAPACITY) {
            System.out.println("线程池队列已满" + size);
            task.setTaskStatus(TaskStatus.UNTRACKING);
        }else {
            // 将task传给线程， 由对应的线程执行更新操作
            codeService.runRCode(task, code, cancerStudy);
        }
        return super.save(task);
    }

    @Override
    public Task runCancerStudyTask(int id){
        Task task = findById(id);
        return runCancerStudyTask(task);
    }


    public Task runCancerStudyTask(Task task){
        CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(task.getObjId());
        Code code = codeService.findById(task.getCodeId());
        codeService.runRCode(task, code, cancerStudy);
        return task;
    }

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
                runCancerStudyTask(task);
//                CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(task.getObjId());
//                codeService.processAsyncByCancerStudy(task,cancerStudy);
            });
        }
        return tasks;
    }
}
