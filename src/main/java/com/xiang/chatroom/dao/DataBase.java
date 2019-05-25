package com.xiang.chatroom.dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author xiang
 * @date 2019/5/25
 */
public class DataBase {


    public static Connection getConn( ) {
        Connection conn=null;

        String host_port ="182.61.47.1:3306";
        String database = "chat";
        String username = "root";
        String password = "1357";
        String url = "jdbc:mysql://" + host_port + "/" + database + "?serverTimezone=GMT%2B8&useSSL=false";

        try {
            Class.forName("com.mysql.jdbc.Driver");
             conn = DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
