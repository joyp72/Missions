package com.likeapig.missions;

import org.bukkit.plugin.java.*;

import com.likeapig.missions.commands.CommandsManager;
import com.likeapig.missions.map.MapManager;
import com.likeapig.missions.map.RaidListener;

import org.bukkit.plugin.*;


public class Main extends JavaPlugin
{
    public static Main instance;
    
    public static Main get() {
        return Main.instance;
    }
    
    public void onEnable() {
        Main.instance = this;
        this.getLogger().info("Raid Enabled!");
        CommandsManager.get().setup();
        RaidListener.get().setup();
        Settings.get().setup((Plugin)this);
        MapManager.get().setupMaps();
    }
    
    public void onDisable() {
        this.getLogger().info("Disabled!");
    }
}
