package com.likeapig.missions.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.likeapig.missions.Main;
import com.likeapig.missions.map.Boss;

public class Test extends Commands {
	public Test() {
		super("raid.admin", "Test", "", new String[] { "t" });
	}
	
	int i = 0;

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;

		Boss.get().Chair(p.getLocation(), p);
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			
			@Override
			public void run() {
				Boss.get().rotateRight();
			}
		}, 0L, 2L);
	}
}
