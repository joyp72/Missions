package com.likeapig.missions.intro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftStructureBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.likeapig.missions.Main;
import com.likeapig.missions.Settings;
import com.likeapig.missions.commands.MessageManager;
import com.likeapig.missions.utils.LocationUtils;

import main.RollbackAPI;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.md_5.bungee.api.ChatColor;

public class Intro {
	private NPCRegistry registry = CitizensAPI.getNPCRegistry();
	private String name;
	private IntroState state;
	static IntroData data;
	private Location spawn;
	private List<String> metadata;
	private HashMap<String, Location> paths;
	private HashMap<String, Location> levers;
	private int check;

	public Intro(String s) {
		name = s;
		state = IntroState.STOPPED;
		metadata = new ArrayList<String>();
		paths = new HashMap<String, Location>();
		levers = new HashMap<String, Location>();
		check = 0;
		loadFromConfig();
		if (spawn != null) {
			List<Location> locs = new ArrayList<Location>(
					RollbackAPI.getBlocksOfTypeInRegion(spawn.getWorld(), "intro", Material.STRUCTURE_BLOCK));
			for (Location l : locs) {
				CraftStructureBlock sb = (CraftStructureBlock) l.getBlock().getState();
				if (sb.getSnapshotNBT().getString("metadata").contains("path")) {
					metadata.add(sb.getSnapshotNBT().getString("metadata"));
					paths.put(sb.getSnapshotNBT().getString("metadata"), l.add(0, 2, 0));
				}
				if (sb.getSnapshotNBT().getString("metadata").contains("lever")) {
					levers.put(sb.getSnapshotNBT().getString("metadata"), l.add(0, 2, 0));
				}
			}
			Collections.sort(metadata);
		}
		checkState();
	}

	private void checkState() {
		boolean flag = false;
		if (spawn == null) {
			flag = true;
		}
		if (flag) {
			setState(IntroState.STOPPED);
			return;
		}
		setState(IntroState.WAITING);
	}

	public void saveToConfig() {
		if (spawn != null) {
			Settings.get().set("intros." + getName() + ".spawn", LocationUtils.locationToString(spawn));
		}
	}

	public void loadFromConfig() {
		Settings s = Settings.get();
		if (s.get("intros." + getName() + ".spawn") != null) {
			String s2 = s.get("intros." + getName() + ".spawn");
			spawn = LocationUtils.stringToLocation(s2);
			spawn.setYaw(0);
		}
	}

	public void handleInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Location loc = e.getClickedBlock().getLocation();
		Intro i = IntroManager.get().getIntro(p);
		if (i != null) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (levers.containsValue(loc)) {
					message("lever");
				}
			}
		}
	}

	public void handleMoveEvent(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Intro i = IntroManager.get().getIntro(p);
		if (i != null) {
			if (check == 0 && p.getLocation().distance(paths.get(metadata.get(0))) <= 2) {
				message("I remember... a lab...");
				check = 1;
			}
			if (check == 1 && p.getLocation().distance(paths.get(metadata.get(1))) <= 2) {
				message("I know it is in ruin now, but I remember how it used to be.");
				check = 2;
			}
			if (check == 2 && p.getLocation().distance(paths.get(metadata.get(2))) <= 2) {
				message("I was just an assistant, but soon,");
				check = 3;
			}
			if (check == 3 && p.getLocation().distance(paths.get(metadata.get(3))) <= 2) {
				message("A war came that threatend our very existance.");
				check = 4;
			}
			if (check == 4 && p.getLocation().distance(paths.get(metadata.get(4))) <= 2) {
				message("Our enemies had already launched a missle powerful enough to destroy the entire country...");
				check = 5;
			}
			if (check == 5 && p.getLocation().distance(paths.get(metadata.get(5))) <= 2) {
				message("There was only one thing we could do:");
				check = 6;
			}
			if (check == 6 && p.getLocation().distance(paths.get(metadata.get(6))) <= 2) {
				message(ChatColor.RED + "" + ChatColor.ITALIC + "Strike Them First.");
				check = 7;
			}
		}
	}

	public void start() {
		getPlayer().teleport(spawn);
		check = 0;
	}

	public void stop() {
		setState(IntroState.WAITING);
	}

	public void addPlayer(Player p) {
		if (!containsPlayer(p) && state.canJoin()) {
			data = new IntroData(p, this);
			message("You entered the Intro.");
			start();
		}
	}

	public void removePlayer(Player p) {
		if (containsPlayer(p)) {
			data.restore();
			data = null;
			MessageManager.get().message(p, "You quit the Intro.");
			stop();
		}
	}

	public void setSpawn(Location loc) {
		spawn = loc;
		saveToConfig();
		checkState();
	}

	public void message(String msg) {
		MessageManager.get().message(getPlayer(), msg);
	}

	public boolean isStarted() {
		return state.equals(IntroState.STARTED);
	}

	public Player getPlayer() {
		return data.getPlayer();
	}

	public void setState(IntroState s) {
		state = s;
	}

	public String getName() {
		return name;
	}

	public String getStateName() {
		return state.getName();
	}

	public boolean containsPlayer(Player p) {
		return data != null && data.getIntro() == this && data.getPlayer().equals(p);
	}

	public void onRemoved() {
		if (isStarted()) {
			stop();
		} else {
			removePlayer(getPlayer());
		}
		setState(IntroState.STOPPED);
	}

	public enum IntroState {
		WAITING("WAITING", 0, "WAITING", 0, "WAITING", 0, "WAITING", true), STARTING("STARTING", 1, "STARTING", 1,
				"STARTING", 1, "STARTING", false), STARTED("STARTED", 2, "STARTED", 2, "STARTED", 2, "STARTED",
						false), STOPPED("STOPPED", 3, "STOPPED", 3, "STOPPED", 3, "STOPPED", false);

		private boolean allowJoin;
		private String name;

		private IntroState(final String s3, final int n3, final String s2, final int n2, final String s, final int n,
				final String name, final Boolean allowJoin) {
			this.allowJoin = allowJoin;
			this.name = name;
		}

		public boolean canJoin() {
			return this.allowJoin;
		}

		public String getName() {
			return this.name;
		}
	}
}
