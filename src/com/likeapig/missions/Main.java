package com.likeapig.missions;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.likeapig.missions.commands.CommandsManager;
import com.likeapig.missions.map.Boss;
import com.likeapig.missions.map.Map;
import com.likeapig.missions.map.MapManager;
import com.likeapig.missions.map.MiniTrait;
import com.likeapig.missions.map.MissionTrait;
import com.likeapig.missions.map.RaidListener;
import com.likeapig.missions.models.LawnMower;
import com.likeapig.missions.models.Movement;

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
		//ProtocolLibrary.getProtocolManager().addPacketListener(
			//	new Movement(this, ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.STEER_VEHICLE }));
		CommandsManager.get().setup();
		RaidListener.get().setup();
		Settings.get().setup((Plugin) this);
		MapManager.get().setupMaps();
		CitizensAPI.getTraitFactory().deregisterTrait(TraitInfo.create(MissionTrait.class));
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(MissionTrait.class));
		CitizensAPI.getTraitFactory().deregisterTrait(TraitInfo.create(MiniTrait.class));
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(MiniTrait.class));
		new Boss();
		new LawnMower();
	}

	public void onDisable() {
		Map m = MapManager.get().getMap("test");
		if (m != null) {
			if (m.getStateName().equals("STARTED")) {
				m.kickPlayer(m.getPlayer());
			}
		}
		this.getLogger().info("Disabled!");
	}
}
