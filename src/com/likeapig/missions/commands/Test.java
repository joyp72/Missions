package com.likeapig.missions.commands;

import org.bukkit.entity.Player;

import com.likeapig.missions.map.Final;

import de.Ste3et_C0st.FurnitureLib.main.FurnitureLib;

public class Test extends Commands {
	public Test() {
		super("raid.admin", "Test", "", new String[] { "t" });
	}

	int i = 0;
	public FurnitureLib ins = FurnitureLib.getInstance();

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;
		p.sendMessage(Integer.toString(Final.get().getRound()));
		p.sendMessage(Integer.toString(Final.get().getDestroyed().size()));
		p.sendMessage(Boolean.toString(Final.hit));
	}
}