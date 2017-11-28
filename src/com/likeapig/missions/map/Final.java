package com.likeapig.missions.map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.likeapig.missions.Main;

public class Final {

	public static Final instance;
	private boolean spawned = false;
	private boolean met = false;
	private int id;

	static {
		instance = new Final();
	}

	public static Final get() {
		return instance;
	}

	public void spawnBoss(Location loc, Map m) {
		if (!spawned) {
			Boss.get().Chair(loc);
			id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
				@Override
				public void run() {
					if (!met) {
						Boss.get().rotateRight();
					}
					if (Boss.get().getParts().get("body2").getLocation().getDirection() == getDirection(
							Boss.get().getParts().get("body2").getLocation(), m.getPlayer().getLocation())) {
						met = true;
					}
				}
			}, 0L, 0L);
			spawned = true;
		}
	}

	public void removeBoss() {
		if (spawned) {
			Boss.get().removeChair();
			Bukkit.getServer().getScheduler().cancelTask(id);
			spawned = false;
		}
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
