package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.enums.TaskStatus;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.repository.TaskRepository;
import com.wangyang.bioinfo.service.ICancerStudyService;
import com.wangyang.bioinfo.service.ICodeService;
import com.wangyang.bioinfo.service.ITaskService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import com.wangyang.bioinfo.util.BioinfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

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


    //TUDO
    private int QUEUE_CAPACITY= 300;


    @Override
    public Task addTaskByCancerStudyId(Integer cancerStudyId){
        CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(cancerStudyId);
        Task task = new Task();
        task.setName(cancerStudy.getEnName());
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
                CancerStudy cancerStudy = cancerStudyService.findCancerStudyById(task.getObjId());
                codeService.processAsyncByCancerStudy(task,cancerStudy);
            });
        }
        return tasks;
    }
}
