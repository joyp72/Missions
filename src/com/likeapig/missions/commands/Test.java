package com.likeapig.missions.commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.likeapig.missions.map.MapManager;

public class Test extends Commands {
	public Test() {
		super("raid.admin", "Test", "", new String[] { "t" });
	}

	int i = 0;

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;
		Location l = p.getLocation().clone();
		l.setYaw(p.getLocation().getDirection().angle(MapManager.get().getMap("test").getBossLoc(4).getDirection()));
		p.teleport(l);
	}
	
	public static Vector getDirection(Location location, Location destination) {
		double x1, y1, z1;
		double x0, y0, z0;

		x1 = destination.getX();
		y1 = destination.getY();
		z1 = destination.getZ();

		x0 = location.getX();
		y0 = location.getY();
		z0 = location.getZ();

		return new Vector(x1 - x0, y1 - y0, z1 - z0);
	}
}