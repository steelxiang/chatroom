package com.xiang.chatroom.dao;

import java.util.PrimitiveIterator;

/**
 * @author xiang
 * @date 2019/5/26
 */
public class Dialogue {
    private String sendUser;
    private String receUser;
    private String text;
    private String time;

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public String getReceUser() {
        return receUser;
    }

    public void setReceUser(String receUser) {
        this.receUser = receUser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
