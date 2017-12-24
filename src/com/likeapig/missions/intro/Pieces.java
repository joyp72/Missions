package com.likeapig.missions.intro;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class Pieces {
	public static Pieces instance;
	public NPCRegistry registry;
	private List<NPC> pieces;

	static {
		instance = new Pieces();
	}

	public Pieces() {
		registry = CitizensAPI.getNPCRegistry();
		pieces = new ArrayList<NPC>();
	}

	public static Pieces get() {
		return instance;
	}

	public List<NPC> getPieces() {
		return pieces;
	}
	
	public void spawnPiece(Location l) {
		NPC npc = registry.createNPC(EntityType.VILLAGER, "Bob");
		pieces.add(npc);
		npc.addTrait(IntroTrait.class);
		npc.spawn(l);
	}

}
