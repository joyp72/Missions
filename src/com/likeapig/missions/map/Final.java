package com.likeapig.missions.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.mcmonkey.sentinel.SentinelTrait;

import com.likeapig.missions.Main;
import com.likeapig.missions.utils.ParticleEffect;
import com.likeapig.missions.utils.ParticleEffect.ParticleData;
import com.likeapig.missions.utils.Titles;

import net.apcat.simplesit.SimpleSitPlayer;
import net.citizensnpcs.api.npc.NPC;

public class Final {

	public static Final instance;
	private boolean spawned = false;
	private int id;
	private int id2;
	private int id3;
	private int t = 0;
	private int t2 = 0;
	// public static boolean reached1 = false;
	public static boolean fire = false;
	Fireball fb;
	Fireball fb2;
	List<Fireball> fbs = new ArrayList<Fireball>();
	List<Location> destroyed = new ArrayList<Location>();
	public static boolean hit = false;
	public static boolean check = false;
	boolean check2 = false;
	public boolean once = false;
	int round = 1;
	public static boolean isSitting = false;
	private List<String> msgs = new ArrayList<String>();
	public ItemStack potion;
	private String title = ChatColor.DARK_RED + "" + ChatColor.BOLD + "SCIENTIST" + ChatColor.RESET + ""
			+ ChatColor.GRAY + ": " + ChatColor.WHITE + "" + ChatColor.BOLD + "";

	static {
		instance = new Final();
	}

	public static Final get() {
		return instance;
	}

	public Final() {
		potion = new ItemStack(Material.POTION, 1, (byte) 0);
		{
			ItemMeta meta = potion.getItemMeta();
			meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Mysterious Potion");
			meta.addItemFlags(ItemFlag.values());
			potion.setItemMeta(meta);
			potion.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
		}
	}

	public void setMsgs() {
		msgs.add("OUCH, WHAT THE HELL!");
		msgs.add("CAN YOU NOT?!");
		msgs.add("I WAS SITTING THERE!! REEEEE");
		msgs.add("MY CHAIR! NOOOO");
	}

	public void spawnBoss(Location loc, Map m) {
		if (!spawned) {
			setMsgs();
			Boss.get().Chair(loc);
			Boss.get().setNPC();
			Boss.get().spawnLook();
			id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
				@Override
				public void run() {
					Boss.get().look(m.getPlayer().getLocation());
					if (Boss.get().getParts().get("body2") != null) {
						int body = (int) getDegrees(Boss.get().getParts().get("body2").getLocation());
						int entity = (int) 5
								* (Math.round(getDegrees(Boss.get().getLook().getEntity().getLocation()) / 5));
						t2++;
						if (t2 >= 24) {
							t2 = 0;
						}
						Boss.get().hover(t2);
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
					if (isSitting) {
						if (!check) {
							sit(loc, m);
							check = true;
						}
					}
					if (!isSitting) {
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
									isSitting = true;
								}
							}
						}
						if (round == 2) {
							if (!isSitting) {
								if (!hit) {
									if (destroyed.size() == 1) {
										t = 0;
										fire = false;
										round = 2;
										m.getPlayer().sendMessage(title + "You can't possibly defeat me!");
										spawnRound2(loc);
										hit = true;
									}
								} else {
									if (destroyed.size() == 2) {
										t = 0;
										fire = false;
										round = 3;
										isSitting = true;
										hit = false;
									}
								}
							}
						}
						if (round == 3) {
							if (!isSitting) {
								if (!hit) {
									if (destroyed.size() == 2) {
										t = 0;
										fire = false;
										round = 3;
										m.getPlayer().sendMessage(title + "Resistance is futile.");
										spawnRound3(loc);
										hit = true;
									}
								} else {
									if (destroyed.size() == 3) {
										t = 0;
										fire = false;
										round = 4;
										isSitting = true;
										hit = false;
									}
								}
							}
						}
						if (round == 4) {
							if (!isSitting) {
								if (!hit) {
									if (destroyed.size() == 3) {
										t = 0;
										fire = false;
										round = 4;
										m.getPlayer().sendMessage(title + "Your fate is already sealed.");
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
						}
						if (round == 5) {
							SimpleSitPlayer sp = new SimpleSitPlayer(
									(Player) Boss.get().getNPC().getTrait(SentinelTrait.class).getLivingEntity());
							if (!hit) {
								if (destroyed.size() == 4) {
									Boss.get().removeChair();
									Boss.get().getNPC().data().set(NPC.DEFAULT_PROTECTED_METADATA, true);
									Titles.get().addTitle(m.getPlayer(),
											ChatColor.RED + "" + ChatColor.BOLD + "MAD SCIENTIST", 90);
									Titles.get().addSubTitle(m.getPlayer(), ChatColor.WHITE
											+ "I have rigged this place to BLOW UP! HAHA (cough) (cough)", 90);
									m.getPlayer().getInventory().addItem(potion);
									m.message("You picked up a mysterious potion!");
									m.removeGlass();
									hit = true;
								}
							} else {
								if (!sp.isSitting()) {
									Boss.get().getNPC().getEntity().teleport(m.getBossLoc(4).clone().add(1, 0, 0));
									sp.setSitting(true);
								} else {
									setBlood();
									if (m.getPlayer().isGliding()) {
										if (!m.getPlayer().hasPotionEffect(PotionEffectType.JUMP)) {
											m.setComplete(true);
											m.removePlayer(m.getPlayer());
										}
									}
								}
							}
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

	public void setBlood() {
		Location l = Boss.get().getNPC().getEntity().getLocation();
		l.add(0, 1, 0);
		id3 = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			@Override
			public void run() {
				if (Boss.get().getNPC().getEntity() != null) {
					ParticleEffect.BLOCK_CRACK.display(new ParticleData(Material.REDSTONE_BLOCK, (byte) 0) {
					}, 0.0f, 0.0f, 0.0f, 0.0f, 1, l, 5);
				}
			}
		}, 0L, 60L);
	}

	public ItemStack getPotion() {
		return potion;
	}

	public void sit(Location loc, Map m) {
		Random r = new Random();
		String msg = msgs.get(r.nextInt(msgs.size()));
		SimpleSitPlayer sp = new SimpleSitPlayer(
				(Player) Boss.get().getNPC().getTrait(SentinelTrait.class).getLivingEntity());
		Boss.get().removeChair();
		Boss.get().getNPC().getEntity().teleport(m.getBossLoc(4).clone().add(1, 0, 0));
		Boss.get().getNPC().data().set(NPC.DEFAULT_PROTECTED_METADATA, false);
		sp.setSitting(true);
		Titles.get().addTitle(m.getPlayer(), ChatColor.RED + "" + ChatColor.BOLD + "MAD SCIENTIST", 100);
		Titles.get().addSubTitle(m.getPlayer(), ChatColor.WHITE + msg, 100);
		msgs.remove(msg);
		id2 = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			@Override
			public void run() {
				if (Boss.get().getNPC().getEntity() != null) {
					ParticleEffect.ANGRY_VILLAGER.display(Boss.get().getNPC().getEntity().getLocation().add(0, 2, 0),
							0f, 0f, 0f, 0f, 2);
				}
			}
		}, 0L, 20L);
		Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable() {
			@Override
			public void run() {
				sp.setSitting(false);
				hit = false;
				isSitting = false;
				check = false;
				Boss.get().getNPC().data().set(NPC.DEFAULT_PROTECTED_METADATA, true);
				Boss.get().Chair(loc);
				Boss.get().getParts().get("body2").setPassenger(Boss.get().getNPC().getEntity());
				Bukkit.getServer().getScheduler().cancelTask(id2);
			}
		}, 120L);
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
		}, 120L);
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
						if (!destroyed.contains(console.clone().add(0.5, 0.5, 0.5))) {
							destroyed.add(console.clone().add(0.5, 0.5, 0.5));
							m.message("A console has been destroyed!");
							check = true;
						}
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
			} else {
				hit = true;
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
			Bukkit.getServer().getScheduler().cancelTask(id3);
			Bukkit.getServer().getScheduler().cancelTask(id2);
			Bukkit.getServer().getScheduler().cancelTask(id);
			msgs.clear();
			destroyed.clear();
			fbs.clear();
			fire = false;
			check = false;
			check2 = false;
			isSitting = false;
			hit = false;
			t = 0;
			round = 1;
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
