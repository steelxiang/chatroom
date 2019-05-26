package com.xiang.chatroom.dao;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiang
 * @date 2019/5/25
 */

public class ResponseBean <T> implements Serializable {
    private Integer code;//0 失败 1 登录成功 2 注册成功
    private String message;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
