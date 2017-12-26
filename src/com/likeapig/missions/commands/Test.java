package com.likeapig.missions.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftStructureBlock;
import org.bukkit.entity.Player;

import de.Ste3et_C0st.FurnitureLib.main.FurnitureLib;
import main.RollbackAPI;

public class Test extends Commands {
	public Test() {
		super("raid.admin", "Test", "", new String[] { "t" });
	}

	int i = 0;
	int id;
	int t;
	public FurnitureLib ins = FurnitureLib.getInstance();
	private List<Location> blocks;
	private List<String> s = new ArrayList<String>();

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;
		blocks = new ArrayList<Location>(
				RollbackAPI.getBlocksOfTypeInRegion(p.getWorld(), "intro", Material.STRUCTURE_BLOCK));
		for (Location loc : blocks) {
			Block b = loc.getBlock();
			CraftStructureBlock sb = (CraftStructureBlock) b.getState();
			if (sb.getSnapshotNBT().getString("metadata").contains("lever")) {
				s.add(sb.getSnapshotNBT().getString("metadata"));
			}
		}
		Collections.sort(s);
		for (String string : s) {
			p.sendMessage(string);
		}
		s.clear();
	}
}