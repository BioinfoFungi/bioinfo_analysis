package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Cancer;
import com.wangyang.bioinfo.pojo.DataOrigin;
import com.wangyang.bioinfo.pojo.Study;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.param.DataOriginParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/26
 */
public interface IDataOriginService {
    DataOrigin addDataOrigin(DataOriginParam dataOriginParam,User user);
    DataOrigin delDataOrigin(int id);
    DataOrigin findDataOriginById(int id);
    DataOrigin findDataOriginByEnName(String name);
    DataOrigin findAndCheckByEnName(String name);

    List<DataOrigin> findAllById(Collection<Integer> id);
    List<DataOrigin> listAll();
    Page<DataOrigin> pageDataOrigin(Pageable pageable);
}
