package com.likeapig.missions.map;

import java.util.*;
import org.bukkit.*;
import org.bukkit.ChatColor;

import com.likeapig.missions.*;
import org.bukkit.plugin.*;
import net.md_5.bungee.api.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;

public class RaidListener implements Listener
{
    private static RaidListener instance;
    private List<LivingEntity> mobs;
    
    static {
        RaidListener.instance = new RaidListener();
    }
    
    public RaidListener() {
        this.mobs = Mob.get().getMobs();
    }
    
    public static RaidListener get() {
        return RaidListener.instance;
    }
    
    public void setup() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Main.get());
    }
    
    @EventHandler
    public void onEntityDamangeEvent(final EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof LivingEntity) {
            final int health = (int)(((Damageable)e.getEntity()).getHealth() - e.getDamage());
            if (this.mobs.contains(e.getEntity())) {
                if (health > 0) {
                    e.getEntity().setCustomName(new StringBuilder().append(ChatColor.RED).append(health).append(" \u2764").toString());
                }
                else {
                    e.getEntity().setCustomName("");
                    e.getEntity().setCustomNameVisible(false);
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityDeathEvent(final EntityDeathEvent e) {
        if (e.getEntity() instanceof LivingEntity && this.mobs.contains(e.getEntity())) {
            this.mobs.remove(e.getEntity());
            if (e.getEntity().getKiller() instanceof Player) {
                final Player p = e.getEntity().getKiller();
                if (MapManager.get().getMap(p) != null) {
                    final Map m = MapManager.get().getMap(p);
                    if (this.mobs.size() <= 0) {
                        m.stop();
                    }
                }
            }
        }
    }
}
