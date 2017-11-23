package com.likeapig.missions.commands;

import org.bukkit.entity.Player;

import com.likeapig.missions.map.Mob;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class Test extends Commands {
	public Test() {
		super("raid.admin", "Test", "", new String[] { "t" });
	}

	int i = 0;

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;

		if (i == 0) {
			Mob.get().Floor2Boss(p.getLocation().add(1, 0, 0));
			i++;
			return;
		} else {
			CitizensAPI.getNPCRegistry().deregister(Mob.get().getBoss(2));
			Mob.get().getBoss().clear();
			i = 0;
			return;
		}
	}
}
