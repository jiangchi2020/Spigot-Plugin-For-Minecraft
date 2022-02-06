package com.scarike.minecraft.command;

import com.scarike.minecraft.DreamFlyPlugin;
import com.scarike.minecraft.config.PluginConfig;
import com.scarike.minecraft.event.PlayerDreamWakeEvent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class WakeupCommand implements CommandExecutor {
    private static final String CMD = "wakeup";
    private final Set<UUID> dreamPlayers;
    private final PluginConfig CONF;

    public WakeupCommand(Set<UUID> dreamPlayers, DreamFlyPlugin plugin) {
        this.dreamPlayers = dreamPlayers;
        this.CONF = plugin.getConf();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase(CMD)) {
                Player player = (Player) sender;
                if (CONF.wake_condition.cmd_force) {
                    if (dreamPlayers.contains(player.getUniqueId())) {
                        Bukkit.getPluginManager().callEvent(new PlayerDreamWakeEvent(player));
                    }else {
                        sender.sendMessage("您并没有在梦游");
                    }
                }else {
                    sender.sendMessage("不允许使用命令行强制唤醒");
                }
            }
        } else {
            sender.sendMessage("Only Players can use this command");
        }
        return true;
    }
}
