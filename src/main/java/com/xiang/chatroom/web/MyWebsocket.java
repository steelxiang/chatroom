package com.xiang.chatroom.web;

/**
 * @author xiang
 * @date 2019/5/25
 */

import com.alibaba.fastjson.JSONObject;
import com.xiang.chatroom.dao.DataBase;
import com.xiang.chatroom.service.Service;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
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

    @Autowired
    private DataBase dataBase;

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        String name = null;
        try {
            name = session.getRequestParameterMap().get("name").get(0);
            boolean b = dataBase.queryByName(name);
            MyWebsocket myWebsocket = webSocketMap.get(name);
            if(b||null!=myWebsocket){
                sendMessage("此用户名已被使用");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return;
        }
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
        subOnlineCount();
        refreshList();
        //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());

    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session)  {

        JSONObject jsonObject = JSONObject.parseObject(message);
         if(null==jsonObject){
             return;
         }
        String sendUser = jsonObject.getString("send_user");
        String receUser = jsonObject.getString("rece_user");
        String mes = jsonObject.getString("message");
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        try {
            if(dataBase.queryByName(sendUser)){
                dataBase.saveDia(sendUser,receUser,mes,timestamp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        MyWebsocket item=webSocketMap.get(sendUser);
        if(null!=item){

            try {
                item.sendMessage(mes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
        String str = null;
        try {
            for(String s :set){
                sb=sb.append(s).append(",");
            }
            str = sb.substring(0, sb.length() - 1).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebsocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebsocket.onlineCount--;
        if(MyWebsocket.onlineCount<1){
            MyWebsocket.onlineCount=0;
        }
    }
}

