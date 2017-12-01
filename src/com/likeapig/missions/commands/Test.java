package com.likeapig.missions.commands;

import org.bukkit.entity.Player;

import com.likeapig.missions.map.Final;

import de.Ste3et_C0st.FurnitureLib.main.FurnitureLib;
import net.apcat.simplesit.SimpleSit;
import net.apcat.simplesit.SimpleSitPlayer;

public class Test extends Commands {
	public Test() {
		super("raid.admin", "Test", "", new String[] { "t" });
	}

	int i = 0;
	public FurnitureLib ins = FurnitureLib.getInstance();

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;
		SimpleSitPlayer sp = new SimpleSitPlayer(p);
		sp.setLaying(true);
	}
}