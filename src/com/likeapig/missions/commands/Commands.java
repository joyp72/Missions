package com.likeapig.missions.commands;

import org.bukkit.entity.*;
import net.md_5.bungee.api.*;

public abstract class Commands
{
    private String message;
    private String usage;
    private String permission;
    private String[] aliases;
    
    public Commands(final String permission, final String message, final String usage, final String... aliases) {
        this.permission = permission;
        this.message = message;
        this.usage = usage;
        this.aliases = aliases;
    }
    
    public void commandPreprocess(final Player sender, final String[] args) {
        if (sender.hasPermission(this.permission)) {
            this.onCommand(sender, args);
        }
        else {
            sender.sendMessage(ChatColor.RED + "Not enough permissions!");
        }
    }
    
    public abstract void onCommand(final Player p0, final String[] p1);
    
    public String getMessage() {
        return this.message;
    }
    
    public String getUsage() {
        return this.usage;
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    public String[] getAliases() {
        return this.aliases;
    }
    
    public boolean noIndex() {
        return false;
    }
}
