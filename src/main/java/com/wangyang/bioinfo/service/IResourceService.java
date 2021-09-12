package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.authorize.Resource;
import com.wangyang.bioinfo.pojo.authorize.ResourceVO;
import com.wangyang.bioinfo.service.base.ICrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/5/5
 */
public interface IResourceService  extends ICrudService<Resource, Integer> {
    Resource findRoleById(int id);
    Resource delResource(int id);
    Page<Resource> pageResource(Pageable pageable);
    Resource listByUri(String Uri);

    List<Resource> findByIds(Iterable<Integer> inputIds);

    List<Resource> findByWithoutIds(Iterable<Integer> inputIds);

    //
    List<ResourceVO> findByRoleId(Integer id);

    List<Resource> findByWithoutRoleId(Integer id);


//    List<Resource> findByIds(Iterable<Integer> inputIds);
//
//    List<ResourceVO> findByRoleId(Integer id);
//
//    List<Resource> findByWithoutRoleId(Integer id);
}
