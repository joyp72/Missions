package com.likeapig.missions.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;

import com.likeapig.missions.Main;
import com.likeapig.missions.map.Boss;
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
		p.sendMessage("reached1 = " + Final.reached1);
		p.sendMessage("round1 = " + Final.round1);
		p.sendMessage("hit = " + Final.hit);
		p.sendMessage("check = " + Final.check);
	}
}