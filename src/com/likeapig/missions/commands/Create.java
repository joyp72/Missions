package com.likeapig.missions.commands;

import org.bukkit.entity.*;

import com.likeapig.missions.map.MapManager;
import com.likeapig.missions.map.*;

public class Create extends Commands
{
    public Create() {
        super("raid.admin", "Create a Map", "<name>", new String[] { "c", "createmap" });
    }
    
    @Override
    public void onCommand(final Player sender, final String[] args) {
        if (args.length == 0) {
            MessageManager.get().message(sender, "You must specify a map name.", MessageManager.MessageType.BAD);
            return;
        }
        final String id = args[0];
        final Map m = MapManager.get().getMap(id);
        if (m != null) {
            MessageManager.get().message(sender, "That map already exists.", MessageManager.MessageType.BAD);
            return;
        }
        MapManager.get().registerMap(id);
        MessageManager.get().message(sender, "Map created!", MessageManager.MessageType.GOOD);
    }
}
