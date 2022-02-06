package com.scarike.minecraft.command;

import com.scarike.minecraft.entity.Mark;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class MarkCommand implements CommandExecutor {

    private static final String CMD="/mk";

    private final Map<String, Mark> playerMarkMap;

    public MarkCommand(Map<String, Mark> playerMarkMap) {
        this.playerMarkMap = playerMarkMap;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if(command.getName().equalsIgnoreCase(CMD)) {
                if(args.length<1){
                    Mark mark = new Mark(((Player) sender).getLocation(), "A Mark");
                    playerMarkMap.put(sender.getName(),mark);
                    sender.sendMessage("已成功设置标记点=> "+mark);
                    return true;
                }else if(args[0].equalsIgnoreCase("-i")){
                    Mark mark = playerMarkMap.get(sender.getName());
                    if(mark==null){
                        sender.sendMessage("您没有设置任何标记点");
                    }else sender.sendMessage(mark.toString());
                    return true;
                }else if(args[0].equalsIgnoreCase("-m")){
                    if(args.length<2)
                        return false;
                    String message=args[1];
                    Mark mark = new Mark(((Player) sender).getLocation(), message);
                    playerMarkMap.put(sender.getName(),mark);
                    sender.sendMessage("已成功设置标记点=> "+mark);
                    return true;
                }else return false;
            }else return false;
        }else{
            sender.sendMessage("Only Players can use this command!");
        }
        return false;
    }
}
