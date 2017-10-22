package com.likeapig.missions.map;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class Mob {
	public static Mob instance;
	Evoker ev;
	public List<NPC> npcs;
	Location loc;
	NPCRegistry registry = CitizensAPI.getNPCRegistry();

	static {
		Mob.instance = new Mob();
	}

	public static Mob get() {
		return Mob.instance;
	}

	public List<NPC> getNpcs() {
		return npcs;
	}

	private Mob() {
		npcs = new ArrayList<NPC>();
	}

	public void spawnBoss(final Location loc) {
	}

	public void spawnRound1(final Location loc) {

		NPC npc = registry.createNPC(EntityType.PLAYER, "Robocop");

		npcs.add(npc);
		for (NPC NPC : npcs) {
			NPC.spawn(loc);
		}
	}
}
