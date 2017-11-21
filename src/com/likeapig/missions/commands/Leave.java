package com.likeapig.missions.commands;

import org.bukkit.entity.*;
import java.util.*;

import com.likeapig.missions.commands.MessageManager.MessageType;
import com.likeapig.missions.map.*;
import com.likeapig.missions.map.Map;

public class Leave extends Commands
{
    public Leave() {
        super("raid.default", "Leave a map", "", new String[] { "l" });
    }
    
    @Override
    public void onCommand(final Player sender, final String[] args) {
        final Map m = MapManager.get().getMap(sender);
        if (m == null) {
        	MessageManager.get().message(sender, "You are not in a map.", MessageType.BAD);
        	return;
        }
        m.kickPlayer(sender);
    }
}
