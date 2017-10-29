package com.likeapig.missions.map;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class Mob {
	public static Mob instance;
    Evoker ev;
    public static List<NPC> npcs;
    Location loc;
    NPCRegistry registry;
    
    static {
        Mob.instance = new Mob();
    }
    
    public static Mob get() {
        return Mob.instance;
    }
    
    public List<NPC> getNpcs() {
        return Mob.npcs;
    }
    
    private Mob() {
        this.registry = CitizensAPI.getNPCRegistry();
        Mob.npcs = new ArrayList<NPC>();
    }
    
    public void spawnBoss(final Location loc) {
    }
    
    public void spawnRound1(final Location loc) {
        final NPC npc = this.registry.createNPC(EntityType.PLAYER, "Robocop");
        npc.addTrait(Mission.class);
        Mob.npcs.add(npc);
        npc.spawn(loc);
    }
}
