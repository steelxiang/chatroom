package com.xiang.chatroom.service;

import com.xiang.chatroom.dao.Dialogue;
import com.xiang.chatroom.dao.ResponseBean;
import com.xiang.chatroom.dao.DataBase;
import com.xiang.chatroom.web.MyWebsocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

/**
 * @author xiang
 * @date 2019/5/25
 */
@org.springframework.stereotype.Service
public class Service {
    @Autowired
    private DataBase dataBase;

    public ResponseBean login(String user, String password) throws SQLException {


        ResponseBean repponse = new ResponseBean();
        repponse.setCode(0);
        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(password)) {

            repponse.setMessage("用户名或密码不能为空");
            return repponse;
        } else {

            boolean b = dataBase.queryPassword(user, password);
            if (b) {
                repponse.setCode(1);
                repponse.setMessage("登录成功");
            } else {
                repponse.setMessage("登录失败");
            }

        }


        return repponse;
    }

    public List<Dialogue> receord(String send_user, String rece_user) throws SQLException {

        return dataBase.queryDiaByUser(send_user, rece_user);
    }

    public ResponseBean guestLogin(String name) {
        ResponseBean responseBean = new ResponseBean();
        MyWebsocket myWebsocket = MyWebsocket.webSocketMap.get(name);
        boolean b = false;
        try {
            b = dataBase.queryByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (b || null != myWebsocket) {
            responseBean.setCode(0);
            responseBean.setData("用户名已存在");
        } else {
            responseBean.setCode(1);
            responseBean.setData("用户名可以使用");
        }
        return responseBean;
    }

    public ResponseBean regester(String user, String password) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(1);
        try {

            boolean b = dataBase.queryByName(user);
            if (b) {
                responseBean.setMessage("该用户名已被注册");
                responseBean.setCode(2);
            } else {


                boolean insert = dataBase.insert(user, password);
                if (insert) {
                    responseBean.setMessage("注册成功");
                } else {
                    responseBean.setMessage("注册失败");
                    responseBean.setCode(0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responseBean;
    }


    public String saveImg(MultipartFile file,String name) {
        if (file.isEmpty()) {
            return null;
        }

        return null;
    }

}
