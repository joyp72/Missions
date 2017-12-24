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

import com.likeapig.missions.Main;
import com.likeapig.missions.Settings;
import com.likeapig.missions.commands.MessageManager;
import com.likeapig.missions.utils.LocationUtils;

import main.RollbackAPI;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class Intro {
	private NPCRegistry registry = CitizensAPI.getNPCRegistry();
	private String name;
	private IntroState state;
	static IntroData data;
	private Location spawn;
	private List<Location> spaces;
	private List<String> metadata;
	private HashMap<String, Location> hash;

	public Intro(String s) {
		name = s;
		state = IntroState.STOPPED;
		metadata = new ArrayList<String>();
		hash = new HashMap<String, Location>();
		loadFromConfig();
		if (spawn != null) {
			spaces = new ArrayList<Location>(
					RollbackAPI.getBlocksOfTypeInRegion(spawn.getWorld(), "intro", Material.STRUCTURE_BLOCK));
		}
		for (Location l : spaces) {
			CraftStructureBlock sb = (CraftStructureBlock) l.getBlock().getState();
			metadata.add(sb.getSnapshotNBT().getString("metadata"));
			hash.put(sb.getSnapshotNBT().getString("metadata"), l);
		}
		Collections.sort(metadata);
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

	public void start() {
		getPlayer().teleport(spawn);
		Pieces.get().spawnPiece(spawn);

		Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable() {
			@Override
			public void run() {
				Random r = new Random();
				int roll = r.nextInt(spaces.size());
				message("You rolled: " + Integer.toString(roll + 1));
				for (NPC npc : Pieces.get().getPieces()) {
					npc.getNavigator().setTarget(hash.get(metadata.get(roll)));
				}
			}
		}, 40L);
	}

	public void stop() {
		setState(IntroState.WAITING);
		for (NPC npc : Pieces.get().getPieces()) {
			registry.deregister(npc);
		}
		Pieces.get().getPieces().clear();
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

	public List<Location> getSpaces() {
		return spaces;
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
