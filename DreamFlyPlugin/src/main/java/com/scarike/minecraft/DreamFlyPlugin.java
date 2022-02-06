package com.scarike.minecraft;

import com.scarike.minecraft.command.WakeupCommand;
import com.scarike.minecraft.config.PluginConfig;
import com.scarike.minecraft.exception.IllegalConfigException;
import com.scarike.minecraft.listen.PlayerDreamListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

/**
 * 功能实现：玩家上床睡觉后(PlayerBedEnterEvent)，超过了一定的时间(可配置)，即进入到梦游状态(玩家进入旁观者模式)
 * 当触发以下情况时，自动醒来(tp到床上并回到生存模式)：
 * 1. 飞出超过20个区块——PlayerMoveEvent
 * 2. 飞入坐标-64以下——PlayerMoveEvent
 * 3. 飞入地狱或末地传送门——PlayerChangeWorldEvent
 * 4. 超过最长时间
 */
public final class DreamFlyPlugin extends JavaPlugin {

    private Set<UUID> dreamPlayers;
    private PluginConfig CONF;
    private final Logger log=Logger.getLogger("minecraft");

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        CONF = readConf();
        log.info("读取配置：\n" + CONF);

        dreamPlayers=new HashSet<>();

        getServer().getPluginManager().registerEvents(new PlayerDreamListener(this), this);
        getCommand("wakeup").setExecutor(new WakeupCommand(dreamPlayers,this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (UUID uuid : dreamPlayers) {
            Player p = Bukkit.getPlayer(uuid);
            if (p!=null) {
                p.setGameMode(GameMode.SURVIVAL);
                p.teleport(getSpawnLocation(p));
            }
        }
        dreamPlayers.clear();
    }

    public Location getSpawnLocation(Player p) {
        Location spawn = p.getBedSpawnLocation();
        if (spawn == null) {
            spawn = CONF.working_world.getSpawnLocation();
        }
        return spawn;
    }

    private PluginConfig readConf() {
        PluginConfig conf = new PluginConfig();

        try {
            conf.working_world = Bukkit.getWorld(getConfig().getString("working_world"));
        } catch (Exception e) {
            throw new IllegalConfigException("未能成功获取到世界，请检查working_world配置项");
        }

        conf.ticks_to_dream = getConfig().getLong("ticks_to_dream");
        if(conf.ticks_to_dream<0){
            throw new IllegalConfigException("ticks_to_dream配置项不能小于0");
        }

        conf.interval_to_rand = getConfig().getLong("interval_to_rand");
        if(conf.interval_to_rand<0){
            throw new IllegalConfigException("interval_to_rand配置项不能小于0");
        }

        conf.max_dream_ticks = getConfig().getLong("max_dream_ticks");
        if(conf.max_dream_ticks<1){
            throw new IllegalConfigException("max_dream_ticks配置项不能小于1");
        }

        int probability_to_dream = getConfig().getInt("probability_to_dream");
        if (probability_to_dream < 1) {
            conf.probability_to_dream = 1;
        } else if (probability_to_dream > 100) {
            conf.probability_to_dream = 100;
        } else {
            conf.probability_to_dream = probability_to_dream;
        }

        conf.wake_condition.distance = getConfig().getInt("wake_condition.distance");
        conf.wake_condition.min_y = getConfig().getInt("wake_condition.min_y");
        conf.wake_condition.max_y = getConfig().getInt("wake_condition.max_y");
        conf.wake_condition.world_change = getConfig().getBoolean("wake_condition.world_change");
        conf.wake_condition.cmd_force = getConfig().getBoolean("wake_condition.cmd_force");

        conf.tips.player_in_dream.title = getConfig().getString("tips.player_in_dream.title");
        conf.tips.player_in_dream.subtitle = getConfig().getString("tips.player_in_dream.subtitle");
        conf.tips.player_wake.title = getConfig().getString("tips.player_wake.title");
        conf.tips.player_wake.subtitle = getConfig().getString("tips.player_wake.subtitle");

        conf.tips.progress_bar_title=getConfig().getString("tips.progress_bar_title");

        return conf;
    }

    public PluginConfig getConf() {
        return CONF;
    }

    public Set<UUID> getDreamPlayers() {
        return dreamPlayers;
    }

}
