package com.likeapig.missions.commands;

import org.bukkit.entity.Player;

import com.likeapig.missions.map.Map;
import com.likeapig.missions.map.MapManager;
import com.likeapig.missions.map.Mob;
import com.likeapig.missions.models.LawnMower;

public class Test extends Commands {
	public Test() {
		super("raid.admin", "Test", "", new String[] { "t" });
	}
	
	int i = 0;

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;

		if (i == 0) {
			LawnMower.get().spawnMower(p.getLocation().add(0, 2, 0), p);
			i++;
		} else {
			LawnMower.get().removeMower();
			i = 0;
		}

	}
}
