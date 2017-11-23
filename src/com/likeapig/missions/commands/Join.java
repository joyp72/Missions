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
        final Map a = MapManager.get().getMap("test");
        if (a.containsPlayer(sender)) {
            MessageManager.get().message(sender, "You are already in a map", MessageManager.MessageType.BAD);
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
