package com.likeapig.missions.commands;

import org.bukkit.entity.Player;

import com.likeapig.missions.commands.MessageManager.MessageType;
import com.likeapig.missions.intro.Intro;
import com.likeapig.missions.intro.IntroManager;
import com.likeapig.missions.map.Map;
import com.likeapig.missions.map.MapManager;

public class Leave extends Commands {
	public Leave() {
		super("raid.default", "Leave a map", "", new String[] { "l" });
	}

	@Override
	public void onCommand(final Player sender, final String[] args) {
		final Map m = MapManager.get().getMap(sender);
		Intro i = IntroManager.get().getIntro(sender);
		if (m == null && i == null) {
			MessageManager.get().message(sender, "You are not in a map or intro.", MessageType.BAD);
			return;
		}
		if (m != null) {
			m.kickPlayer(sender);
			return;
		}
		if (i != null) {
			i.removePlayer(sender);
			return;
		}
	}
}
