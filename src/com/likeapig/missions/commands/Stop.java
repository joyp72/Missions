package com.likeapig.missions.commands;

import org.bukkit.entity.*;
import com.likeapig.missions.map.*;

public class Stop extends Commands
{
    public Stop() {
        super("raid.admin", "Stop a game", "", new String[] { "" });
    }
    
    @Override
    public void onCommand(final Player sender, final String[] args) {
        final Map m = MapManager.get().getMap("test");
        if (!m.containsPlayer(sender)) {
            MessageManager.get().message(sender, "You are not in a map.", MessageManager.MessageType.BAD);
            return;
        }
        if (!m.isStarted()) {
            MessageManager.get().message(sender, "The game has not started yet.", MessageManager.MessageType.BAD);
            return;
        }
        m.stop();
        MessageManager.get().message(sender, "Raid ended.");
    }
}
