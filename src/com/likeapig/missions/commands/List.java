package com.likeapig.missions.commands;

import org.bukkit.entity.*;
import com.likeapig.missions.map.*;
import com.likeapig.missions.map.Map;

import net.md_5.bungee.api.*;
import java.util.*;

public class List extends Commands {
	public List() {
		super("elimination.default", "List all maps", "", new String[] { "" });
	}

	@Override
	public void onCommand(final Player sender, final String[] args) {
		if (MapManager.get().getMap("test") != null) {
			MessageManager.get().message(sender, "Map list: ");
			Map m = MapManager.get().getMap("test");
			MessageManager.get().message(sender, ChatColor.YELLOW + m.getName());
		} else {
			MessageManager.get().message(sender, "No maps found", MessageManager.MessageType.BAD);
		}
	}
}
