package com.wangyang.bioinfo.handle;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.CancerStudy;
import com.wangyang.bioinfo.pojo.entity.base.BaseFile;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.pojo.entity.Code;

import java.util.List;
import java.util.Map;

public interface ICodeResult<T extends BaseFile>{
    void call(Code code, User user, CodeMsg codeMsg, T t);
    Map<String, String>  getMap(T t);
    T getObj(int id);
    TaskType getTaskType();
    void getRealTimeMsg(User user, String msg);

    List<T> getProcessObj(T t,Code code,String json);

    Boolean checkExist(List<CancerStudy> cancerStudies);

    T save(T t,User user);

    void call(User user,T t,List<T> ts);

    Boolean checkRun(Code code, BaseFile baseFile);
}
