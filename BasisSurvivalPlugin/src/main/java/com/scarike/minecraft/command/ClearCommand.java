package com.scarike.minecraft.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearCommand implements CommandExecutor {

    private static final String CMD="/clear";

    private final Server server= Bukkit.getServer();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if(command.getName().equalsIgnoreCase(CMD)) {
                Player player= (Player) sender;
                Location loc=player.getLocation();
                int x=loc.getBlockX();
                int y=loc.getBlockY();
                int z=loc.getBlockZ();
                StringBuilder sb=new StringBuilder("kill @e[x=");
                sb.append(x);sb.append(",y=");
                sb.append(y);sb.append(",z=");
                sb.append(z);sb.append(",distance=..10,type=item]");
                System.out.println(sb);
                server.dispatchCommand(server.getConsoleSender(), sb.toString());
            }else return false;
        }else{
            sender.sendMessage("Only Players can use this command!");
        }
        return true;
    }
}
