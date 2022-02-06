package com.scarike.minecraft.listener;

import com.scarike.minecraft.config.PluginConfig;
import com.scarike.minecraft.dao.PlayerDao;
import com.scarike.minecraft.dao.PlayerDaoImpl;
import com.scarike.minecraft.dao.PlayerDaoImpl2;
import com.scarike.minecraft.util.LoginStatus;
import com.scarike.minecraft.util.SQLUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.logging.Logger;

public class PlayerLoginListener implements Listener {

    private final PlayerDao dao;
    private final PluginConfig CONF;
    private final Logger log=Logger.getLogger("minecraft");

    public PlayerLoginListener(PluginConfig conf) {
        this.dao = new PlayerDaoImpl2(conf);
        CONF = conf;
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        String name = e.getName();
        log.info(name+"尝试登录");
        LoginStatus check = dao.check(name);
        switch (check) {
            case FAIL: {
                log.warning("[SWJTUAuthPlugin] 玩家"+name+"登录时遇到未知异常");
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "未知异常导致不能进入服务器，请联系腐竹debug");
                break;
            }
            case NO_CONFIRMED: {
                log.warning("[SWJTUAuthPlugin] 玩家"+name+"由于未认证被拦截");
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, CONF.tips.no_confirm);
                break;
            }
            case UNKNOWN_USER:{
                log.info("[SWJTUAuthPlugin] 玩家"+name+"是未知用户，放行");
                e.allow();
                break;
            }
            case SUCCESS_LOGIN: {
                log.info("[SWJTUAuthPlugin] 玩家"+name+"成功登录");
                e.allow();
                break;
            }
        }
    }
}
