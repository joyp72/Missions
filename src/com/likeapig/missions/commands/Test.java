package com.likeapig.missions.commands;

import org.bukkit.entity.Player;

import com.likeapig.missions.map.Map;
import com.likeapig.missions.map.MapManager;
import com.likeapig.missions.map.Mob;

public class Test extends Commands {
	public Test() {
		super("raid.admin", "Test", "", new String[] { "t" });
	}

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;
		Mob.get().Round2Boss(p.getLocation());
	}
}
