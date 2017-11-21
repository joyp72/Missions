package com.likeapig.missions.map;

import org.bukkit.*;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;

import com.likeapig.missions.*;
import com.likeapig.missions.utils.*;
import java.util.*;
import java.util.List;

import org.bukkit.entity.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mcmonkey.sentinel.SentinelTarget;
import org.mcmonkey.sentinel.SentinelTrait;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPCRegistry;
import com.likeapig.missions.commands.*;
import com.likeapig.missions.commands.MessageManager.MessageType;

public class Map {
	private String name;
	static Data data;
	private int round = 1;
	private List<NPC> round1;
	private List<NPC> round2;
	private List<NPC> boss1;
	private List<Location> doors;
	private List<Location> button2s;
	private Location spawn;
	private Location bossLoc;
	private Location door1;
	private Location door2;
	private Location door3;
	private Location door4;
	private MapState state;
	private ItemStack card1;
	private Location button2;
	private Location floor2;
	NPCRegistry registry;

	public Map(final String s) {
		this.registry = CitizensAPI.getNPCRegistry();
		this.round1 = Mob.get().getRound(1);
		round2 = Mob.get().getRound(2);
		boss1 = Mob.get().getBoss(1);
		this.doors = new ArrayList<Location>();
		button2s = new ArrayList<Location>();
		this.state = MapState.STOPPED;
		this.name = s;
		card1 = new ItemStack(Material.PAPER);
		{
			ItemMeta meta = card1.getItemMeta();
			meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Keycard");
			ArrayList<String> lore = new ArrayList<>();
			lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "(Mission Item)");
			lore.add("");
			lore.add(ChatColor.GRAY + "Used to access 2nd Floor.");
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.values());
			card1.setItemMeta(meta);
			card1.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
		}
		this.loadFromConfig();
		if (this.door1 != null && this.door2 != null && this.door3 != null && this.door4 != null) {
			this.doors.add(this.door1);
			this.doors.add(this.door2);
			this.doors.add(this.door3);
			this.doors.add(this.door4);
		}
		if (button2 != null) {
			button2s.add(button2);
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
		if (boss1.contains(npc)) {
			boss1.remove(npc);
			registry.deregister(npc);
			getPlayer().getInventory().addItem(card1);
			message("You picked up a Keycard!");
		}
		if (getRound() == 1 && round1.size() == 0) {
			message("A guard has been deployed.");
			secondRound();
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
		if (button2 != null) {
			Settings.get().set("maps." + this.getName() + ".button2", LocationUtils.locationToString(button2));
		}
		if (floor2 != null) {
			Settings.get().set("maps." + this.getName() + ".floor2", LocationUtils.locationToString(floor2));
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
		if (s.get("maps." + this.getName() + ".button2") != null) {
			final String s3 = s.get("maps." + this.getName() + ".button2");
			(this.button2 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.button2.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".floor2") != null) {
			final String s3 = s.get("maps." + this.getName() + ".floor2");
			(this.floor2 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.floor2.setYaw(LocationUtils.stringToYaw(s3));
		}
	}

	public void onTimerTick(final String arg, final int timer) {
	}

	public void onTimerEnd(final String arg) {
		if (arg.equalsIgnoreCase("round1end")) {
		}
	}

	private void checkState() {
		boolean flag = false;
		if (this.spawn == null) {
			flag = true;
		}
		if (button2 == null) {
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
		if (floor2 == null) {
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
		Mob.get().spawnRound1(door1);
		Mob.get().spawnRound1(door2);
		Mob.get().spawnRound1(door3);
		Mob.get().spawnRound1(door4);

		Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable() {
			@Override
			public void run() {
				Mob.get().spawnRound1(door1);
				Mob.get().spawnRound1(door2);
				Mob.get().spawnRound1(door3);
				Mob.get().spawnRound1(door4);
			}
		}, 40L);
		// Timer.get().createTimer(this.getMap(), "round1end",
		// 60).startTimer(this.getMap(), "round1end");
	}

	public void secondRound() {
		setRound(2);
		Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable() {

			@Override
			public void run() {
				Mob.get().spawnRound2(door1);
				Mob.get().spawnRound2(door2);
				Mob.get().Floor1Boss(bossLoc);
			}
		}, 2);
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
			if (p.getInventory().contains(card1)) {
				p.getInventory().remove(card1);
			}
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
			MessageManager.get().message(p, message, MessageType.BAD);
		}
		if (showLeaveMessage) {
			MessageManager.get().message(p, "You left the mission!", MessageManager.MessageType.BAD);
		}
		this.removePlayer(p);
	}

	public void stop() {
		// Timer.get().stopTasks(this);
		this.setState(MapState.WAITING);
		Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable() {

			@Override
			public void run() {
				for (final NPC NPCs : round1) {
					registry.deregister(NPCs);
				}
				round1.clear();
				for (final NPC NPCs : round2) {
					registry.deregister(NPCs);
				}
				round2.clear();
				for (NPC NPCs : boss1) {
					registry.deregister(NPCs);
				}
				boss1.clear();
				setRound(1);
			}
		}, 1);
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

	public void setButton(int i, Location l) {
		if (i == 2) {
			button2 = l;
			saveToConfig();
			checkState();
		}
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
	
	public Location getButtonLoc(int i) {
		if (i == 2) {
			return button2;
		} else {
			return null;
		}
	}
	
	public void setFloor(int i, Location l) {
		if (i == 2) {
			floor2 = l;
			saveToConfig();
			checkState();
		}
	}
	
	public Location getFloor(int i) {
		if (i == 2) {
			return floor2;
		} else {
			return null;
		}
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

	public List<NPC> getRoundNPC(int i) {
		if (i == 1) {
			return round1;
		} else if (i == 2) {
			return round2;
		} else {
			return null;
		}
	}

	public List<NPC> getBoss(int i) {
		if (i == 1) {
			return boss1;
		} else {
			return null;
		}
	}

	public ItemStack getCard(int i) {
		if (i == 1) {
			return card1;
		} else {
			return null;
		}
	}

	public String getName() {
		return this.name;
	}
	
	public List<Location> getButtons(int i) {
		if (i == 2) {
			return button2s;
		} else {
			return null;
		}
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
