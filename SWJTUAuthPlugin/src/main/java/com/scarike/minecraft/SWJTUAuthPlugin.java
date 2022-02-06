package com.scarike.minecraft;

import com.scarike.minecraft.config.PluginConfig;
import com.scarike.minecraft.listener.PlayerLoginListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class SWJTUAuthPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginConfig CONF = readConf();
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(CONF),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public PluginConfig readConf(){
        PluginConfig CONF=new PluginConfig();

        saveDefaultConfig();

        CONF.tips.no_confirm=getConfig().getString("tips.no_confirm");
        CONF.jdbc.url=getConfig().getString("jdbc.url");
        CONF.jdbc.username=getConfig().getString("jdbc.username");
        CONF.jdbc.password=getConfig().getString("jdbc.password");

        return CONF;
    }
}
