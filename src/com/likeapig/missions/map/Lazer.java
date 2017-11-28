package com.likeapig.missions.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.likeapig.missions.utils.ParticleEffect;

public class Lazer {

	public static Lazer instance;
	public final Material[] NON_OPAQUE = { Material.SKULL, Material.AIR, Material.SAPLING, Material.WATER,
			Material.STATIONARY_WATER, Material.LAVA, Material.STATIONARY_LAVA, Material.POWERED_RAIL,
			Material.DETECTOR_RAIL, Material.WEB, Material.LONG_GRASS, Material.DEAD_BUSH, Material.YELLOW_FLOWER,
			Material.RED_ROSE, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.TORCH, Material.FIRE,
			Material.REDSTONE_WIRE, Material.CROPS, Material.LADDER, Material.RAILS, Material.SIGN_POST, Material.LEVER,
			Material.STONE_PLATE, Material.WOOD_PLATE, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON,
			Material.STONE_BUTTON, Material.SNOW, Material.SUGAR_CANE_BLOCK, Material.PORTAL, Material.DIODE_BLOCK_OFF,
			Material.DIODE_BLOCK_ON, Material.PUMPKIN_STEM, Material.MELON_STEM, Material.VINE, Material.WATER_LILY,
			Material.NETHER_WART_BLOCK, Material.ENDER_PORTAL, Material.COCOA, Material.TRIPWIRE_HOOK,
			Material.TRIPWIRE, Material.FLOWER_POT, Material.CARROT, Material.POTATO, Material.WOOD_BUTTON,
			Material.GOLD_PLATE, Material.IRON_PLATE, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON,
			Material.DAYLIGHT_DETECTOR, Material.CARPET, Material.DOUBLE_PLANT, Material.STANDING_BANNER,
			Material.WALL_BANNER, Material.DAYLIGHT_DETECTOR_INVERTED, Material.END_ROD, Material.CHORUS_PLANT,
			Material.CHORUS_FLOWER, Material.BEETROOT_BLOCK, Material.END_GATEWAY };
	Location l;
	Vector v;
	Location l2;
	Vector v2;
	boolean reached = false;
	boolean checked = false;
	boolean isBlocking = false;
	boolean damaged = false;
	boolean show = true;
	boolean killed = false;

	Location ll;
	Vector vv;
	Location ll2;
	Vector vv2;
	boolean rreached = false;
	boolean cchecked = false;
	boolean iisBlocking = false;
	boolean ddamaged = false;
	boolean sshow = true;
	boolean kkilled = false;

	Location lll;
	Vector vvv;
	Location lll2;
	Vector vvv2;
	boolean rrreached = false;
	boolean ccchecked = false;
	boolean iiisBlocking = false;
	boolean dddamaged = false;
	boolean ssshow = true;
	boolean kkkilled = false;

	Location llll;
	Vector vvvv;
	Location llll2;
	Vector vvvv2;
	boolean rrrreached = false;
	boolean cccchecked = false;
	boolean iiiisBlocking = false;
	boolean ddddamaged = false;
	boolean sssshow = true;
	boolean kkkkilled = false;
	
	boolean isEnabled = false;

	static {
		instance = new Lazer();
	}

	public static Lazer get() {
		return instance;
	}

	public void reset() {
		reached = false;
		isBlocking = false;
		checked = false;
		damaged = false;
		show = true;
		killed = false;

		rreached = false;
		cchecked = false;
		iisBlocking = false;
		ddamaged = false;
		sshow = true;
		kkilled = false;

		rrreached = false;
		ccchecked = false;
		iiisBlocking = false;
		dddamaged = false;
		ssshow = true;
		kkkilled = false;

		rrrreached = false;
		cccchecked = false;
		iiiisBlocking = false;
		ddddamaged = false;
		sssshow = true;
		kkkkilled = false;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void setEnabled(boolean b) {
		isEnabled = b;
	}
	
	public boolean isDead() {
		if (killed && kkilled && kkkilled && kkkkilled) {
			return true;
		} else {
			return false;
		}
	}

	public void lazer1(int t, Location location, Location player, Player p, Map m) {
		isEnabled = true;
		if (!killed) {
			if (t <= 60) {
				Location loc = location.clone();
				loc.add(loc.getDirection().multiply(1).normalize());
				displayColoredParticle(loc, "ff7400");
			}
			if (t > 60 && t < 100) {
				Location loc = location.clone();
				loc.add(loc.getDirection().multiply(1).normalize());
				displayColoredParticle(loc, "ff2700");
			}
			if (t == 100) {
				l = location.clone();
				v = getDirection(l, player.add(0, 1, 0));
				l2 = p.getLocation().clone().add(0, 1, 0);
				v2 = getDirection(l2, location);
			}
			if (t > 100 && t < 160) {
				fireShot(l, v);
				if (l.distance(p.getLocation()) <= 2) {
					reached = true;
				}
				if (reached) {
					if (!checked) {
						if (p.isBlocking()) {
							isBlocking = true;
							checked = true;
						} else {
							isBlocking = false;
							checked = true;
						}
					}
					if (checked && !damaged && isBlocking) {
						fireShotBack(l2, v2);
						if (l2.distance(location) <= 2) {
							ParticleEffect.EXPLOSION_LARGE.display(location, 0.0f, 0.0f, 0.0f, 0.0f, 1);
							p.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
							location.getBlock().setType(Material.AIR);
							location.getBlock().getState().update();
							killed = true;
							damaged = true;
							show = false;
						}
					}
					if (checked && !damaged && !isBlocking) {
						ParticleEffect.EXPLOSION_LARGE.display(player, 0.0f, 0.0f, 0.0f, 0.0f, 1);
						p.getWorld().playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
						p.damage(12);
						show = false;
						damaged = true;
					}
				}
			}
			if (t >= 160) {
				reached = false;
				isBlocking = false;
				checked = false;
				damaged = false;
				show = true;
				m.resetT(1);
			}
		}
	}

	public void fireShot(Location loc, Vector vec) {
		Block b = loc.getBlock();
		if (!isSolid(b)) {
			loc.add(vec.normalize().multiply(0.8));
			if (show) {
				displayColoredParticle(loc, "E41B17");
				ParticleEffect.SMOKE.display(loc, 0.0f, 0.0f, 0.0f, 0.0f, 25);
			}
		}

	}

	public void fireShotBack(Location loc2, Vector vec2) {
		Block block = loc2.getBlock();
		if (!isSolid(block)) {
			loc2.add(vec2.normalize().multiply(0.8));
			if (show) {
				displayColoredParticle(loc2, "E41B17", 0.0f, 0.0f, 0.0f);
				ParticleEffect.SMOKE.display(loc2, 0.0f, 0.0f, 0.0f, 0.0f, 25);
			}
		}
	}
	
	public void lazer2(int t, Location location, Location player, Player p, Map m) {
		isEnabled = true;
		if (!kkilled) {
			if (t <= 60 + 40) {
				Location loc = location.clone();
				loc.add(loc.getDirection().multiply(1).normalize());
				displayColoredParticle(loc, "ff7400");
			}
			if (t > 60 + 40 && t < 100 + 40) {
				Location loc = location.clone();
				loc.add(loc.getDirection().multiply(1).normalize());
				displayColoredParticle(loc, "ff2700");
			}
			if (t == 100 + 40) {
				ll = location.clone();
				vv = getDirection(ll, player.add(0, 1, 0));
				ll2 = p.getLocation().clone().add(0, 1, 0);
				vv2 = getDirection(ll2, location);
			}
			if (t > 100 + 40 && t < 160 + 40) {
				fireShot2(ll, vv);
				if (ll.distance(p.getLocation()) <= 2) {
					rreached = true;
				}
				if (rreached) {
					if (!cchecked) {
						if (p.isBlocking()) {
							iisBlocking = true;
							cchecked = true;
						} else {
							iisBlocking = false;
							cchecked = true;
						}
					}
					if (cchecked && !ddamaged && iisBlocking) {
						fireShotBack2(ll2, vv2);
						if (ll2.distance(location) <= 2) {
							ParticleEffect.EXPLOSION_LARGE.display(location, 0.0f, 0.0f, 0.0f, 0.0f, 1);
							p.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
							location.getBlock().setType(Material.AIR);
							location.getBlock().getState().update();
							kkilled = true;
							ddamaged = true;
							sshow = false;
						}
					}
					if (cchecked && !ddamaged && !iisBlocking) {
						ParticleEffect.EXPLOSION_LARGE.display(player, 0.0f, 0.0f, 0.0f, 0.0f, 1);
						p.getWorld().playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
						p.damage(12);
						sshow = false;
						ddamaged = true;
					}
				}
			}
			if (t >= 160 + 40) {
				rreached = false;
				iisBlocking = false;
				cchecked = false;
				ddamaged = false;
				sshow = true;
				m.resetT(2);
			}
		}
	}

	public void fireShot2(Location loc, Vector vec) {
		Block b = loc.getBlock();
		if (!isSolid(b)) {
			loc.add(vec.normalize().multiply(0.8));
			if (sshow) {
				displayColoredParticle(loc, "E41B17");
				ParticleEffect.SMOKE.display(loc, 0.0f, 0.0f, 0.0f, 0.0f, 25);
			}
		}

	}

	public void fireShotBack2(Location loc2, Vector vec2) {
		Block block = loc2.getBlock();
		if (!isSolid(block)) {
			loc2.add(vec2.normalize().multiply(0.8));
			if (sshow) {
				displayColoredParticle(loc2, "E41B17", 0.0f, 0.0f, 0.0f);
				ParticleEffect.SMOKE.display(loc2, 0.0f, 0.0f, 0.0f, 0.0f, 25);
			}
		}
	}
	
	public void lazer3(int t, Location location, Location player, Player p, Map m) {
		isEnabled = true;
		if (!kkkilled) {
			if (t <= 60 + 80) {
				Location loc = location.clone();
				loc.add(loc.getDirection().multiply(1).normalize());
				displayColoredParticle(loc, "ff7400");
			}
			if (t > 60 + 80 && t < 100 + 80) {
				Location loc = location.clone();
				loc.add(loc.getDirection().multiply(1).normalize());
				displayColoredParticle(loc, "ff2700");
			}
			if (t == 100 + 80) {
				lll = location.clone();
				vvv = getDirection(lll, player.add(0, 1, 0));
				lll2 = p.getLocation().clone().add(0, 1, 0);
				vvv2 = getDirection(lll2, location);
			}
			if (t > 100 + 80 && t < 160 + 80) {
				fireShot3(lll, vvv);
				if (lll.distance(p.getLocation()) <= 2) {
					rrreached = true;
				}
				if (rrreached) {
					if (!ccchecked) {
						if (p.isBlocking()) {
							iiisBlocking = true;
							ccchecked = true;
						} else {
							iiisBlocking = false;
							ccchecked = true;
						}
					}
					if (ccchecked && !dddamaged && iiisBlocking) {
						fireShotBack3(lll2, vvv2);
						if (lll2.distance(location) <= 2) {
							ParticleEffect.EXPLOSION_LARGE.display(location, 0.0f, 0.0f, 0.0f, 0.0f, 1);
							p.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
							location.getBlock().setType(Material.AIR);
							location.getBlock().getState().update();
							kkkilled = true;
							dddamaged = true;
							ssshow = false;
						}
					}
					if (ccchecked && !dddamaged && !iiisBlocking) {
						ParticleEffect.EXPLOSION_LARGE.display(player, 0.0f, 0.0f, 0.0f, 0.0f, 1);
						p.getWorld().playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
						p.damage(12);
						ssshow = false;
						dddamaged = true;
					}
				}
			}
			if (t >= 160 + 80) {
				rrreached = false;
				iiisBlocking = false;
				ccchecked = false;
				dddamaged = false;
				ssshow = true;
				m.resetT(3);
			}
		}
	}

	public void fireShot3(Location loc, Vector vec) {
		Block b = loc.getBlock();
		if (!isSolid(b)) {
			loc.add(vec.normalize().multiply(0.8));
			if (ssshow) {
				displayColoredParticle(loc, "E41B17");
				ParticleEffect.SMOKE.display(loc, 0.0f, 0.0f, 0.0f, 0.0f, 25);
			}
		}

	}
	
	public void fireShotBack3(Location loc2, Vector vec2) {
		Block block = loc2.getBlock();
		if (!isSolid(block)) {
			loc2.add(vec2.normalize().multiply(0.8));
			if (ssshow) {
				displayColoredParticle(loc2, "E41B17", 0.0f, 0.0f, 0.0f);
				ParticleEffect.SMOKE.display(loc2, 0.0f, 0.0f, 0.0f, 0.0f, 25);
			}
		}
	}
	
	public void lazer4(int t, Location location, Location player, Player p, Map m) {
		isEnabled = true;
		if (!kkkkilled) {
			if (t <= 60 + 120) {
				Location loc = location.clone();
				loc.add(loc.getDirection().multiply(1).normalize());
				displayColoredParticle(loc, "ff7400");
			}
			if (t > 60 + 120 && t < 100 + 120) {
				Location loc = location.clone();
				loc.add(loc.getDirection().multiply(1).normalize());
				displayColoredParticle(loc, "ff2700");
			}
			if (t == 100 + 120) {
				llll = location.clone();
				vvvv = getDirection(llll, player.add(0, 1, 0));
				llll2 = p.getLocation().clone().add(0, 1, 0);
				vvvv2 = getDirection(llll2, location);
			}
			if (t > 100 + 120 && t < 160 + 120) {
				fireShot4(llll, vvvv);
				if (llll.distance(p.getLocation()) <= 2) {
					rrrreached = true;
				}
				if (rrrreached) {
					if (!cccchecked) {
						if (p.isBlocking()) {
							iiiisBlocking = true;
							cccchecked = true;
						} else {
							iiiisBlocking = false;
							cccchecked = true;
						}
					}
					if (cccchecked && !ddddamaged && iiiisBlocking) {
						fireShotBack4(llll2, vvvv2);
						if (llll2.distance(location) <= 2) {
							ParticleEffect.EXPLOSION_LARGE.display(location, 0.0f, 0.0f, 0.0f, 0.0f, 1);
							p.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
							location.getBlock().setType(Material.AIR);
							location.getBlock().getState().update();
							kkkkilled = true;
							ddddamaged = true;
							sssshow = false;
						}
					}
					if (cccchecked && !ddddamaged && !iiiisBlocking) {
						ParticleEffect.EXPLOSION_LARGE.display(player, 0.0f, 0.0f, 0.0f, 0.0f, 1);
						p.getWorld().playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
						p.damage(12);
						sssshow = false;
						ddddamaged = true;
					}
				}
			}
			if (t >= 160 + 120) {
				rrrreached = false;
				iiiisBlocking = false;
				cccchecked = false;
				ddddamaged = false;
				sssshow = true;
				m.resetT(4);
			}
		}
	}

	public void fireShot4(Location loc, Vector vec) {
		Block b = loc.getBlock();
		if (!isSolid(b)) {
			loc.add(vec.normalize().multiply(0.8));
			if (sssshow) {
				displayColoredParticle(loc, "E41B17");
				ParticleEffect.SMOKE.display(loc, 0.0f, 0.0f, 0.0f, 0.0f, 25);
			}
		}

	}

	public void fireShotBack4(Location loc2, Vector vec2) {
		Block block = loc2.getBlock();
		if (!isSolid(block)) {
			loc2.add(vec2.normalize().multiply(0.8));
			if (sssshow) {
				displayColoredParticle(loc2, "E41B17", 0.0f, 0.0f, 0.0f);
				ParticleEffect.SMOKE.display(loc2, 0.0f, 0.0f, 0.0f, 0.0f, 25);
			}
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

	public boolean isSolid(Block block) {
		return !Arrays.asList(NON_OPAQUE).contains(block.getType());
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
