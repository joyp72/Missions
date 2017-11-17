package com.likeapig.missions.commands;

import org.bukkit.entity.Player;

import com.likeapig.missions.map.Boss;

public class SpawnBoss extends Commands {
	public SpawnBoss() {
		super("raid.admin", "Boss", "", new String[] { "b" });
	}

	@Override
	public void onCommand(final Player sender, final String[] args) {
		
		Boss.get().Chair(sender.getLocation(), sender);
		
	}
}
