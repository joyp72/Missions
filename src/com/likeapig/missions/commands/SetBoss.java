package com.likeapig.missions.commands;

import org.bukkit.entity.*;

import com.likeapig.missions.map.Map;
import com.likeapig.missions.map.MapManager;

public class SetBoss extends Commands
{
    public SetBoss() {
        super("raid.admin", "Set the Boss spawn of a map", "<map>", new String[] { "sal" });
    }
    
    @Override
    public void onCommand(final Player sender, final String[] args) {
        if (args.length == 0) {
            MessageManager.get().message(sender, "You must specify a map!", MessageManager.MessageType.BAD);
            return;
        }
        final String id = args[0];
        final Map m = MapManager.get().getMap(id);
        if (m == null) {
            MessageManager.get().message(sender, "Unknown map.", MessageManager.MessageType.BAD);
            return;
        }
        m.setBossLoc(sender.getLocation());
        MessageManager.get().message(sender, "Boss Loc set for: " + m.getName(), MessageManager.MessageType.GOOD);
    }
}
