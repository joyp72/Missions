package com.likeapig.missions.commands;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class Test extends Commands
{
    public Test() {
        super("raid.admin", "Test", "", new String[] { "t" });
    }
    
    @Override
    public void onCommand(final Player sender, final String[] args) {
        
    	NPCRegistry registry = CitizensAPI.getNPCRegistry();
    	NPC npc = registry.createNPC(EntityType.PLAYER, "Robocop");
    	npc.spawn(sender.getLocation());
    	
    	
    }
}
