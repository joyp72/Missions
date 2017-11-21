package com.likeapig.missions.commands;

import org.bukkit.entity.*;

import com.likeapig.missions.commands.MessageManager.MessageType;
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
		if (MapManager.get().getMaps().size() > 0) {
			MessageManager.get().message(sender, "Map list: ");
			for (Map m : MapManager.get().getMaps()) {
				MessageManager.get().message(sender,
						new StringBuilder().append(ChatColor.YELLOW).append(m.getName()).toString());
			}
		} else {
			MessageManager.get().message(sender, "No maps found", MessageType.BAD);
}
	}
}
