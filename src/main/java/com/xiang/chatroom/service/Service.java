package com.xiang.chatroom.service;

import com.xiang.chatroom.dao.Dialogue;
import com.xiang.chatroom.dao.ResponseBean;
import com.xiang.chatroom.dao.DataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

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

            boolean f = dataBase.queryByName(user);
            if (f) {
                repponse.setCode(1);
                repponse.setMessage("登录成功");
                return repponse;
            } else {
                boolean b = dataBase.insert(user, password);
                if (!b) {
                    repponse.setCode(2);
                    repponse.setMessage("注册成功");
                } else {
                    repponse.setMessage("注册失败");
                }

            }


        }


        return repponse;
    }

    public List<Dialogue> receord(String send_user, String rece_user) throws SQLException {

       return dataBase.queryDiaByUser(send_user,rece_user);
    }
}
