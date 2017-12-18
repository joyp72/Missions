package com.likeapig.missions.commands;

import org.bukkit.entity.*;

import com.likeapig.missions.commands.MessageManager.MessageType;
import com.likeapig.missions.intro.Intro;
import com.likeapig.missions.intro.IntroManager;
import com.likeapig.missions.map.*;

public class SetSpawn extends Commands {
	public SetSpawn() {
		super("raid.admin", "Set the spawn of a map", "<map>", new String[] { "sal" });
	}

	@Override
	public void onCommand(final Player sender, final String[] args) {
		if (args.length == 0) {
			MessageManager.get().message(sender, "You must specify a map!", MessageManager.MessageType.BAD);
			return;
		}
		final String id = args[0];
		final Map m = MapManager.get().getMap(id);
		Intro i = IntroManager.get().getIntro(id);
		if (m == null && i == null) {
			MessageManager.get().message(sender, "Unknown map or intro.", MessageManager.MessageType.BAD);
			return;
		}
		if (m != null) {
			m.setSpawn(sender.getLocation());
			MessageManager.get().message(sender, "Alpha spawn set for: " + m.getName(),
					MessageManager.MessageType.GOOD);
			return;
		}
		if (i != null) {
			i.setSpawn(sender.getLocation());
			MessageManager.get().message(sender, "Spawn set for: " + i.getName(), MessageType.GOOD);
			return;
		}
	}
}
