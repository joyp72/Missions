package com.likeapig.missions;

import org.bukkit.plugin.java.*;

import com.likeapig.missions.commands.CommandsManager;
import com.likeapig.missions.map.MapManager;
import com.likeapig.missions.map.Mission;
import com.likeapig.missions.map.RaidListener;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

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
         CitizensAPI.getTraitFactory().deregisterTrait(TraitInfo.create(Mission.class));
         CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(Mission.class));
    }
    
    
    public void onDisable() {
        this.getLogger().info("Disabled!");
    }
}
