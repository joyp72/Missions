package com.likeapig.missions.commands;

import org.bukkit.entity.Player;

public class Glide extends Commands {
	public Glide() {
		super("raid.admin", "Glide", "", new String[] { "g" });
	}

	int i = 0;

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;

		p.setGliding(true);
		if (p.isGliding()) {
			p.setVelocity(p.getLocation().getDirection().normalize().multiply(0.2));
		}
	}
}
