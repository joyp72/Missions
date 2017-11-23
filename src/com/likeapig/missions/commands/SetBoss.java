package com.likeapig.missions.commands;

import org.bukkit.entity.*;

import com.likeapig.missions.commands.MessageManager.MessageType;
import com.likeapig.missions.map.Map;
import com.likeapig.missions.map.MapManager;

public class SetBoss extends Commands {
	public SetBoss() {
		super("raid.admin", "Set the Edit of a map", "<map>", new String[] { "edt" });
	}

	@Override
	public void onCommand(final Player sender, final String[] args) {
		if (args.length == 0) {
			MessageManager.get().message(sender, "You must specify a map!", MessageManager.MessageType.BAD);
			return;
		}
		if (args.length == 1) {
			MessageManager.get().message(sender, "You must specify an int", MessageType.BAD);
		}
		final String id = args[0];
		final Map m = MapManager.get().getMap(id);
		if (m == null) {
			MessageManager.get().message(sender, "Unknown map.", MessageManager.MessageType.BAD);
			return;
		}
		if (args[1].equalsIgnoreCase("1")) {
			m.setBossLoc(1, sender.getLocation());
			MessageManager.get().message(sender, "Boss Loc 1 set for: " + m.getName(), MessageManager.MessageType.GOOD);
			return;
		}
		if (args[1].equalsIgnoreCase("2")) {
			m.setBossLoc(2, sender.getLocation());
			MessageManager.get().message(sender, "Boss Loc 2 set for: " + m.getName(), MessageManager.MessageType.GOOD);
			return;
		}
	}
}
