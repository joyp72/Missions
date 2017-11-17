package com.likeapig.missions.commands;

import org.bukkit.entity.*;
import com.likeapig.missions.map.*;

public class SetSpawn extends Commands
{
    public SetSpawn() {
        super("raid.admin", "Set the spawn of a map", "<map>", new String[] { "sal" });
    }
    
    @Override
    public void onCommand(final Player sender, final String[] args) {
        if (args.length == 0) {
            MessageManager.get().message(sender, "You must specify a map!", MessageManager.MessageType.BAD);
            return;
        }
        final String id = args[0];
        final Map m = MapManager.get().getMap("test");
        m.setSpawn(sender.getLocation());
        MessageManager.get().message(sender, "Alpha spawn set for: " + m.getName(), MessageManager.MessageType.GOOD);
    }
}
