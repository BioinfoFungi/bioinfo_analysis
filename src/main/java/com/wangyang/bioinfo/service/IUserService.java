package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.authorize.UserDetailDTO;
import com.wangyang.bioinfo.pojo.dto.UserDto;
import com.wangyang.bioinfo.service.base.IBaseAuthorizeService;
import com.wangyang.bioinfo.service.base.ICrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/5/5
 */
public interface IUserService  extends IBaseAuthorizeService<User> {
    User addUser(User user);
    List<UserDto> listAllUserDto();
    User delUser(int id);
    User findUserById(int id);
    List<User> findAllById(Collection<Integer> id);
    Page<User> pageUser(Pageable pageable);
    User updateUser(User user);
    UserDetailDTO login(String username, String password);
    User findUserByUsername(String username);
    // ---------------------------------------
}
