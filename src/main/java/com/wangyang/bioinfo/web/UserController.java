package com.wangyang.bioinfo.web;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.wangyang.bioinfo.pojo.annotation.Anonymous;
import com.wangyang.bioinfo.pojo.authorize.*;
import com.wangyang.bioinfo.pojo.dto.Captcha;
import com.wangyang.bioinfo.pojo.dto.PermMenu;
import com.wangyang.bioinfo.pojo.dto.UserDto;
import com.wangyang.bioinfo.pojo.entity.Menu;
import com.wangyang.bioinfo.service.IMenuService;
import com.wangyang.bioinfo.service.IRoleService;
import com.wangyang.bioinfo.service.IUserService;
import com.wangyang.bioinfo.pojo.support.Token;
import com.wangyang.bioinfo.util.BaseResponse;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.TokenProvider;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

    @Autowired
    IMenuService menuService;

    @Autowired
    private Producer producer;

    @GetMapping
    public Page<User> page(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        return userService.pageUser(pageable);
    }

    @GetMapping("/permmenu")
    public PermMenu getPermmenu(HttpServletRequest request){
        PermMenu permMenu = new PermMenu();
        List<Menu> menus = menuService.listAll();
        permMenu.setPerms(new ArrayList<>());
        permMenu.setMenus(menus);
        return permMenu;
    }

    @GetMapping("/info")
    public User getInfo(HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        user= userService.findById(user.getId());
        user.setPassword("");
        return user;
    }

    @GetMapping("/listAll")
    public List<UserDto> listAll(){
        return userService.listAllUserDto();
    }

    @GetMapping("/captcha")
    @Anonymous
    public BaseResponse captcha( HttpServletRequest request){
        /**
         * Cache-Control指定请求和响应遵循的缓存机制
         * no-store:用于防止重要的信息被无意的发布。在请求消息中发送将使得请求和响应消息都不使用缓存。
         * no-cache:指示请求或响应消息不能缓存
         */
//        response.setHeader("Cache-Control","no-store,no-cache");
//
//        // 设置输出流内容格式为图片格式.image/jpeg,图片格式用于生成图片随机码
//        response.setContentType("image/jpeg");

        // 生成文字验证码
        String text = producer.createText();

        // 生成图片验证码
        BufferedImage image = producer.createImage(text);
        // 保存验证码到session中
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY,text);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();


        ServletOutputStream outputStream = null;
        try {
//            outputStream = response.getOutputStream();
//
//            ImageIO.write(image,"jpg",outputStream);
            ImageIO.write(image, "jpg", stream);
            byte[] bytes = Base64.encodeBase64(stream.toByteArray());
            String base64 = new String(bytes);
            Captcha captcha = new Captcha();
            captcha.setImg("data:image/jpeg;base64,"+base64);
            return  BaseResponse.ok("success",captcha);

        } catch (IOException e) {
            e.printStackTrace();
            throw new BioinfoException("error!!");
        }

//        IOUtils.closeQuietly(outputStream);
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
