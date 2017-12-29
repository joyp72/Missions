package com.likeapig.missions.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftStructureBlock;
import org.bukkit.entity.Player;

import com.likeapig.missions.map.MapManager;

import main.RollbackAPI;

public class Test extends Commands {
	public Test() {
		super("raid.admin", "Test", "", new String[] { "t" });
	}

	int i = 0;
	int id;
	int t;
	private List<Location> blocks;
	private List<String> s = new ArrayList<String>();

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;
		List<Location> locs = new ArrayList<Location>(
				RollbackAPI.getBlocksOfTypeInRegion(p.getWorld(), "intro", Material.STRUCTURE_BLOCK));
		p.sendMessage(Integer.toString(locs.size()));
	}
}