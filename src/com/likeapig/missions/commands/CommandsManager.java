package com.likeapig.missions.commands;

import com.likeapig.missions.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import java.util.*;
import java.util.List;

public class CommandsManager implements CommandExecutor
{
    private List<Commands> cmds;
    private static CommandsManager instance;
    
    static {
        CommandsManager.instance = new CommandsManager();
    }
    
    public static CommandsManager get() {
        return CommandsManager.instance;
    }
    
    private CommandsManager() {
        this.cmds = new ArrayList<Commands>();
    }
    
    public void setup() {
        Main.get().getCommand("ra").setExecutor((CommandExecutor)this);
        this.cmds.add(new Test());
        this.cmds.add(new Create());
        this.cmds.add(new Join());
        this.cmds.add(new SetSpawn());
        this.cmds.add(new SetBoss());
        this.cmds.add(new Leave());
        this.cmds.add(new Start());
        this.cmds.add(new Stop());
        this.cmds.add(new com.likeapig.missions.commands.List());
        this.cmds.add(new SetDoor());
        cmds.add(new SpawnBoss());
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        final Player p = (Player)sender;
        if (!cmd.getName().equalsIgnoreCase("ra")) {
            return true;
        }
        if (args.length == 0) {
            MessageManager.get().message(p, "Commands List:");
            for (final Commands c : this.cmds) {
                if (!c.noIndex()) {
                    MessageManager.get().message(p, "/ra " + c.getClass().getSimpleName().toLowerCase() + " " + c.getUsage());
                }
            }
            return true;
        }
        if (args.length != 0) {
            final Commands c = this.getCommand(args[0]);
            if (c != null) {
                final List<String> a = new ArrayList<String>(Arrays.asList(args));
                a.remove(0);
                args = a.toArray(new String[a.size()]);
                c.commandPreprocess(p, args);
            }
        }
        return true;
    }
    
    private Commands getCommand(final String name) {
        for (final Commands c : this.cmds) {
            if (c.getClass().getSimpleName().trim().equalsIgnoreCase(name.trim())) {
                return c;
            }
            String[] aliases;
            for (int length = (aliases = c.getAliases()).length, i = 0; i < length; ++i) {
                final String s = aliases[i];
                if (s.trim().equalsIgnoreCase(name.trim())) {
                    return c;
                }
            }
        }
        return null;
    }
}
