package com.likeapig.missions.commands;

import com.likeapig.missions.*;
import com.likeapig.missions.menus.Menus;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import java.util.*;
import java.util.List;

public class CommandsManager implements CommandExecutor {
	private List<Commands> cmds;
	private static CommandsManager instance;
	public boolean gui;

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
		Main.get().getCommand("story").setExecutor((CommandExecutor) this);
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
		cmds.add(new Skip());
		cmds.add(new Edit());
		cmds.add(new SetFloor());
		cmds.add(new Glide());
		gui = true;
	}

	public boolean onCommand(final CommandSender sender, final Command cmd, final String arg2, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		final Player p = (Player) sender;
		if (!cmd.getName().equalsIgnoreCase("story")) {
			return true;
		}
		if (gui && args.length == 0) {
			Menus.resetInvs(p);
			new Menus(p);
			p.openInventory(Menus.getMenus().get(p));
		}
		return true;
	}
}
