package com.likeapig.missions.commands;

import org.bukkit.entity.Player;

import com.likeapig.missions.map.Map;
import com.likeapig.missions.map.MapManager;

public class Test extends Commands {
	public Test() {
		super("raid.admin", "Test", "", new String[] { "t" });
	}

	int i = 0;

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;
		Map m = MapManager.get().getMap(p);
		if (m != null) {
			p.sendMessage(m.getRS().getBlock().getType().toString());
		}
	}
}