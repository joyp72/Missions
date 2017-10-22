package com.likeapig.missions.commands;

import org.bukkit.entity.*;
import com.likeapig.missions.map.*;

public class Join extends Commands
{
    public Join() {
        super("raid.default", "Join an arena", "<arena>", new String[] { "j" });
    }
    
    @Override
    public void onCommand(final Player sender, final String[] args) {
        if (args.length == 0) {
            MessageManager.get().message(sender, "You must specify a map name!", MessageManager.MessageType.BAD);
            return;
        }
        final Map a = MapManager.get().getMap(sender);
        if (a != null) {
            MessageManager.get().message(sender, "You are already in a map", MessageManager.MessageType.BAD);
            return;
        }
        final String id = args[0];
        final Map a2 = MapManager.get().getMap(id);
        if (a2 == null) {
            MessageManager.get().message(sender, "Unknown map", MessageManager.MessageType.BAD);
            return;
        }
        if (a2.getSpawn() == null) {
            MessageManager.get().message(sender, String.valueOf(a2.getName()) + " doesn't have a spawn set!", MessageManager.MessageType.BAD);
            return;
        }
        if (a2.getBossLoc() == null) {
            MessageManager.get().message(sender, String.valueOf(a2.getName()) + " doesn't have a Boss set!", MessageManager.MessageType.BAD);
            return;
        }
        a2.addPlayer(sender);
    }
}
