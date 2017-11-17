package com.likeapig.missions.map;

import org.bukkit.*;
import org.bukkit.ChatColor;

import com.likeapig.missions.*;
import com.likeapig.missions.utils.*;
import java.util.*;
import java.util.List;

import org.bukkit.entity.*;
import org.mcmonkey.sentinel.SentinelTarget;
import org.mcmonkey.sentinel.SentinelTrait;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPCRegistry;
import com.likeapig.missions.commands.*;

public class Map {
	private String name;
	static Data data;
	private int round = 1;
	private List<NPC> round1;
	private List<NPC> round2;
	private List<NPC> boss1;
	private List<Location> doors;
	private Location spawn;
	private Location bossLoc;
	private Location door1;
	private Location door2;
	private Location door3;
	private Location door4;
	private MapState state;
	NPCRegistry registry;

	public Map(final String s) {
		this.registry = CitizensAPI.getNPCRegistry();
		this.round1 = Mob.get().getRound(1);
		round2 = Mob.get().getRound(2);
		boss1 = Mob.get().getBoss(1);
		this.doors = new ArrayList<Location>();
		this.state = MapState.STOPPED;
		this.name = s;
		this.loadFromConfig();
		if (this.door1 != null && this.door2 != null && this.door3 != null && this.door4 != null) {
			this.doors.add(this.door1);
			this.doors.add(this.door2);
			this.doors.add(this.door3);
			this.doors.add(this.door4);
		}
		this.saveToConfig();
		this.checkState();
	}

	public void handleNPCDeath(NPCDeathEvent e) {
		NPC npc = e.getNPC();
		if (round1.contains(npc)) {
			round1.remove(npc);
			registry.deregister(npc);
		}
		if (round2.contains(npc)) {
			round2.remove(npc);
			registry.deregister(npc);
		}
		if (getRound() == 1 && round1.size() == 0) {
			getPlayer().sendMessage(ChatColor.GREEN + "Round 1 ended.");
			secondRound();
		}
		if (getRound() == 2 && round2.size() == 0) {
			getPlayer().sendMessage(ChatColor.GREEN + "Round 2 ended.");
		}
	}

	public void saveToConfig() {
		if (this.spawn != null) {
			Settings.get().set("maps." + this.getName() + ".spawn", LocationUtils.locationToString(this.spawn));
		}
		if (this.bossLoc != null) {
			Settings.get().set("maps." + this.getName() + ".bossLoc", LocationUtils.locationToString(this.bossLoc));
		}
		if (this.door1 != null) {
			Settings.get().set("maps." + this.getName() + ".door1", LocationUtils.locationToString(this.door1));
		}
		if (this.door2 != null) {
			Settings.get().set("maps." + this.getName() + ".door2", LocationUtils.locationToString(this.door2));
		}
		if (this.door3 != null) {
			Settings.get().set("maps." + this.getName() + ".door3", LocationUtils.locationToString(this.door3));
		}
		if (this.door4 != null) {
			Settings.get().set("maps." + this.getName() + ".door4", LocationUtils.locationToString(this.door4));
		}
	}

	public void loadFromConfig() {
		final Settings s = Settings.get();
		if (s.get("maps." + this.getName() + ".spawn") != null) {
			final String s2 = s.get("maps." + this.getName() + ".spawn");
			(this.spawn = LocationUtils.stringToLocation(s2)).setPitch(LocationUtils.stringToPitch(s2));
			this.spawn.setYaw(LocationUtils.stringToYaw(s2));
		}
		if (s.get("maps." + this.getName() + ".bossLoc") != null) {
			final String s3 = s.get("maps." + this.getName() + ".bossLoc");
			(this.bossLoc = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.bossLoc.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".door1") != null) {
			final String s3 = s.get("maps." + this.getName() + ".door1");
			(this.door1 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.door1.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".door2") != null) {
			final String s3 = s.get("maps." + this.getName() + ".door2");
			(this.door2 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.door2.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".door3") != null) {
			final String s3 = s.get("maps." + this.getName() + ".door3");
			(this.door3 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.door3.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".door4") != null) {
			final String s3 = s.get("maps." + this.getName() + ".door4");
			(this.door4 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.door4.setYaw(LocationUtils.stringToYaw(s3));
		}
	}

	public void onTimerTick(final String arg, final int timer) {
		arg.equalsIgnoreCase("endround");
	}

	public void onTimerEnd(final String arg) {
		if (arg.equalsIgnoreCase("round1end")) {
			this.stop();
		}
	}

	private void checkState() {
		boolean flag = false;
		if (this.spawn == null) {
			flag = true;
		}
		if (this.bossLoc == null) {
			flag = true;
		}
		if (this.door1 == null) {
			flag = true;
		}
		if (this.door2 == null) {
			flag = true;
		}
		if (this.door3 == null) {
			flag = true;
		}
		if (this.door4 == null) {
			flag = true;
		}
		if (flag) {
			this.setState(MapState.STOPPED);
			return;
		}
		this.setState(MapState.WAITING);
	}

	public void setState(final MapState a) {
		this.state = a;
	}

	public void start() {
		this.setState(MapState.STARTED);
		this.firstRound();
	}

	public void firstRound() {
		// Mob.get().spawnRound1(door1);
		// Mob.get().spawnRound1(door2);
		// Mob.get().spawnRound1(door3);
		// Mob.get().spawnRound1(door4);
		Mob.get().Round2Boss(bossLoc);

		// Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable() {
		// @Override
		// public void run() {
		// Mob.get().spawnRound1(door1);
		// Mob.get().spawnRound1(door2);
		// Mob.get().spawnRound1(door3);
		// Mob.get().spawnRound1(door4);
		// }
		// }, 40L);
		// Timer.get().createTimer(this.getMap(), "round1end",
		// 60).startTimer(this.getMap(), "round1end");
	}

	public void secondRound() {
		// setRound(2);
		// Mob.get().spawnRound2(door1);
		// Mob.get().spawnRound2(door2);
		// Mob.get().Round2Boss(bossLoc);
	}

	public int getRound() {
		return round;
	}

	public void setRound(int i) {
		round = i;
	}

	public void addPlayer(final Player p) {
		if (!this.containsPlayer(p) && this.state.canJoin()) {
			Map.data = new Data(p, this);
			p.teleport(this.spawn);
			this.message(ChatColor.GREEN + p.getName() + " joined the Mission!");
			if (Map.data != null) {
				this.start();
			}
		}
	}

	public void removePlayer(final Player p) {
		if (this.containsPlayer(p)) {
			Map.data.restore();
			Map.data = null;
			if (Map.data == null) {
				this.stop();
			}
		}
	}

	public void kickAll(final boolean b) {
		final Player p = getPlayer();
		if (b) {
			this.kickPlayer(p);
		} else {
			this.removePlayer(p);
		}
	}

	public void kickPlayer(final Player p) {
		this.kickPlayer(p, "", true);
	}

	public void kickPlayer(final Player p, final String message, final boolean showLeaveMessage) {
		if (message != "") {
			MessageManager.get().message(p, "Kicked for: " + message);
		}
		if (showLeaveMessage) {
			MessageManager.get().message(p, "You left the arena!", MessageManager.MessageType.BAD);
		}
		this.removePlayer(p);
	}

	public void stop() {
		Timer.get().stopTasks(this);
		this.setState(MapState.WAITING);
		for (final NPC NPCs : this.round1) {
			this.registry.deregister(NPCs);
		}
		round1.clear();
		for (final NPC NPCs : this.round2) {
			this.registry.deregister(NPCs);
		}
		this.round2.clear();
		for (NPC NPCs : boss1) {
			registry.deregister(NPCs);
		}
		boss1.clear();
		setRound(1);
	}

	public boolean isStarted() {
		return this.state.equals(MapState.STARTED);
	}

	public Location getSpawn() {
		return this.spawn;
	}

	public void setSpawn(final Location l) {
		this.spawn = l;
		this.checkState();
		this.saveToConfig();
	}

	public void setBossLoc(final Location l) {
		this.bossLoc = l;
		this.checkState();
		this.saveToConfig();
	}

	public void setDoorLoc1(final Location l) {
		this.door1 = l;
		this.checkState();
		this.saveToConfig();
	}

	public void setDoorLoc2(final Location l) {
		this.door2 = l;
		this.checkState();
		this.saveToConfig();
	}

	public void setDoorLoc3(final Location l) {
		this.door3 = l;
		this.checkState();
		this.saveToConfig();
	}

	public void setDoorLoc4(final Location l) {
		this.door4 = l;
		this.checkState();
		this.saveToConfig();
	}

	public Location getBossLoc() {
		return this.bossLoc;
	}

	public void message(final String message) {
		MessageManager.get().message(getPlayer(), message);
	}

	public Player getPlayer() {
		return Map.data.getPlayer();
	}

	public String getPNames() {
		return Map.data.getPlayer().getName();
	}

	public boolean containsPlayer(final Player p) {
		return Map.data != null && Map.data.getMap() == this && Map.data.getPlayer().equals(p);
	}

	public boolean containsNPC(NPC npc) {
		return round1.contains(npc) || round2.contains(npc) || boss1.contains(npc);
	}

	public List<NPC> getRound1() {
		return round1;
	}

	public String getName() {
		return this.name;
	}

	public Map getMap() {
		return this;
	}

	public String getStateName() {
		return this.state.getName();
	}

	public MapState getState() {
		return this.state;
	}

	public void onRemoved() {
		if (this.isStarted()) {
			this.stop();
		} else {
			this.removePlayer(getPlayer());
		}
		this.setState(MapState.STOPPED);
	}

	public enum MapState {
		WAITING("WAITING", 0, "WAITING", 0, "WAITING", 0, "WAITING", true), STARTING("STARTING", 1, "STARTING", 1,
				"STARTING", 1, "STARTING", true), STARTED("STARTED", 2, "STARTED", 2, "STARTED", 2, "STARTED",
						false), STOPPED("STOPPED", 3, "STOPPED", 3, "STOPPED", 3, "STOPPED", false);

		private boolean allowJoin;
		private String name;

		private MapState(final String s3, final int n3, final String s2, final int n2, final String s, final int n,
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
