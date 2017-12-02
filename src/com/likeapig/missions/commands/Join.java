package com.likeapig.missions.commands;

import org.bukkit.Material;
import org.bukkit.entity.*;

import com.likeapig.missions.commands.MessageManager.MessageType;
import com.likeapig.missions.map.*;

public class Join extends Commands
{
    public Join() {
        super("raid.default", "Join an arena", "<arena>", new String[] { "j" });
    }
    
    @Override
    public void onCommand(final Player sender, final String[] args) {
    	Player p = sender;
        final Map a = MapManager.get().getMap("test");
        if (p.getInventory().contains(Material.REDSTONE) || p.getInventory().contains(Material.REDSTONE_BLOCK)) {
        	MessageManager.get().message(p, "Redstone is not allowed inside the mission!", MessageType.BAD);
        	return;
        }
        if (p.getInventory().contains(Material.ENDER_PEARL)) {
        	MessageManager.get().message(p, "Ender Pearls is not allowed inside the mission!", MessageType.BAD);
        	return;
        }
        if (a.getStateName().equals("STARTED")) {
            MessageManager.get().message(sender, "The map is being used by another player.", MessageManager.MessageType.BAD);
            return;
        }
        final Map a2 = MapManager.get().getMap("test");
        if (a2.getStateName().equals("STOPPED")) {
            MessageManager.get().message(sender, String.valueOf(a2.getName()) + " doesn't have some loc set!", MessageManager.MessageType.BAD);
            return;
        }
        if (args.length == 0) {
            MapManager.get().getMap("test").addPlayer(sender);
        }
    }
}
