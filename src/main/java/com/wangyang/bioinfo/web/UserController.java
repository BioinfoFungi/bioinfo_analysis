package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.annotation.Anonymous;
import com.wangyang.bioinfo.pojo.authorize.*;
import com.wangyang.bioinfo.pojo.dto.UserDto;
import com.wangyang.bioinfo.service.IRoleService;
import com.wangyang.bioinfo.service.IUserService;
import com.wangyang.bioinfo.pojo.support.Token;
import com.wangyang.bioinfo.util.TokenProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/5/5
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    IUserService userService;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    IRoleService roleService;

    @GetMapping
    public Page<User> page(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        return userService.pageUser(pageable);
    }

    @GetMapping("/listAll")
    public List<UserDto> listAll(){
        return userService.listAllUserDto();
    }



    @PostMapping("/login")
    @Anonymous
    public LoginUser login(@RequestBody UserLoginParam inputUser){
        UserDetailDTO user = userService.login(inputUser.getUsername(), inputUser.getPassword());
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(user,loginUser);
        Token token = tokenProvider.generateToken(user);
        loginUser.setToken(token.getToken());
        return loginUser;
    }

    @PostMapping
    public User addUser(@RequestBody @Validated UserParam inputUser){
        return userService.addUser(inputUser);
    }

    @PostMapping("/update/{id}")
    public User updateUser(@PathVariable("id") Integer id,@RequestBody @Validated UserParam inputUser){
        return userService.updateUser(id,inputUser);
    }

    @GetMapping("/del/{id}")
    public User delUser(@PathVariable("id") Integer id){
        return userService.delUser(id);
    }
    @GetMapping("/findById/{id}")
    public User findById(@PathVariable("id") Integer id){
        return userService.findById(id);
    }
}
