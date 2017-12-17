package com.likeapig.missions.intro;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.likeapig.missions.Settings;
import com.likeapig.missions.commands.MessageManager;
import com.likeapig.missions.utils.LocationUtils;

public class Intro {
	private String name;
	private IntroState state;
	static IntroData data;
	private Location spawn;

	public Intro(String s) {
		name = s;
		state = IntroState.STOPPED;
		loadFromConfig();
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
			Settings.get().set("intros" + getName() + ".spawn", LocationUtils.locationToString(spawn));
		}
	}

	public void loadFromConfig() {
		Settings s = Settings.get();
		if (s.get("intros." + getName() + ".spawn") != null) {
			String s2 = s.get("intros." + getName() + ".spawn");
			spawn = LocationUtils.stringToLocation(s2);
			spawn.setY(0);
		}
	}

	public void stop() {

	}

	public void addPlayer(Player p) {
		if (containsPlayer(p) && state.canJoin()) {
			data = new IntroData(p, this);
			message("You entered the Intro.");
		}
	}

	public void removePlayer(Player p) {
		if (containsPlayer(p)) {
			data.restore();
			data = null;
			p.sendmes
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
