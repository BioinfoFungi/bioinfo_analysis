package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.repository.base.BaseAuthorizeRepository;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangyang
 * @date 2021/5/5
 */
public interface UserRepository extends BaseAuthorizeRepository<User> {
}
