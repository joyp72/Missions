package com.likeapig.missions.commands;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import com.likeapig.missions.commands.MessageManager.MessageType;
import com.likeapig.missions.map.Map;
import com.likeapig.missions.map.MapManager;

public class Edit extends Commands {
	public Edit() {
		super("raid.admin", "Set the buttons of a map", "<map>", new String[] { "edit" });
	}

	private HashMap<Map, Integer> edit = MapManager.get().getEdit();
	private List<Player> editors = MapManager.get().getEditors();

	@Override
	public void onCommand(final Player sender, final String[] args) {
		if (args.length == 0) {
			MessageManager.get().message(sender, "You must specify a map!", MessageManager.MessageType.BAD);
			return;
		}
		final String id = args[0];
		final Map m = MapManager.get().getMap(id);
		if (m == null) {
			MessageManager.get().message(sender, "Unknown map.", MessageManager.MessageType.BAD);
			return;
		}
		if (editors.contains(sender)) {
			editors.remove(sender);
			MessageManager.get().message(sender, "You have quit editing.");
			return;
		}
		if (args[1].equalsIgnoreCase("21")) {
			editors.add(sender);
			edit.put(m, 21);
			MessageManager.get().message(sender, "Editing button 2, floor 1 for: " + m.getName());
			return;
		}
		if (args[1].equalsIgnoreCase("22")) {
			editors.add(sender);
			edit.put(m, 23);
			MessageManager.get().message(sender, "Editing button 2, floor 3 for: " + m.getName());
			return;
		}
		if (args[1].equalsIgnoreCase("11")) {
			editors.add(sender);
			edit.put(m, 11);
			MessageManager.get().message(sender, "Editing button 1, floor 2 for: " + m.getName());
			return;
		} else {
			MessageManager.get().message(sender, "Bad Input", MessageType.BAD);
			return;
		}
	}
}
