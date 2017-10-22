package com.likeapig.missions.commands;

import org.bukkit.entity.*;
import com.likeapig.missions.map.*;

public class Test extends Commands
{
    public Test() {
        super("raid.admin", "Test", "", new String[] { "t" });
    }
    
    @Override
    public void onCommand(final Player sender, final String[] args) {
        Mob.get().spawnBoss(sender.getLocation());
    }
}
