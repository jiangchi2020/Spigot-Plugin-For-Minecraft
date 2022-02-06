package com.scarike.minecraft.entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SerializableMark implements Serializable {

    private static final long serialVersionUID = 114514L;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private String world;
    private String message;
    private String name;
    public static SerializableMark[] serialize(Map<String,Mark> playerMarkMap){
        SerializableMark[] markList=new SerializableMark[playerMarkMap.size()];
        var ref=new Object(){
            int i=0;
        };
        playerMarkMap.forEach((player,mark)->{
            SerializableMark smk=new SerializableMark();
            smk.name=player;
            smk.message=mark.getMessage();
            smk.world=mark.getLocation().getWorld().getName();
            smk.x=mark.getLocation().getBlockX();
            smk.y=mark.getLocation().getBlockY();
            smk.z=mark.getLocation().getBlockZ();
            smk.pitch=mark.getLocation().getPitch();
            smk.yaw=mark.getLocation().getYaw();
            System.out.println("已成功序列化=> "+mark);
            markList[ref.i++]=smk;
        });
        return markList;
    }

    public static Map<String,Mark> deserialize(SerializableMark[] markList){
        Server server= Bukkit.getServer();
        Map<String,Mark> playerMarkMap=new HashMap<>();
        for (SerializableMark smk : markList) {
            Mark mark=new Mark(new Location(server.getWorld(smk.world),smk.x,smk.y,smk.z,smk.yaw,smk.pitch),smk.message);
            System.out.println("已成功反序列化=> "+mark);
            playerMarkMap.put(smk.name,mark);
        }
        return playerMarkMap;
    }

    @Override
    public String toString() {
        return "SerializableMark{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", pitch=" + pitch +
                ", yaw=" + yaw +
                ", world='" + world + '\'' +
                ", message='" + message + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
