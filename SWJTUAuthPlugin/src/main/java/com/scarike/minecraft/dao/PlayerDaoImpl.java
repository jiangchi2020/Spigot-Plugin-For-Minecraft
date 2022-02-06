package com.scarike.minecraft.dao;

import com.scarike.minecraft.util.LoginStatus;
import com.scarike.minecraft.util.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDaoImpl implements PlayerDao{
    public static final String CHECK_SQL="SELECT `is_swjtuer` FROM `users` WHERE `username`=?;";
    private final SQLUtil sqlUtil;
    public PlayerDaoImpl(SQLUtil sqlUtil) {
        this.sqlUtil = sqlUtil;
    }

    @Override
    public LoginStatus check(String username) {
        LoginStatus res=LoginStatus.FAIL;
        Connection conn = sqlUtil.getConn();
        PreparedStatement sql=null;
        ResultSet rs=null;
        try {
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
            sqlUtil.returnConn(conn);
        }
        return res;
    }
}
