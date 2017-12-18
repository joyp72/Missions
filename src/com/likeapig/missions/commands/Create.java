package com.likeapig.missions.commands;

import org.bukkit.entity.*;

import com.likeapig.missions.map.MapManager;
import com.likeapig.missions.commands.MessageManager.MessageType;
import com.likeapig.missions.intro.Intro;
import com.likeapig.missions.intro.IntroManager;
import com.likeapig.missions.map.*;

public class Create extends Commands {
	public Create() {
		super("raid.admin", "Create a Map", "<name>", new String[] { "c", "createmap" });
	}

	@Override
	public void onCommand(final Player sender, final String[] args) {
		if (args.length == 0) {
			MessageManager.get().message(sender, "You must specify a map or intro name.",
					MessageManager.MessageType.BAD);
			return;
		}
		final String id = args[0];
		final Map m = MapManager.get().getMap(id);
		Intro i = IntroManager.get().getIntro(id);
		if (m != null && i != null) {
			MessageManager.get().message(sender, "That map or intro already exists.", MessageManager.MessageType.BAD);
			return;
		}
		if (id.equalsIgnoreCase("map")) {
			MapManager.get().registerMap(id);
			MessageManager.get().message(sender, "Map created!", MessageManager.MessageType.GOOD);
			return;
		}
		if (id.equalsIgnoreCase("intro")) {
			IntroManager.get().registerIntro(id);
			MessageManager.get().message(sender, "Intro created!", MessageType.GOOD);
			return;
		}
	}
}
