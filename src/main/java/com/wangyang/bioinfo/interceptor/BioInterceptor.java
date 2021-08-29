package com.wangyang.bioinfo.interceptor;

import com.wangyang.bioinfo.pojo.authorize.ApiUserDetailDTO;
import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.authorize.UserDetailDTO;
import com.wangyang.bioinfo.service.IPermissionService;
import com.wangyang.bioinfo.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/4/24
 */
public class BioInterceptor implements HandlerInterceptor {

    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    IPermissionService permissionService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if("OPTIONS".equals(request.getMethod().toString())) {
            return true;
        }
        String uri = request.getRequestURI();
        String method = request.getMethod();
        Set<Role> needsRoles = permissionService.findRolesByResource(method+uri);
        Set<String> needRoleStr = ServiceUtil.fetchProperty(needsRoles, Role::getEnName);

        if(needRoleStr.contains("anonymous")){
            User user = new User(-1);
            request.setAttribute("user",user);
            return true;
        }

        String authorize=null;
        String authorizationSdk = request.getHeader("authorizeSDK");
        String authorizeParam = request.getParameter("authorize");
        if(authorizationSdk!=null)authorize=authorizationSdk;
        if(authorizeParam!=null)authorize=authorizeParam;

        if(authorize!=null){
            ApiUserDetailDTO apiUserDetailDTO = permissionService.findSDKRolesByResource(authorize);
            for(Role needRole : needsRoles){
                if(apiUserDetailDTO.getRoles().contains(needRole)){
                    request.setAttribute("user",apiUserDetailDTO);
                    return true;
                }
            }
        }


        String token = getToken(request, "Authorization");
        if(token==null | !tokenProvider.validateToken(token)){
            throw new AuthorizationException("未授权！");
        }

        UserDetailDTO userDetailDTO = tokenProvider.getAuthentication(token);
        for(Role needRole : needsRoles){
            Set<Role> roles = userDetailDTO.getRoles();
            Set<String> roleStr = ServiceUtil.fetchProperty(roles, Role::getEnName);
            if(roleStr.contains(needRole.getEnName())) {
                request.setAttribute("user",userDetailDTO);
                return true;
            }
        }


        String authorities = needRoleStr.stream()
                .collect(Collectors.joining(" | "));
        throw new AuthorizationException("权限不足，["+uri+"]请求角色："+authorities);
    }

    public static  String getToken(HttpServletRequest request,String tokenName){
        String bearerToken = request.getHeader(tokenName);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
