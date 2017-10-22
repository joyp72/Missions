package com.likeapig.missions.map;

import org.bukkit.*;
import org.bukkit.ChatColor;

import java.util.*;
import net.md_5.bungee.api.*;
import org.bukkit.entity.*;

public class Mob
{
    public static Mob instance;
    Evoker ev;
    public List<LivingEntity> mobs;
    Location loc;
    
    static {
        Mob.instance = new Mob();
    }
    
    public static Mob get() {
        return Mob.instance;
    }
    
    public List<LivingEntity> getMobs() {
        return this.mobs;
    }
    
    private Mob() {
        this.mobs = new ArrayList<LivingEntity>();
    }
    
    public void spawnBoss(final Location loc) {
        final Location l = loc.clone();
        (this.ev = (Evoker)l.getWorld().spawn(loc, (Class)Evoker.class)).setGravity(true);
        this.ev.setCollidable(false);
        this.ev.setCustomNameVisible(true);
        this.ev.setGliding(false);
        this.ev.setCanPickupItems(false);
        this.ev.setAI(false);
        this.ev.setMaxHealth(1000.0);
        this.ev.setHealth(1000.0);
        this.ev.setCustomName(ChatColor.RED + "\u2764 " + (int)this.ev.getHealth());
        this.ev.setCurrentSpell(Evoker.Spell.NONE);
        this.mobs.add((LivingEntity)this.ev);
    }
    
    public void spawnRound1(final Location loc) {
        this.mobs.add((LivingEntity)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE));
        this.mobs.add((LivingEntity)loc.getWorld().spawnEntity(loc, EntityType.SKELETON));
    }
}
