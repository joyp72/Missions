package com.likeapig.missions.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.likeapig.missions.utils.ParticleEffect;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class Mob {
	public static Mob instance;
	Evoker ev;
	private List<NPC> round1;
	private List<NPC> round2;
	private List<NPC> round3;
	private HashMap<Integer, NPC> boss;
	Location loc;
	NPCRegistry registry;
	ItemStack bossStick;

	static {
		Mob.instance = new Mob();
	}

	public static Mob get() {
		return Mob.instance;
	}

	public List<NPC> getRound(int i) {
		if (i == 1) {
			return round1;
		} if (i == 2) {
			return round2;
		} if (i == 3) {
			return round3;
		} else {
			return null;
		}
	}

	public NPC getBoss(int i) {
		return boss.get(i);
	}

	public HashMap<Integer, NPC> getBoss() {
		return boss;
	}

	public void fireShot(Location loc) {
		Location l = loc.add(0.0, 1.0, 0.0);
		Vector direction = l.getDirection();
		l = l.add(direction.multiply(1));
		displayColoredParticle(l, "E41B17", 0.0f, 0.0f, 0.0f);
	}

	private Mob() {
		this.registry = CitizensAPI.getNPCRegistry();
		round1 = new ArrayList<NPC>();
		round2 = new ArrayList<NPC>();
		round3 = new ArrayList<NPC>();
		boss = new HashMap<Integer, NPC>();
		bossStick = new ItemStack(Material.STICK);
		{
			ItemMeta meta = bossStick.getItemMeta();
			meta.addItemFlags(ItemFlag.values());
			bossStick.setItemMeta(meta);
			bossStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
		}
	}

	public void Floor2Boss(Location loc) {
		NPC npc = registry.createNPC(EntityType.PLAYER, "T-2000");
		npc.addTrait(MiniTrait.class);
		npc.data().set(NPC.GLOWING_METADATA, true);
		boss.put(2, npc);
		npc.spawn(loc);
	}

	public void Floor1Boss(final Location loc) {
		NPC npc = registry.createNPC(EntityType.PLAYER, "Guard");
		npc.addTrait(MissionTrait.class);
		npc.data().set(NPC.GLOWING_METADATA, true);
		boss.put(1, npc);
		npc.spawn(loc);
		if (npc.isSpawned()) {
			Damageable entity = (Damageable) npc.getEntity();
			entity.setMaxHealth(100);
			entity.setHealth(100);
			((HumanEntity) npc.getEntity()).getInventory().setItemInMainHand(bossStick);
		}
	}
	
	public void spawnGuard(Location loc) {
		NPC npc = registry.createNPC(EntityType.PLAYER, "Guard");
		npc.addTrait(MissionTrait.class);
		round3.add(npc);
		npc.spawn(loc);
		if (npc.isSpawned()) {
			((HumanEntity) npc.getEntity()).getInventory().setItemInMainHand(bossStick);
		}
	}

	public void spawnRound1(final Location loc) {
		final NPC npc = this.registry.createNPC(EntityType.PLAYER, "Robot");
		npc.addTrait(MissionTrait.class);
		round1.add(npc);
		npc.spawn(loc);
	}

	public void spawnRound2(Location loc) {
		final NPC npc = this.registry.createNPC(EntityType.PLAYER, "Robot");
		npc.addTrait(MissionTrait.class);
		round2.add(npc);
		npc.spawn(loc);
	}

	public ItemStack getBossStick() {
		return bossStick;
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
