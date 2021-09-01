package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/24
 */
public interface TaskRepository extends BaseRepository<Task,Integer> ,
        JpaSpecificationExecutor<Task> {

    List<Task> findByObjId(int objId);
    List<Task> findByCodeId(int codeId);
}
