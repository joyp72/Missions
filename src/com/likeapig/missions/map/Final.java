package com.likeapig.missions.map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.likeapig.missions.Main;

import de.Ste3et_C0st.FurnitureLib.main.FurnitureLib;
import net.md_5.bungee.api.ChatColor;

public class Final {

	public static Final instance;
	private boolean spawned = false;
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
			Boss.get().setNPC();
			Boss.get().spawnLook();
			id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
				@Override
				public void run() {
					Boss.get().look(m.getPlayer().getLocation());
					int body = (int) getDegrees(Boss.get().getParts().get("body2").getLocation());
					int entity = (int) 5 * (Math.round(getDegrees(Boss.get().getLook().getEntity().getLocation()) / 5));
					if (body < entity) {
						Boss.get().rotateRight();
					}
					if (body > entity) {
						Boss.get().rotateLeft();
					}
					Boss.get().getNPC().faceLocation(m.getPlayer().getLocation());
				}
			}, 0L, 0L);
			spawned = true;
		}
	}

	public float getDegrees(Location l) {
		if (l.getYaw() >= 0) {
			return l.getYaw();
		} else {
			return 360 - Math.abs(l.getYaw());
		}
	}

	public void removeBoss() {
		if (spawned) {
			Boss.get().removeChair();
			Boss.get().removeNPC();
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
