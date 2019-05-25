package com.xiang.chatroom.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiang
 * @date 2019/5/25
 */
@RestController
public class HomeController {
    @RequestMapping("/test")
    public String greeting() {

        return "index";
    }
}
