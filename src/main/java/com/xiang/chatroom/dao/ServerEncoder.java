package com.xiang.chatroom.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * @author xiang
 * @date 2019/5/26
 */
public class ServerEncoder implements Encoder.Text<ResponseBean>{

    @Override
    public String encode(ResponseBean responseBean) throws EncodeException {

        String s = JSON.toJSONString(responseBean);


        return s;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
