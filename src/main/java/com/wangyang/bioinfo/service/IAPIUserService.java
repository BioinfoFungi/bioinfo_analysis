package com.wangyang.bioinfo.service;


import com.wangyang.bioinfo.pojo.authorize.APIUser;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.service.base.IBaseAuthorizeService;

public interface IAPIUserService extends IBaseAuthorizeService<APIUser> {

    APIUser findByAuthorize(String authorize);
}
