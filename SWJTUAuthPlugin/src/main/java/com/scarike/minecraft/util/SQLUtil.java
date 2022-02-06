package com.scarike.minecraft.util;

import com.scarike.minecraft.config.PluginConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLUtil {
    private final Connection instance;
    private final PluginConfig CONF;
    private boolean use = false;

    public SQLUtil(PluginConfig CONF) {
        this.CONF = CONF;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            instance = DriverManager.getConnection(CONF.jdbc.url, CONF.jdbc.username, CONF.jdbc.password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未能找到驱动类", e);
        } catch (SQLException e) {
            throw new RuntimeException("链接到数据库失败，请检查配置信息并重启。", e);
        }
    }

    public Connection getConn() {
        if (!use) {
            use = true;
            return instance;
        }
        try {
            return DriverManager.getConnection(CONF.jdbc.url, CONF.jdbc.username, CONF.jdbc.password);
        } catch (SQLException e) {
            throw new RuntimeException("链接到数据库失败，请检查配置信息。", e);
        }
    }

    public void returnConn(Connection conn) {
        if (conn != instance) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            use = false;
        }
    }
}
