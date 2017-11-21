package com.likeapig.missions.map;

import com.likeapig.missions.*;

import net.citizensnpcs.api.npc.NPC;

import java.util.*;

import org.bukkit.entity.Player;

public class MapManager {
	public static MapManager instance;
	public List<Map> maps;
	public List<Player> editors;
	public HashMap<Map, Integer> edit;

	static {
		MapManager.instance = new MapManager();
	}

	public static MapManager get() {
		return MapManager.instance;
	}

	private MapManager() {
		maps = new ArrayList<Map>();
		editors = new ArrayList<Player>();
		edit = new HashMap<Map, Integer>();
	}
	
	public HashMap<Map, Integer> getEdit() {
		return edit;
	}
	
	public List<Player> getEditors() {
		return editors;
	}

	public void registerMap(String s) {
		if (getMap(s) == null) {
			Map m = new Map(s);
			maps.add(m);
		}
	}

	public void setupMaps() {
		maps.clear();
		if (Settings.get().get("maps") != null) {
			for (String s : Settings.get().getConfigSection().getKeys(false)) {
				try {
					registerMap(s);
				} catch (Exception ex) {
					Main.get().getLogger().info("Exception ocurred when loading map: " + s);
					ex.printStackTrace();
				}
			}
		}
	}

	public void removeMap(Map m) {
		if (maps.contains(m)) {
			maps.remove(m);
			m.onRemoved();
		}
	}

	public Map getMap(String name) {
		for (Map m : maps) {
			if (m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		return null;
	}

	public Map getMap(Player p) {
		for (Map m : maps) {
			if (m.containsPlayer(p)) {
				return m;
			}
		}
		return null;
	}
	
	public Map getMap(NPC npc) {
		for (Map m : maps) {
			if (m.containsNPC(npc)) {
				return m;
			}
		}
		return null;
	}

	public List<Map> getMaps() {
		return maps;
	}

}
