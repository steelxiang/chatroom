package com.xiang.chatroom.web;

import com.xiang.chatroom.dao.Dialogue;
import com.xiang.chatroom.dao.ResponseBean;
import com.xiang.chatroom.service.Service;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

/**
 * @author xiang
 * @date 2019/5/25
 */
@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST}, origins = "*")
public class HomeController {

    @Autowired
    private Service service;


    @GetMapping ("/login")
    @ApiOperation(value = "登录接口", notes = "集合登录与注册")
    public ResponseBean login(@RequestParam(value = "user") String user,@RequestParam(value = "password") String password) {


        ResponseBean repponse = new ResponseBean();

       // String user = request.getParameter("user");
      //  String password = request.getParameter("password");

        try {
            repponse = service.login(user, password);
        } catch (Exception e) {
            e.printStackTrace();
            repponse.setCode(0);
            repponse.setMessage("内部错误");
        }

        return repponse;
    }

    @GetMapping("/getReceord")
    @ApiOperation(value = "查看聊天记录", notes = "返回所有记录")
    public List<Dialogue> receord(@RequestParam("send_user") String send_user,@RequestParam("rece_user") String rece_user) {
       // String send_user = request.getParameter("send_user");
       // String rece_user = request.getParameter("rece_user");
        List<Dialogue> list = null;
        try {
            list = service.receord(send_user, rece_user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
