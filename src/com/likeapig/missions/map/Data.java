package com.likeapig.missions.map;

import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class Data
{
    private UUID id;
    private Location location;
    private Map map;
    
    protected Data(final Player p, final Map m) {
        this.id = p.getUniqueId();
        this.location = p.getLocation();
        this.map = m;
    }
    
    protected void restore() {
        final Player p = Bukkit.getPlayer(this.id);
        p.teleport(this.location);
    }
    
    public Map getMap() {
        return this.map;
    }
    
    public Player getPlayer() {
        return Bukkit.getPlayer(this.id);
    }
    
    public Location getLocation() {
        return this.location;
    }
}
