package com.likeapig.missions.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;

import com.likeapig.missions.Main;
import com.likeapig.missions.map.Boss;

public class Test extends Commands {
	public Test() {
		super("raid.admin", "Test", "", new String[] { "t" });
	}

	int i = 0;

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;
		Fireball fb = p.getWorld().spawn(p.getLocation().add(p.getLocation().getDirection()), Fireball.class);
		fb.setVelocity(p.getLocation().getDirection().multiply(1));
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			@Override
			public void run() {
				fb.setVelocity(p.getLocation().getDirection());
				if (fb.isDead() || p.getLocation().distance(fb.getLocation()) > 40) {
					return;
				}
			}
		}, 0L, 0L);
	}
}