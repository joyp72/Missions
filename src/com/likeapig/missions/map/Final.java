package com.likeapig.missions.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.likeapig.missions.Main;
import com.likeapig.missions.utils.ParticleEffect;
import com.likeapig.missions.utils.Titles;

import net.md_5.bungee.api.ChatColor;

public class Final {

	public static Final instance;
	private boolean spawned = false;
	private int id;
	private int t = 0;
	private int t2 = 0;
	boolean reached1 = false;
	boolean reached2 = false;
	boolean round1 = true;
	Fireball fb;
	Fireball fb2;

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
					if (!reached1 && !reached2 && round1) {
						t++;
						t2++;
						fireBall(t, Boss.get().getParts().get("leftbutton").getEyeLocation(),
								m.getPlayer().getLocation());
						fireBall2(t2, Boss.get().getParts().get("rightbutton").getEyeLocation(),
								m.getPlayer().getLocation());
					}
				}
			}, 0L, 0L);
			spawned = true;
			//Titles.get().addTitle(m.getPlayer(), ChatColor.RED + "" + ChatColor.BOLD + "MAD SCIENTIST", 160);
			//Titles.get().addSubTitle(m.getPlayer(), ChatColor.WHITE + "You really think you can beat me?! HAHAHA", 140);
			// spawnRound1(loc);
		}
	}

	public void setRoundOver(int i) {
		if (i == 1) {
			round1 = true;
		}
	}

	public void spawnRound1(Location loc) {
		Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable() {
			@Override
			public void run() {
				Mob.get().spawnRound1(loc.clone().add(4, 0, 5));
				Mob.get().spawnRound1(loc.clone().add(0, 0, 5).subtract(4, 0, 0));
				Mob.get().spawnRound1(loc.clone().add(4, 0, 0).subtract(0, 0, 5));
				Mob.get().spawnRound1(loc.clone().subtract(4, 0, 5));
			}
		}, 160L);
	}

	public void fireBall(int i, Location from, Location to) {
		if (!reached1) {
			Location l = null;
			Vector v = null;
			if (i >= 0 && i <= 60) {
				Location loc = from.clone().add(0, 0.5, 0);
				displayColoredParticle(loc, "ff7400");
			}
			if (i > 60 && i < 120) {
				Location loc = from.clone().add(0, 0.5, 0);
				displayColoredParticle(loc, "ff2700");
				ParticleEffect.SMOKE.display(loc, 0.0f, 0.0f, 0.0f, 0.0f, 10);
			}
			if (i == 120) {
				l = from.clone();
				v = getDirection(l, to.add(0, 3, 0));
				l.add(v.normalize().multiply(2));
				fb = from.getWorld().spawn(l.add(l.getDirection()), Fireball.class);
			}
			if (i > 120) {
				if (fb != null) {
					if (fb.isDead()) {
						reached1 = true;
						this.t = 0;
					}
				} else {
					reached1 = true;
					this.t = 0;
				}
			}
		}
	}

	public void fireBall2(int i, Location from, Location to) {
		if (!reached2) {
			Location l = null;
			Vector v = null;
			if (i >= 0 && i <= 60) {
				Location loc = from.clone().add(0, 0.5, 0);
				displayColoredParticle(loc, "ff7400");
			}
			if (i > 60 && i < 120) {
				Location loc = from.clone().add(0, 0.5, 0);
				displayColoredParticle(loc, "ff2700");
				ParticleEffect.SMOKE.display(loc, 0.0f, 0.0f, 0.0f, 0.0f, 10);
			}
			if (i == 120) {
				l = from.clone();
				v = getDirection(l, to.add(0, 3, 0));
				l.add(v.normalize().multiply(2));
				fb2 = from.getWorld().spawn(l.add(l.getDirection()), Fireball.class);
			}
			if (i > 120) {
				if (fb2 != null) {
					if (fb2.isDead()) {
						reached2 = true;
						this.t2 = 0;
					}
				} else {
					reached2 = true;
					this.t2 = 0;
				}
			}
		}
	}

	public void removeBoss() {
		if (spawned) {
			Boss.get().removeChair();
			Boss.get().removeNPC();
			Bukkit.getServer().getScheduler().cancelTask(id);
			reached1 = false;
			reached2 = false;
			round1 = true;
			t = 0;
			t2 = 0;
			spawned = false;
		}
	}

	public float getDegrees(Location l) {
		if (l.getYaw() >= 0) {
			return l.getYaw();
		} else {
			return 360 - Math.abs(l.getYaw());
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

	public List<Entity> getEntitiesAroundPoint(Location location, double radius) {
		List<Entity> entities = new ArrayList<Entity>();
		World world = location.getWorld();

		int smallX = (int) (location.getX() - radius) >> 4;
		int bigX = (int) (location.getX() + radius) >> 4;
		int smallZ = (int) (location.getZ() - radius) >> 4;
		int bigZ = (int) (location.getZ() + radius) >> 4;

		for (int x = smallX; x <= bigX; x++) {
			for (int z = smallZ; z <= bigZ; z++) {
				if (world.isChunkLoaded(x, z)) {
					entities.addAll(Arrays.asList(world.getChunkAt(x, z).getEntities()));
				}
			}
		}

		Iterator<Entity> entityIterator = entities.iterator();
		while (entityIterator.hasNext()) {
			Entity e = entityIterator.next();
			if (e.getWorld().equals(location.getWorld())
					&& e.getLocation().distanceSquared(location) > radius * radius) {
				entityIterator.remove();
			} else if (e instanceof Player && ((Player) e).getGameMode().equals(GameMode.SPECTATOR)) {
				entityIterator.remove();
			}
		}

		return entities;
	}

	public static void displayColoredParticle(Location loc, ParticleEffect type, String hexVal, float xOffset,
			float yOffset, float zOffset) {
		int R = 0;
		int G = 0;
		int B = 0;

		if (hexVal.length() <= 6) {
			R = Integer.valueOf(hexVal.substring(0, 2), 16);
			G = Integer.valueOf(hexVal.substring(2, 4), 16);
			B = Integer.valueOf(hexVal.substring(4, 6), 16);
			if (R <= 0) {
				R = 1;
			}
		} else if (hexVal.length() <= 7 && hexVal.substring(0, 1).equals("#")) {
			R = Integer.valueOf(hexVal.substring(1, 3), 16);
			G = Integer.valueOf(hexVal.substring(3, 5), 16);
			B = Integer.valueOf(hexVal.substring(5, 7), 16);
			if (R <= 0) {
				R = 1;
			}
		}

		loc.setX(loc.getX() + Math.random() * (xOffset / 2 - -(xOffset / 2)));
		loc.setY(loc.getY() + Math.random() * (yOffset / 2 - -(yOffset / 2)));
		loc.setZ(loc.getZ() + Math.random() * (zOffset / 2 - -(zOffset / 2)));

		if (type == ParticleEffect.RED_DUST || type == ParticleEffect.REDSTONE) {
			ParticleEffect.RED_DUST.display(R, G, B, 0.004F, 0, loc, 255.0);
		} else if (type == ParticleEffect.SPELL_MOB || type == ParticleEffect.MOB_SPELL) {
			ParticleEffect.SPELL_MOB.display((float) 255 - R, (float) 255 - G, (float) 255 - B, 1, 0, loc, 255.0);
		} else if (type == ParticleEffect.SPELL_MOB_AMBIENT || type == ParticleEffect.MOB_SPELL_AMBIENT) {
			ParticleEffect.SPELL_MOB_AMBIENT.display((float) 255 - R, (float) 255 - G, (float) 255 - B, 1, 0, loc,
					255.0);
		} else {
			ParticleEffect.RED_DUST.display(0, 0, 0, 0.004F, 0, loc, 255.0D);
		}
	}

	public static void displayColoredParticle(Location loc, String hexVal) {
		int R = 0;
		int G = 0;
		int B = 0;

		if (hexVal.length() <= 6) {
			R = Integer.valueOf(hexVal.substring(0, 2), 16);
			G = Integer.valueOf(hexVal.substring(2, 4), 16);
			B = Integer.valueOf(hexVal.substring(4, 6), 16);
			if (R <= 0) {
				R = 1;
			}
		} else if (hexVal.length() <= 7 && hexVal.substring(0, 1).equals("#")) {
			R = Integer.valueOf(hexVal.substring(1, 3), 16);
			G = Integer.valueOf(hexVal.substring(3, 5), 16);
			B = Integer.valueOf(hexVal.substring(5, 7), 16);
			if (R <= 0) {
				R = 1;
			}
		}
		ParticleEffect.RED_DUST.display(R, G, B, 0.004F, 0, loc, 257D);
	}

	public static void displayColoredParticle(Location loc, String hexVal, float xOffset, float yOffset,
			float zOffset) {
		int R = 0;
		int G = 0;
		int B = 0;

		if (hexVal.length() <= 6) {
			R = Integer.valueOf(hexVal.substring(0, 2), 16);
			G = Integer.valueOf(hexVal.substring(2, 4), 16);
			B = Integer.valueOf(hexVal.substring(4, 6), 16);
			if (R <= 0) {
				R = 1;
			}
		} else if (hexVal.length() <= 7 && hexVal.substring(0, 1).equals("#")) {
			R = Integer.valueOf(hexVal.substring(1, 3), 16);
			G = Integer.valueOf(hexVal.substring(3, 5), 16);
			B = Integer.valueOf(hexVal.substring(5, 7), 16);
			if (R <= 0) {
				R = 1;
			}
		}

		loc.setX(loc.getX() + Math.random() * (xOffset / 2 - -(xOffset / 2)));
		loc.setY(loc.getY() + Math.random() * (yOffset / 2 - -(yOffset / 2)));
		loc.setZ(loc.getZ() + Math.random() * (zOffset / 2 - -(zOffset / 2)));

		ParticleEffect.RED_DUST.display(R, G, B, 0.004F, 0, loc, 257D);
	}
}
