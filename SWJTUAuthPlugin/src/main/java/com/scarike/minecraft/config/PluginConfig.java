package com.scarike.minecraft.config;

public class PluginConfig {
    public Tips tips=new Tips();
    public JDBC jdbc=new JDBC();
    public static class Tips{
        public String no_confirm;
    }
    public static class JDBC{
        public String url;
        public String username;
        public String password;
    }
}
