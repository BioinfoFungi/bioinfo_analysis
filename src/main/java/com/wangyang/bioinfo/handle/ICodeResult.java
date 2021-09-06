package com.wangyang.bioinfo.handle;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.pojo.file.Annotation;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;

import java.util.Map;

public interface ICodeResult<T extends BaseFile>{
    void call(Code code, User user, CodeMsg codeMsg, T t);
    Map<String, Object>  getMap(T t);
    T getObj(int id);
    TaskType getTaskType();
    void getRealTimeMsg(User user, String msg);
}
