package com.likeapig.missions;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.likeapig.missions.commands.CommandsManager;
import com.likeapig.missions.intro.Intro;
import com.likeapig.missions.intro.IntroListener;
import com.likeapig.missions.intro.IntroManager;
import com.likeapig.missions.map.Boss;
import com.likeapig.missions.map.Map;
import com.likeapig.missions.map.MapManager;
import com.likeapig.missions.map.MiniTrait;
import com.likeapig.missions.map.MissionTrait;
import com.likeapig.missions.map.RaidListener;
import com.likeapig.missions.menus.MenusListener;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

public class Main extends JavaPlugin {
	public static Main instance;

	public static Main get() {
		return Main.instance;
	}

	public void onEnable() {
		Main.instance = this;
		this.getLogger().info("Mission Enabled!");
		// ProtocolLibrary.getProtocolManager().addPacketListener(
		// new Movement(this, ListenerPriority.NORMAL, new PacketType[] {
		// PacketType.Play.Client.STEER_VEHICLE }));
		CommandsManager.get().setup();
		RaidListener.get().setup();
		IntroListener.get().setup();
		MenusListener.get().setup();
		Settings.get().setup((Plugin) this);
		MapManager.get().setupMaps();
		IntroManager.get().setupIntros();
		CitizensAPI.getTraitFactory().deregisterTrait(TraitInfo.create(MissionTrait.class));
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(MissionTrait.class));
		CitizensAPI.getTraitFactory().deregisterTrait(TraitInfo.create(MiniTrait.class));
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(MiniTrait.class));
		new Boss();
	}

	public void onDisable() {
		for (Map m : MapManager.get().getMaps()) {
			if (m.getStateName().equals("STARTED")) {
				m.kickPlayer(m.getPlayer());
			}
		}
		for (Intro i : IntroManager.get().getIntros()) {
			if (i.getStateName().equals("STARTED")) {
				i.removePlayer(i.getPlayer());
			}
		}
		this.getLogger().info("Disabled!");
	}
}
