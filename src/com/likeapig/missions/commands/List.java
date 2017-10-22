package com.likeapig.missions.commands;

import org.bukkit.entity.*;
import com.likeapig.missions.map.*;
import com.likeapig.missions.map.Map;

import net.md_5.bungee.api.*;
import java.util.*;

public class List extends Commands
{
    public List() {
        super("elimination.default", "List all maps", "", new String[] { "" });
    }
    
    @Override
    public void onCommand(final Player sender, final String[] args) {
        if (MapManager.get().getMaps().size() > 0) {
            MessageManager.get().message(sender, "Map list: ");
            for (final Map m : MapManager.get().getMaps()) {
                MessageManager.get().message(sender, ChatColor.YELLOW + m.getName());
            }
        }
        else {
            MessageManager.get().message(sender, "No maps found", MessageManager.MessageType.BAD);
        }
    }
}
