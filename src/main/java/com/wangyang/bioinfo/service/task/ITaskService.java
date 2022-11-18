package com.wangyang.bioinfo.service.task;

import com.wangyang.bioinfo.pojo.entity.CancerStudy;
import com.wangyang.bioinfo.pojo.entity.Task;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.pojo.param.TaskParam;
import com.wangyang.bioinfo.pojo.param.TaskQuery;
import com.wangyang.bioinfo.service.base.ICrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author wangyang
 * @date 2021/7/24
 */
public interface ITaskService  extends ICrudService<Task, Integer> {

    Page<Task> page(TaskQuery taskQuery, Pageable pageable);

    List<Task> findByCodeId(Integer codeId);

    List<Task> delByCodeId(Integer codeId);

    List<Task> findByCanSId(Integer canSId);

    List<Task> delByCanSId(Integer canSId);

//    Task addTask(TaskParam taskParam, User user);

//    Map getObjMap(TaskType taskType, int objId);

    Task shutdownProcess(int taskId);
    Task addTask(CrudType crudEnum, ICrudService<BaseEntity,Integer> crudService, Integer id, Integer codeId, User user);
    Task addTask(CrudType crudEnum, ICrudService crudService,Integer taskId, Integer id, Map<String, String> map, Integer codeId, User user);

    Task runTask( ICrudService<BaseEntity,Integer> crudService,Integer id,User user);
//    Task addTaskByCancerStudyId(Integer cancerStudyId);
//
//    Task findByCodeAndObj(int codeId, int objId, TaskType taskType);
//
//    Task addTask(Integer id, Integer cancerStudyId);
//
//    Task runCancerStudyTask(int id);

    String getLogFiles(@NonNull Integer taskId, @NonNull Integer lines);

    List<Task> listPending();

    List<Task> addTas2Queue();


    List<CancerStudy>  runByCodeId(Integer id, User user);

    List<Task> listAll(CrudType crudEnum, Integer id);

    Task upload(MultipartFile file, User user, Integer taskId, TaskParam taskParam);
}
