package com.likeapig.missions.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.mcmonkey.sentinel.SentinelTrait;

import com.likeapig.missions.Main;
import com.likeapig.missions.utils.ParticleEffect;
import com.likeapig.missions.utils.Titles;

import de.Ste3et_C0st.FurnitureLib.main.FurnitureLib;
import net.apcat.simplesit.SimpleSitPlayer;
import net.citizensnpcs.api.npc.NPC;
import net.minecraft.server.v1_12_R1.Packet;

public class Final {

	public static Final instance;
	private boolean spawned = false;
	private int id;
	private int t = 0;
	// public static boolean reached1 = false;
	public static boolean fire = false;
	Fireball fb;
	Fireball fb2;
	List<Fireball> fbs = new ArrayList<Fireball>();
	public FurnitureLib ins = FurnitureLib.getInstance();
	boolean d1 = false;
	boolean d2 = false;
	boolean d3 = false;
	boolean d4 = false;
	List<Location> destroyed = new ArrayList<Location>();
	public static boolean hit = false;
	public static boolean check = false;
	public boolean once = false;
	int round = 5;

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
					if (round != 5) {
						int body = (int) getDegrees(Boss.get().getParts().get("body2").getLocation());
						int entity = (int) 5
								* (Math.round(getDegrees(Boss.get().getLook().getEntity().getLocation()) / 5));
						if (body < entity) {
							Boss.get().rotateRight();
						}
						if (body > entity) {
							Boss.get().rotateLeft();
						}
						Boss.get().getNPC().faceLocation(m.getPlayer().getLocation());
					}
					if (fire) {
						t++;
						fireBall(t, Boss.get().getParts().get("leftbutton").getEyeLocation(),
								m.getPlayer().getLocation(), m,
								Boss.get().getParts().get("rightbutton").getEyeLocation(), m.getPlayer().getLocation());
						// fireBall2(t2, Boss.get().getParts().get("rightbutton").getEyeLocation(),
						// m.getPlayer().getLocation(), m);
					}
					if (destroyed.size() != 0) {
						for (Location ds : destroyed) {
							destroyConsole(ds);
						}
					}
					if (round == 1) {
						if (!hit) {
							if (destroyed.isEmpty()) {
								t = 0;
								fire = false;
								spawnRound1(loc);
								hit = true;
							}
						} else {
							if (destroyed.size() == 1) {
								t = 0;
								fire = false;
								round = 2;
								hit = false;
							}
						}
					}
					if (round == 2) {
						if (!hit) {
							if (destroyed.size() == 1) {
								t = 0;
								fire = false;
								round = 2;
								spawnRound2(loc);
								hit = true;
							}
						} else {
							if (destroyed.size() == 2) {
								t = 0;
								fire = false;
								round = 3;
								hit = false;
							}
						}
					}
					if (round == 3) {
						if (!hit) {
							if (destroyed.size() == 2) {
								t = 0;
								fire = false;
								round = 3;
								spawnRound3(loc);
								hit = true;
							}
						} else {
							if (destroyed.size() == 3) {
								t = 0;
								fire = false;
								round = 4;
								hit = false;
							}
						}
					}
					if (round == 4) {
						if (!hit) {
							if (destroyed.size() == 3) {
								t = 0;
								fire = false;
								round = 4;
								spawnRound4(loc);
								hit = true;
							}
						} else {
							if (destroyed.size() == 4) {
								t = 0;
								fire = false;
								round = 5;
								hit = false;
							}
						}
					}
					if (round == 5) {
						if (!hit) {
							// if (destroyed.size() == 4) {
							Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable() {
								@Override
								public void run() {
									Boss.get().removeChair();
									//Boss.get().getNPC().getTrait(SentinelTrait.class).getLivingEntity()
										//	.setGliding(true);
									//Boss.get().getNPC().getTrait(SentinelTrait.class).getLivingEntity().setAI(false);
									Boss.get().getNPC().data().set(NPC.NAMEPLATE_VISIBLE_METADATA, false);
									SimpleSitPlayer sp = new SimpleSitPlayer((Player) Boss.get().getNPC().getTrait(SentinelTrait.class).getLivingEntity());
									sp.setLaying(true);
									hit = true;
								}
							}, 20L);

							// }
						}
					}
				}
			}, 0L, 0L);
			spawned = true;
			Titles.get().addTitle(m.getPlayer(), ChatColor.RED + "" + ChatColor.BOLD + "MAD SCIENTIST", 60);
			Titles.get().addSubTitle(m.getPlayer(), ChatColor.WHITE + "You really think you can beat me?! HAHAHA", 60);
		}

	}

	public void setFire(boolean b) {
		fire = b;
	}

	public int getRound() {
		return round;
	}

	public List<Location> getDestroyed() {
		return destroyed;
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
		}, 60L);
	}

	public void spawnRound2(Location loc) {
		Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable() {
			@Override
			public void run() {
				Mob.get().spawnGuard(loc.clone().add(4, 0, 5));
				Mob.get().spawnGuard(loc.clone().add(0, 0, 5).subtract(4, 0, 0));
				Mob.get().spawnGuard(loc.clone().add(4, 0, 0).subtract(0, 0, 5));
				Mob.get().spawnGuard(loc.clone().subtract(4, 0, 5));
			}
		}, 60L);
	}

	public void spawnRound3(Location loc) {
		Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable() {
			@Override
			public void run() {
				Mob.get().spawnLits(loc.clone().add(4, 0, 5));
				Mob.get().spawnLits(loc.clone().add(0, 0, 5).subtract(4, 0, 0));
				Mob.get().spawnLits(loc.clone().add(4, 0, 0).subtract(0, 0, 5));
				Mob.get().spawnLits(loc.clone().subtract(4, 0, 5));
			}
		}, 60L);
	}

	public void spawnRound4(Location loc) {
		Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable() {
			@Override
			public void run() {
				Mob.get().spawnT2000(loc.clone().add(4, 0, 5));
				Mob.get().spawnT2000(loc.clone().add(0, 0, 5).subtract(4, 0, 0));
				Mob.get().spawnT2000(loc.clone().add(4, 0, 0).subtract(0, 0, 5));
				Mob.get().spawnT2000(loc.clone().subtract(4, 0, 5));
			}
		}, 60L);
	}

	public void fireBall(int i, Location from, Location to, Map m, Location from2, Location to2) {
		if (fire) {
			Location l = null;
			Location l2 = null;
			Vector v = null;
			Vector v2 = null;
			if (i >= 0 && i <= 60) {
				Location loc = from.clone().add(0, 0.5, 0);
				displayColoredParticle(loc, "ff7400");
				Location loc2 = from2.clone().add(0, 0.5, 0);
				displayColoredParticle(loc2, "ff7400");
			}
			if (i > 60 && i < 120) {
				Location loc = from.clone().add(0, 0.5, 0);
				displayColoredParticle(loc, "ff2700");
				ParticleEffect.SMOKE.display(loc, 0.0f, 0.0f, 0.0f, 0.0f, 10);
				Location loc2 = from2.clone().add(0, 0.5, 0);
				displayColoredParticle(loc2, "ff2700");
				ParticleEffect.SMOKE.display(loc2, 0.0f, 0.0f, 0.0f, 0.0f, 10);
			}
			if (i == 120) {
				l = from.clone();
				v = getDirection(l, to.add(0, 3, 0));
				l.add(v.normalize().multiply(3));
				fb = from.getWorld().spawn(l.add(l.getDirection()), Fireball.class);
				l2 = from2.clone();
				v2 = getDirection(l2, to2.add(0, 3, 0));
				l2.add(v2.normalize().multiply(3));
				fb2 = from2.getWorld().spawn(l2.add(l2.getDirection()), Fireball.class);
				fbs.add(fb);
				fbs.add(fb2);
				once = false;
			}
			if (i > 120) {
				for (Location console : m.getConsoles()) {
					if (fb.getLocation().distance(console) <= 4) {
						if (!destroyed.contains(console)) {
							destroyed.add(console.clone().add(0.5, 0.5, 0.5));
							m.message("A console has been destroyed!");
						}
						check = true;
					}
				}
			}
		}
	}

	public void exploded() {
		if (!once) {
			fbs.clear();
			if (!check) {
				hit = false;
				MapManager.get().getMap("test").message("no hit!");
			} else {
				hit = true;
				MapManager.get().getMap("test").message("hit!");
				check = false;
			}
			t = 0;
			fire = false;
			once = true;
		}
	}

	public void destroyConsole(Location l) {
		ParticleEffect.SMOKE.display(l, 0.0f, 0.0f, 0.0f, 0.0f, 30);
	}

	public void removeBoss() {
		if (spawned) {
			Boss.get().removeChair();
			Boss.get().removeNPC();
			Bukkit.getServer().getScheduler().cancelTask(id);
			destroyed.clear();
			fbs.clear();
			fire = false;
			check = false;
			hit = false;
			t = 0;
			round = 5;
			spawned = false;
		}
	}

	public List<Fireball> getFbs() {
		return fbs;
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
