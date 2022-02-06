package com.scarike.minecraft.command;

import com.scarike.minecraft.entity.Mark;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class MoveCommand implements CommandExecutor {

    private static final String CMD="/mv";

    private final Server server= Bukkit.getServer();

    private final Map<String, Mark> playerMarkMap;

    public MoveCommand(Map<String, Mark> playerMarkMap) {
        this.playerMarkMap = playerMarkMap;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if(command.getName().equalsIgnoreCase(CMD)){
                Player player= (Player) sender;
                switch (args.length){
                    case 0:
                        Mark mark = playerMarkMap.get(player.getName());
                        if(mark==null){
                            player.sendMessage("您没有设置任何标记点");
                        } else {
                            player.teleport(mark.getLocation());
                            player.sendMessage("已传送至=> "+mark.toString());
                        }
                        return true;
                    case 1:
                        return handleOneParam(player,args[0]);
                    case 2:
                        return handleTwoParam(args[0],args[1]);
                    case 3:
                        return handleThreeParam(player,args[0],args[1],args[2]);
                    default:return false;
                }
            }
        }else{
            sender.sendMessage("Only Players can use this command");
            return true;
        }
        return false;
    }

    private boolean handleOneParam(Player player,String name){
        Player target = server.getPlayer(name);
        if(target==null){
            return false;
        }
        if(target==player){
            return true;
        }
        player.teleport(target);
        player.sendMessage("已传送至=> "+target.getName());
        return true;
    }

    private boolean handleTwoParam(String name1,String name2){
        Player player = server.getPlayer(name1);
        Player target = server.getPlayer(name2);
        if(target==null||player==null){
            return false;
        }
        if(target==player){
            return true;
        }
        player.teleport(target);
        player.sendMessage("已将"+name1+"传送至=> "+name2);
        return true;
    }

    private boolean handleThreeParam(Player player,String _x,String _y,String _z){
        int x,y,z;
        try{
            x=Integer.parseInt(_x);
            y=Integer.parseInt(_y);
            z=Integer.parseInt(_z);
        }catch (Exception e){
            return false;
        }
        Location target=player.getLocation();
        target.setX(x);target.setY(y);target.setZ(z);
        player.teleport(target);
        player.sendMessage("已传送至=> Location:[x="+x+",y="+y+",z="+z+"]");
        return true;
    }
}
