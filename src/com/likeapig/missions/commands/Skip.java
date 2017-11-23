package com.likeapig.missions.commands;

import org.bukkit.entity.Player;

import com.likeapig.missions.map.Map;
import com.likeapig.missions.map.MapManager;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class Skip extends Commands {
	public Skip() {
		super("raid.admin", "Skip", "", new String[] { "skp" });
	}

	NPCRegistry registry = CitizensAPI.getNPCRegistry();;

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;
		Map m = MapManager.get().getMap(p);
		if (m == null) {
			return;
		}
		if (m.getRound() == 1) {
			for (NPC npcs : m.getRoundNPC(1)) {
				registry.deregister(npcs);
			}
			m.getRoundNPC(1).clear();
			m.message("A guard has been deployed.");
			m.secondRound();
			return;
		}
		if (m.getRound() == 2) {
			for (NPC boss : m.getBoss().values()) {
				registry.deregister(boss);
			}
			m.getBoss().clear();
			for (NPC npcs : m.getRoundNPC(2)) {
				registry.deregister(npcs);
			}
			m.getRoundNPC(2).clear();
			m.getPlayer().getInventory().addItem(m.getCard(1));
			m.message("You picked up a Keycard!");
			m.setLocked(false);
			return;
		}

	}
}
