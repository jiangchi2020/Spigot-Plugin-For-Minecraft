package com.scarike.minecraft.listen;

import com.scarike.minecraft.DreamFlyPlugin;
import com.scarike.minecraft.config.PluginConfig;
import com.scarike.minecraft.event.PlayerDreamEvent;
import com.scarike.minecraft.event.PlayerDreamWakeEvent;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class PlayerDreamListener implements Listener {

    private final DreamFlyPlugin plugin;
    private final Set<UUID> dreamPlayers;

    private final PluginConfig CONF;

    private final Logger log = Logger.getLogger("minecraft");

    public PlayerDreamListener(DreamFlyPlugin plugin) {
        this.plugin = plugin;
        this.dreamPlayers = plugin.getDreamPlayers();
        this.CONF = plugin.getConf();
    }

    @EventHandler
    public void onSleep(PlayerBedEnterEvent e) {
        if (!e.isCancelled() && e.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            //玩家上床睡觉，开始计时
            Player player = e.getPlayer();
            new BukkitRunnable() {

                private final Random rand = new Random(System.currentTimeMillis());

                @Override
                public void run() {
                    if (player.isOnline() && player.isSleeping()) {
                        if (rand.nextInt(100) < CONF.probability_to_dream) {
                            Bukkit.getServer().getPluginManager().callEvent(new PlayerDreamEvent(player));
                            this.cancel();
                        }
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, CONF.ticks_to_dream, CONF.interval_to_rand);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (CONF.working_world.equals(e.getPlayer().getWorld()) && dreamPlayers.contains(e.getPlayer().getUniqueId())) {
            //判断是否超出距离
            Location spawn = plugin.getSpawnLocation(e.getPlayer());
            if (!CONF.inRange(spawn, e.getTo())) {
                Player p = e.getPlayer();
                Bukkit.getPluginManager().callEvent(new PlayerDreamWakeEvent(p));
            }
        }
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        if (CONF.wake_condition.world_change && dreamPlayers.contains(e.getPlayer().getUniqueId())) {
            if (e.getFrom().equals(CONF.working_world)) {
                Player p = e.getPlayer();
                Bukkit.getPluginManager().callEvent(new PlayerDreamWakeEvent(p));
            }
        }
    }

    @EventHandler
    public void onDream(PlayerDreamEvent e) {
        Player player = e.getPlayer();
        if (player.isOnline() && player.isSleeping()) {
            log.info(player.getName() + "进入梦境");
            dreamPlayers.add(player.getUniqueId());
            //向玩家发送消息
            player.sendTitle(CONF.tips.player_in_dream.title, CONF.tips.player_in_dream.subtitle, 10, 70, 20);
            //唤醒玩家(玩家亦未寝.jpg)
            player.wakeup(true);
            //设置成旁观者模式
            player.setGameMode(GameMode.SPECTATOR);
            //启动监听等待唤醒
            BossBar bar = Bukkit.createBossBar(CONF.tips.progress_bar_title, BarColor.GREEN, BarStyle.SOLID);
            bar.addPlayer(player);
            new BukkitRunnable() {
                private long remaining_ticks = CONF.max_dream_ticks;

                @Override
                public void run() {
                    if (dreamPlayers.contains(player.getUniqueId())) {
                        remaining_ticks--;
                        if (remaining_ticks <= 0) {
                            Bukkit.getPluginManager().callEvent(new PlayerDreamWakeEvent(player));
                            return;
                        }
                        double progress = (double) remaining_ticks / CONF.max_dream_ticks;
                        bar.setProgress(progress);
                        if (progress <= 0.2) {
                            if (bar.getColor() != BarColor.RED) {
                                bar.setColor(BarColor.RED);
                            }
                        } else if (progress <= 0.5) {
                            if (bar.getColor() != BarColor.YELLOW) {
                                bar.setColor(BarColor.YELLOW);
                            }
                        }
                    } else {
                        bar.removeAll();
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 1, 1);
        }
    }

    @EventHandler
    public void onWake(PlayerDreamWakeEvent e) {
        Player p = e.getPlayer();
        log.info(p.getName() + "醒来");
        p.setGameMode(GameMode.SURVIVAL);
        p.teleport(plugin.getSpawnLocation(p));
        p.sendTitle(CONF.tips.player_wake.title, CONF.tips.player_wake.subtitle, 10, 70, 20);
        dreamPlayers.remove(p.getUniqueId());
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (dreamPlayers.contains(p.getUniqueId())) {
            p.setGameMode(GameMode.SURVIVAL);
            p.teleport(plugin.getSpawnLocation(p));
            dreamPlayers.remove(p.getUniqueId());
        }
    }
}
