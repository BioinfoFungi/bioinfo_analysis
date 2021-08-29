package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author wangyang
 * @date 2021/4/29
 */
@Controller
public class WebSocketController {
    @Autowired
    WebSocketServer springWebSocketHandler;



    @RequestMapping("/websocket/login")
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        System.out.println(username+"登录");
        HttpSession session = request.getSession(false);
        session.setAttribute("SESSION_USERNAME", username); //一般直接保存user实体
        return "/websocket/send";
    }

    @RequestMapping("/websocket/send")
    @ResponseBody
    public String send(HttpServletRequest request) {
        String username = request.getParameter("username");
//        springWebSocketHandler.sendMessageToUser(username, new TextMessage("你好，测试！！！！"));
        return null;
    }


    @RequestMapping("/websocket/broad")
    @ResponseBody
    public  String broad() {
//        springWebSocketHandler.sendMessageToUsers(new TextMessage("发送一条小Broad"));
        System.out.println("群发成功");
        return "broad";
    }
}
