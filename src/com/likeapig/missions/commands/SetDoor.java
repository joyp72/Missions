package com.likeapig.missions.commands;

import org.bukkit.entity.*;
import org.bukkit.*;
import com.likeapig.missions.map.*;

public class SetDoor extends Commands
{
    public SetDoor() {
        super("raid.admin", "Set the door of a map", "<map>", new String[] { "sd" });
    }
    
    @Override
    public void onCommand(final Player sender, final String[] args) {
        final Location l = sender.getLocation();
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
        if (args[1].equalsIgnoreCase("1")) {
            m.setDoorLoc1(l);
            MessageManager.get().message(sender, "Door 1 set for: " + m.getName(), MessageManager.MessageType.GOOD);
        }
        if (args[1].equalsIgnoreCase("2")) {
            m.setDoorLoc2(l);
            MessageManager.get().message(sender, "Door 2 set for: " + m.getName(), MessageManager.MessageType.GOOD);
        }
        if (args[1].equalsIgnoreCase("3")) {
            m.setDoorLoc3(l);
            MessageManager.get().message(sender, "Door 3 set for: " + m.getName(), MessageManager.MessageType.GOOD);
        }
        if (args[1].equalsIgnoreCase("4")) {
            m.setDoorLoc4(l);
            MessageManager.get().message(sender, "Door 4 set for: " + m.getName(), MessageManager.MessageType.GOOD);
        }
    }
}
