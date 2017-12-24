package com.likeapig.missions.intro;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.likeapig.missions.Main;

public class IntroListener implements Listener {
	public static IntroListener instance;

	static {
		instance = new IntroListener();
	}

	public static IntroListener get() {
		return instance;
	}

	public void setup() {
		Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.get());
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		Intro i = IntroManager.get().getIntro(p);
		if (i != null) {
			i.removePlayer(p);
		}
	}

}
