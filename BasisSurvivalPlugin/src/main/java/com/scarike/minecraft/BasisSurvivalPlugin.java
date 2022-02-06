package com.scarike.minecraft;

import com.scarike.minecraft.command.ClearCommand;
import com.scarike.minecraft.command.MarkCommand;
import com.scarike.minecraft.command.MoveCommand;
import com.scarike.minecraft.entity.Mark;
import com.scarike.minecraft.entity.SerializableMark;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class BasisSurvivalPlugin extends JavaPlugin {

    private Map<String, Mark> playerMarkMap;

    private final Logger logger= Bukkit.getLogger();

    public static final String DATA_FILE_NAME="player-mark.data";

    @Override
    public void onEnable() {
        File dataFile=new File(DATA_FILE_NAME);
        if(!(dataFile.exists()&&dataFile.isFile())){
            playerMarkMap=new HashMap<>();
        }else{
            try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(dataFile))){
                SerializableMark[] marks = (SerializableMark[]) ois.readObject();
                playerMarkMap=SerializableMark.deserialize(marks);
            } catch (IOException | ClassNotFoundException e) {
                logger.warning("插件加载失败");
                e.printStackTrace();
            }
        }
        // Plugin startup logic
        this.getCommand("/mv").setExecutor(new MoveCommand(playerMarkMap));
        this.getCommand("/mk").setExecutor(new MarkCommand(playerMarkMap));
        this.getCommand("/clear").setExecutor(new ClearCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SerializableMark[] marks = SerializableMark.serialize(playerMarkMap);
        try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(DATA_FILE_NAME))){
            oos.writeObject(marks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
