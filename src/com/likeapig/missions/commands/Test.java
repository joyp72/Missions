package com.likeapig.missions.commands;

import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;

import com.likeapig.missions.map.Boss;

public class Test extends Commands {
	public Test() {
		super("raid.admin", "Test", "", new String[] { "t" });
	}

	int i = 0;

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;
		ShulkerBullet sb = p.getWorld().spawn(Boss.get().getParts().get("leftbutton").getLocation(), ShulkerBullet.class);
		sb.setInvulnerable(true);
		sb.setShooter(p);
		sb.setTarget(p);
		sb.setVelocity(sb.getLocation().getDirection().normalize().multiply(1));
		ShulkerBullet sb2 = p.getWorld().spawn(Boss.get().getParts().get("rightbutton").getLocation(), ShulkerBullet.class);
		sb2.setInvulnerable(true);
		sb2.setShooter(p);
		sb2.setTarget(p);
		sb2.setVelocity(sb2.getLocation().getDirection().normalize().multiply(1));
	}
}