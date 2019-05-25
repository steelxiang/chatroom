package com.xiang.chatroom.web;

/**
 * @author xiang
 * @date 2019/5/25
 */

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.xiang.chatroom.config.GetHttpSessionConfigurator;
import netscape.javascript.JSObject;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/websocket")
@Component
public class MyWebsocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //用来存放每个客户端对应的MyWebSocket对象。
    //使用Map来存放，其中Key可以为用户标识
    private static HashMap<String, MyWebsocket> webSocketMap =new HashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;

        String name = session.getRequestParameterMap().get("name").get(0);

        webSocketMap.put(name+"",this); //加入map中
        addOnlineCount();           //在线数加1
        //上线通知所有人
        sendMessageToAll(name+  "刚刚上线");


        //更新列表信息
        refreshList();


    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){

        webSocketMap.remove(this);//从map中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());

    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        JSONObject jsonObject = JSONObject.parseObject(message);

        String user = jsonObject.getString("user");
        String mes = jsonObject.getString("message");



        //
        MyWebsocket item=webSocketMap.get(user);
        item.sendMessage(mes);

    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    //给客户端传递消息
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);

    }

    public void sendMessageToAll(String message) {

        webSocketMap.forEach((k,v)->{

            try {
                v.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });


    }

    public void refreshList() {

       // Object[] objects = webSocketMap.values().toArray();
        Set<String> keySet = webSocketMap.keySet();
        String s = set2String(keySet);
        String mes="{\"count\":\""+webSocketMap.size()+"\",\"list\":\""+s+"\"}";
        webSocketMap.forEach((k,v)->{

            try {
                v.session.getBasicRemote().sendText(mes);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });


    }

    public String set2String(Set<String> set){
        StringBuilder sb=new StringBuilder();
     for(String s :set){
         sb=sb.append(s).append(",");
     }
        String s = sb.substring(0, sb.length() - 1).toString();
     return s;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebsocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebsocket.onlineCount--;
    }
}

