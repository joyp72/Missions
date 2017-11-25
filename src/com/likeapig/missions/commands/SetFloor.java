package com.likeapig.missions.commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.likeapig.missions.map.Map;
import com.likeapig.missions.map.MapManager;

public class SetFloor extends Commands {
	public SetFloor() {
		super("raid.admin", "Set the floor of a map", "<map>", new String[] { "sf" });
	}

	@Override
	public void onCommand(final Player sender, final String[] args) {
		final Location l = sender.getLocation();
		if (args.length == 0) {
			MessageManager.get().message(sender, "You must specify a map!", MessageManager.MessageType.BAD);
			return;
		}
		if (args.length == 1) {
			MessageManager.get().message(sender, "You must specify a numder!", MessageManager.MessageType.BAD);
			return;
		}
		final String id = args[0];
		final Map m = MapManager.get().getMap(id);
		if (m == null) {
			MessageManager.get().message(sender, "Unknown map.", MessageManager.MessageType.BAD);
			return;
		}
		if (args[1].equalsIgnoreCase("2")) {
			m.setFloor(2, l);
			MessageManager.get().message(sender, "Floor 2 set for: " + m.getName(), MessageManager.MessageType.GOOD);
			return;
		}
		if (args[1].equalsIgnoreCase("3")) {
			m.setFloor(3, l);
			MessageManager.get().message(sender, "Floor 3 set for: " + m.getName(), MessageManager.MessageType.GOOD);
			return;
		}
		if (args[1].equalsIgnoreCase("1")) {
			m.setFloor(1, l);
			MessageManager.get().message(sender, "Floor 1 set for: " + m.getName(), MessageManager.MessageType.GOOD);
			return;
		}
	}
}
