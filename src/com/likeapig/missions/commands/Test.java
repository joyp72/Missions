package com.likeapig.missions.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.likeapig.missions.Main;
import com.likeapig.missions.map.Mission;

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
    	npc.data().set(NPC.GLOWING_METADATA, true);
    	npc.data().set(NPC.DEFAULT_PROTECTED_METADATA, false);
    	
    	npc.getEntity().setInvulnerable(false);
    	
    	sender.sendMessage(npc.data().get(NPC.DEFAULT_PROTECTED_METADATA).toString());
    	
    	sender.sendMessage(Double.toString(((Damageable) npc.getEntity()).getHealth()));
    	
    	Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable() {
    		
    		@Override
    		public void run() {
    			npc.destroy();
    		}
    	}, 120L);
    	
    	
    }
}
