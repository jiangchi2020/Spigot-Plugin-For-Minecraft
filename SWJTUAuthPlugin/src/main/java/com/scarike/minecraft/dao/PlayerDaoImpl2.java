package com.scarike.minecraft.dao;

import com.scarike.minecraft.config.PluginConfig;
import com.scarike.minecraft.util.LoginStatus;

import java.sql.*;

public class PlayerDaoImpl2 implements PlayerDao {
    public static final String CHECK_SQL="SELECT `is_swjtuer` FROM `users` WHERE `username`=?;";

    private final PluginConfig CONF;

    public PlayerDaoImpl2(PluginConfig CONF) {
        this.CONF=CONF;
    }

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LoginStatus check(String username) {
        LoginStatus res=LoginStatus.FAIL;
        Connection conn = null;
        PreparedStatement sql=null;
        ResultSet rs=null;
        try {
            conn= DriverManager.getConnection(CONF.jdbc.url,CONF.jdbc.username,CONF.jdbc.password);
            sql = conn.prepareStatement(CHECK_SQL);
            sql.setString(1,username);
            rs=sql.executeQuery();
            if(rs.next()){
                int is_confirm = rs.getInt(1);
                if (is_confirm == 1) {
                    res = LoginStatus.SUCCESS_LOGIN;
                } else {
                    res = LoginStatus.NO_CONFIRMED;
                }
            }else {
                res=LoginStatus.UNKNOWN_USER;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (sql != null) {
                try {
                    sql.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return res;
    }
}
