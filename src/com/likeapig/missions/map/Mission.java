package com.likeapig.missions.map;

import net.citizensnpcs.api.trait.*;
import com.likeapig.missions.*;
import net.citizensnpcs.api.event.*;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.event.*;
import net.citizensnpcs.api.util.*;
import org.bukkit.entity.*;

public class Mission extends Trait
{
    Main plugin;
    boolean SomeSetting;
    
    public Mission() {
        super("Mission");
        this.plugin = null;
        this.SomeSetting = false;
        this.plugin = Main.get();
    }
    
    public void load(final DataKey key) {
        this.SomeSetting = key.getBoolean("SomeSetting", this.plugin.getConfig().getBoolean("Defaults.SomeSetting"));
    }
    
    public void save(final DataKey key) {
        key.setBoolean("SomeSetting", this.SomeSetting);
    }
    
    @EventHandler
    public void click(final NPCClickEvent event) {
    }
    
    public void onAttach() {
        this.load((DataKey)new MemoryDataKey());
        
        npc.data().set(NPC.GLOWING_METADATA, true);
        
    }
    
    public void onDespawn() {
    }
    
    public void onSpawn() {
       // this.npc.getNavigator().setTarget((Entity)Map.getPlayer(), true);
        this.getLivingEntity().setMaxHealth(50.0);
        this.getLivingEntity().setHealth(50.0);
    }
    
    public void onRemove() {
    }
    
    public LivingEntity getLivingEntity() {
        return (LivingEntity)this.npc.getEntity();
    }
}
