package com.likeapig.missions.commands;

import org.bukkit.entity.*;
import com.likeapig.missions.map.*;

public class Start extends Commands
{
    public Start() {
        super("raid.default", "Start a game", "", new String[] { "s" });
    }
    
    @Override
    public void onCommand(final Player sender, final String[] args) {
        final Map m = MapManager.get().getMap(sender);
        if (m == null) {
            MessageManager.get().message(sender, "You are not in a map.", MessageManager.MessageType.BAD);
            return;
        }
        if (m.isStarted()) {
            MessageManager.get().message(sender, "The game has already been started.", MessageManager.MessageType.BAD);
            return;
        }
        m.start();
        MessageManager.get().message(sender, "You started the game!");
    }
}
