package com.scarike.minecraft.entity;

import org.bukkit.Location;

import java.io.Serializable;

public class Mark {
    private Location location;
    private String message;

    public Mark(Location location, String message) {
        this.location = location;
        this.message = message;
    }

    public Location getLocation() {
        return location;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message+":[" +
                "x="+location.getBlockX()+
                ",y="+location.getBlockY()+
                ",z="+location.getBlockZ()+
                ",yaw="+location.getYaw()+
                ",pitch="+location.getPitch()+
                ",world="+location.getWorld().getName()+
                "]";
    }
}
