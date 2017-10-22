package com.likeapig.missions.map;

import com.likeapig.missions.*;
import java.util.*;
import org.bukkit.entity.*;

public class MapManager
{
    public static MapManager instance;
    private List<Map> maps;
    
    static {
        MapManager.instance = new MapManager();
    }
    
    public static MapManager get() {
        return MapManager.instance;
    }
    
    private MapManager() {
        this.maps = new ArrayList<Map>();
    }
    
    public void registerMap(final String s) {
        if (this.getMap(s) == null) {
            final Map m = new Map(s);
            this.maps.add(m);
        }
    }
    
    public void setupMaps() {
        this.maps.clear();
        if (Settings.get().get("maps") != null) {
            for (final String s : Settings.get().getConfigSection().getKeys(false)) {
                try {
                    this.registerMap(s);
                }
                catch (Exception ex) {
                    Main.get().getLogger().info("Exception ocurred when loading map: " + s);
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public Map getMap(final Player p) {
        for (final Map m : this.maps) {
            if (m.containsPlayer(p)) {
                return m;
            }
        }
        return null;
    }
    
    public Map getMap(final String name) {
        for (final Map m : this.maps) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }
    
    public void removeMap(final Map m) {
        if (this.maps.contains(m)) {
            this.maps.remove(m);
            m.onRemoved();
        }
    }
    
    public List<Map> getMaps() {
        return this.maps;
    }
}
