package com.likeapig.missions.map;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class Mob {
	public static Mob instance;
	Evoker ev;
	private List<NPC> round1;
	private List<NPC> round2;
	private List<NPC> boss1;
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
		} else if (i == 2) {
			return round2;
		} else {
			return null;
		}
	}
	
	public List<NPC> getBoss(int i) {
		if (i == 1) {
			return boss1;
		} else {
			return null;
		}
	}

	private Mob() {
		this.registry = CitizensAPI.getNPCRegistry();
		round1 = new ArrayList<NPC>();
		round2 = new ArrayList<NPC>();
		boss1 = new ArrayList<NPC>();
		bossStick = new ItemStack(Material.STICK);
		{
			ItemMeta meta = bossStick.getItemMeta();
			meta.addItemFlags(ItemFlag.values());
			bossStick.setItemMeta(meta);
			bossStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
		}
	}

	public void Floor1Boss(final Location loc) {

		NPC npc = registry.createNPC(EntityType.PLAYER, "Guard");
		npc.addTrait(MissionTrait.class);
		npc.data().set(NPC.GLOWING_METADATA, true);
		boss1.add(npc);
		npc.spawn(loc);
		if (npc.isSpawned()) {
			Damageable entity = (Damageable) npc.getEntity();
			entity.setMaxHealth(100);
			entity.setHealth(100);
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
}
