package com.xiang.chatroom.dao;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiang
 * @date 2019/5/25
 */
@Component
public class DataBase {


    public Connection getConn() {
        Connection conn = null;

        String host_port = "182.61.47.1:3306";
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

    public boolean queryByName(String user) throws SQLException {

        Connection conn = getConn();
        Statement statement = conn.createStatement();
        String sql = "select * from user where name=\"" + user+"\"";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            return true;

        } else {
            return false;
        }

    }

    public boolean insert(String user, String password) throws SQLException {


        Connection conn = getConn();
        Statement statement = conn.createStatement();
        String sql = "insert into user (name,password) values (\"" + user + "\" , \"" + password + "\")";
        boolean execute = statement.execute(sql);

        conn.close();
        return execute;
    }

    public boolean saveDia(String sendUser, String receUser, String mes,Timestamp timestamp) throws SQLException {
        Connection conn = getConn();
        Statement statement = conn.createStatement();
        String sql="insert into dialogue (send_name,rece_name,text,create_time) values (\"" + sendUser + "\" ,\"" + receUser + "\",\""+mes+"\",\""+timestamp+"\")";
        boolean execute = statement.execute(sql);
        return execute;
    }

    public List<Dialogue> queryDiaByUser(String send_user, String rece_user) throws SQLException {
        ArrayList<Dialogue> list=new ArrayList<>();
        Connection conn = getConn();
        Statement statement = conn.createStatement();
        String sql="select * from dialogue where send_name=\""+send_user +"\" and rece_name=\""+rece_user +"\" order by create_time DESC ";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String text = resultSet.getString("text");
            Timestamp create_time = resultSet.getTimestamp("create_time");
            Dialogue dialogue=new Dialogue();
            dialogue.setReceUser(rece_user);
            dialogue.setSendUser(send_user);
            dialogue.setText(text);
            dialogue.setTime(timeFormat(create_time));
            list.add(dialogue);

        }


        return list;
    }

    private String timeFormat(Timestamp create_time) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");
        String format = sdf.format(create_time.getTime());
        return format;

    }
}
