package com.likeapig.missions;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcmonkey.sentinel.SentinelTrait;

import com.likeapig.missions.commands.CommandsManager;
import com.likeapig.missions.map.Boss;
import com.likeapig.missions.map.MapManager;
import com.likeapig.missions.map.MissionTrait;
import com.likeapig.missions.map.RaidListener;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

public class Main extends JavaPlugin {
	public static Main instance;

	public static Main get() {
		return Main.instance;
	}

	public void onEnable() {
		Main.instance = this;
		this.getLogger().info("Raid Enabled!");
		CommandsManager.get().setup();
		RaidListener.get().setup();
		Settings.get().setup((Plugin) this);
		MapManager.get().setupMaps();
		CitizensAPI.getTraitFactory().deregisterTrait(TraitInfo.create(MissionTrait.class));
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(MissionTrait.class));
		new Boss();
	}

	public void onDisable() {
		if (Boss.class != null) {
			Boss.get().removeChair();
		}
		this.getLogger().info("Disabled!");
	}
}
