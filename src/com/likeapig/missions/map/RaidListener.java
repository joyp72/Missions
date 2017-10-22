package com.likeapig.missions.map;

import java.util.*;
import org.bukkit.*;
import org.bukkit.ChatColor;

import com.likeapig.missions.*;
import org.bukkit.plugin.*;

import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.*;
import org.bukkit.event.*;

public class RaidListener implements Listener
{
    private static RaidListener instance;
    private List<NPC> npcs;
    
    static {
        RaidListener.instance = new RaidListener();
    }
    
    public RaidListener() {
        this.npcs = Mob.get().getNpcs();
    }
    
    public static RaidListener get() {
        return RaidListener.instance;
    }
    
    public void setup() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Main.get());
    }
}
