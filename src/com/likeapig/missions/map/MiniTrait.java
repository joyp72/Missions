package com.likeapig.missions.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.likeapig.missions.Main;
import com.likeapig.missions.utils.ParticleEffect;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;

@TraitName("MiniTrait")

public class MiniTrait extends Trait {
	public MiniTrait() {
		super("MiniTrait");
		plugin = JavaPlugin.getPlugin(Main.class);
	}

	Plugin plugin = null;
	Map map = MapManager.get().getMap("test");
	Player p = map.getPlayer();
	Boolean charged = false;
	int t = 0;
	ConcurrentHashMap<Location, Vector> locs = new ConcurrentHashMap<Location, Vector>();
	HashMap<Location, Integer> ranges = new HashMap<Location, Integer>();

	Location loc;
	Vector vec;

	Location loc2;
	Vector vec2;
	
	private ItemStack diamond = new ItemStack(Material.DIAMOND_CHESTPLATE);
	private ItemStack gold = new ItemStack(Material.GOLD_CHESTPLATE);
	private ItemStack iron = new ItemStack(Material.IRON_CHESTPLATE);

	boolean locReached = false;
	boolean isBlocking = false;
	boolean checkBlock = false;
	boolean damaged = false;
	boolean show = true;

	boolean SomeSetting = false;

	// see the 'Persistence API' section
	@Persist("mysettingname")
	boolean automaticallyPersistedSetting = false;

	// Here you should load up any values you have previously saved (optional).
	// This does NOT get called when applying the trait for the first time, only
	// loading onto an existing npc at server start.
	// This is called AFTER onAttach so you can load defaults in onAttach and they
	// will be overridden here.
	// This is called BEFORE onSpawn, npc.getBukkitEntity() will return null.
	public void load(DataKey key) {
		SomeSetting = key.getBoolean("SomeSetting", false);
	}

	// Save settings for this NPC (optional). These values will be persisted to the
	// Citizens saves file
	public void save(DataKey key) {
		key.setBoolean("SomeSetting", SomeSetting);
	}

	// An example event handler. All traits will be registered automatically as
	// Bukkit Listeners.

	// Called every tick
	@Override
	public void run() {
		if (npc.isSpawned()) {
			t++;
			setCharge(t, npc.getEntity().getLocation());
			if (p != null) {
				if (npc.getEntity().getLocation().distance(p.getLocation()) <= 6) {
					npc.getNavigator().cancelNavigation();
					npc.faceLocation(p.getLocation().subtract(0, 1, 0));
				} else {
					npc.getNavigator().setTarget(p, true);
				}
			}
		}
	}

	public void chargeShot() {
		if (!charged) {

		}

	}

	public void setCharge(int t, Location l) {
		if (!charged) {
			if (t <= 60) {
				Location loc = l.clone().add(0.0, 1.6, 0);
				loc.add(loc.getDirection().multiply(1).normalize());
				displayColoredParticle(loc, "ffb90f");
			}
			if (t > 60 && t < 120) {
				Location loc = l.clone().add(0.0, 1.6, 0);
				loc.add(loc.getDirection().multiply(1).normalize());
				displayColoredParticle(loc, "ff7400");
			}
			if (t >= 120 && t < 160) {
				Location loc = l.clone().add(0.0, 1.6, 0);
				loc.add(loc.getDirection().multiply(1).normalize());
				displayColoredParticle(loc, "ff2700");
			}
			if (t == 160) {
				loc = npc.getEntity().getLocation().add(0, 1.6, 0);
				vec = loc.getDirection();
				loc2 = p.getLocation().add(0, 1, 0);
				vec2 = getDirection(loc2, npc.getEntity().getLocation().add(0, 1, 0));
			}
			if (t > 160 && t < 320) {
				fireShot();
				if (loc.distance(p.getLocation()) <= 2) {
					locReached = true;
				}
				if (locReached) {
					if (!checkBlock) {
						if (p.isBlocking()) {
							isBlocking = true;
							checkBlock = true;
						} else {
							isBlocking = false;
							checkBlock = true;
						}
					}
					if (checkBlock && isBlocking) {
						fireShotBack();
						if (loc2.distance(npc.getEntity().getLocation()) <= 2) {
							((Damageable) npc.getEntity()).damage(4);
							show = false;
						}
					}
					if (checkBlock && !damaged && !isBlocking) {
						p.damage(12);
						show = false;
						damaged = true;
					}
				}
			}
		}
		if (t >= 320) {
			locReached = false;
			checkBlock = false;
			show = true;
			this.t = 0;
		}
	}

	public void fireShot() {
		loc.add(vec.normalize().multiply(0.8));
		if (show) {
			displayColoredParticle(loc, "E41B17", 0.0f, 0.0f, 0.0f);
			ParticleEffect.SMOKE.display(loc, 0.0f, 0.0f, 0.0f, 0.0f, 25);
		}
	}

	public void fireShotBack() {
		loc2.add(vec2.normalize().multiply(0.8));
		if (show) {
			displayColoredParticle(loc2, "E41B17", 0.0f, 0.0f, 0.0f);
			ParticleEffect.SMOKE.display(loc2, 0.0f, 0.0f, 0.0f, 0.0f, 25);
		}
	}

	// Run code when your trait is attached to a NPC.
	// This is called BEFORE onSpawn, so npc.getBukkitEntity() will return null
	// This would be a good place to load configurable defaults for new NPCs.
	@Override
	public void onAttach() {
		npc.data().set(NPC.DEFAULT_PROTECTED_METADATA, false);
		npc.data().set(NPC.DROPS_ITEMS_METADATA, false);
		npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, "z_kris_z");

	}

	// Run code when the NPC is despawned. This is called before the entity actually
	// despawns so npc.getBukkitEntity() is still valid.
	@Override
	public void onDespawn() {
	}

	// Run code when the NPC is spawned. Note that npc.getBukkitEntity() will be
	// null until this method is called.
	// This is called AFTER onAttach and AFTER Load when the server is started.
	@Override
	public void onSpawn() {
		npc.getNavigator().setTarget(map.getPlayer(), true);

	}

	// run code when the NPC is removed. Use this to tear down any repeating tasks.
	@Override
	public void onRemove() {
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
