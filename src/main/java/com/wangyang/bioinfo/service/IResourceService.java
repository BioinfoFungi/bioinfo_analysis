package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.authorize.Resource;
import com.wangyang.bioinfo.service.base.ICrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * @author wangyang
 * @date 2021/5/5
 */
public interface IResourceService  extends ICrudService<Resource, Integer> {
    Resource addResource(Resource resource);
    Resource findRoleById(int id);
    Resource delResource(int id);
    Page<Resource> pageResource(Pageable pageable);
    Resource listByUri(String Uri);

    Map<String, Resource> listAllMap();
}
