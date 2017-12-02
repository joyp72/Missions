package com.likeapig.missions.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.likeapig.missions.Main;
import com.likeapig.missions.map.Boss;
import com.likeapig.missions.map.Final;
import com.likeapig.missions.map.Map;
import com.likeapig.missions.map.MapManager;

import de.Ste3et_C0st.FurnitureLib.main.FurnitureLib;
import net.apcat.simplesit.SimpleSit;
import net.apcat.simplesit.SimpleSitPlayer;

public class Test extends Commands {
	public Test() {
		super("raid.admin", "Test", "", new String[] { "t" });
	}

	int i = 0;
	int id;
	int t;
	public FurnitureLib ins = FurnitureLib.getInstance();

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;
		if (i == 0) {
			i++;
			Boss.get().Chair(p.getLocation());
			id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
				@Override
				public void run() {
					t++;
					if (t >= 24) {
						t = 0;
					}
					Boss.get().hover(t);
				}
			}, 0L, 0L);
		} else {
			Bukkit.getServer().getScheduler().cancelTask(id);
			Boss.get().removeChair();
			i = 0;
		}
	}
}