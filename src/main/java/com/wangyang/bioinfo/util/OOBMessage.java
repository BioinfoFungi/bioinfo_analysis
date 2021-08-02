package com.wangyang.bioinfo.util;

import com.wangyang.bioinfo.handle.SpringWebSocketHandler;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.RList;
//import org.rosuda.REngine.Rserve.OOBInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

/**
 * @author wangyang
 * @date 2021/7/28
 */
public class OOBMessage {// implements OOBInterface {

    SpringWebSocketHandler springWebSocketHandler;


    public OOBMessage(SpringWebSocketHandler springWebSocketHandler){
        this.springWebSocketHandler=springWebSocketHandler;
    }

//    @Override
//    public void oobSend(int i, REXP message) {
//        try {
//            RList l = message.asList();
//            String src = l.at(0).asString();
//            // we only implement the following OOB masseges:
//            if (src.equals("console.out") ||
//                    src.equals("console.err") ||
//                    src.equals("console.msg") ||
//                    src.equals("stdout") ||
//                    src.equals("stderr")) {
//                String txt = l.at((l.size() > 2) ? 2 : 1).asString();
//                if (src.equals("console.out") || src.equals("stdout")){
//                    System.out.print(txt);
//                    springWebSocketHandler.sendMessageToUsers(new TextMessage(txt));
//                }else{
//                    System.err.print(txt);
//                    springWebSocketHandler.sendMessageToUsers(new TextMessage(txt));
//                }
//
//            }
//        } catch (REXPMismatchException ex) {
//            /* if we can't parse it, we ignore it */
//        }
//    }
//
//    @Override
//    public REXP oobMessage(int i, REXP rexp) {
//        /* we don't implement stdin for now, but it
//           would look something like the above */
//        return new REXPString("no!\n");
//    }
}
