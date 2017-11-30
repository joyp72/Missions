package com.likeapig.missions.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;
import org.bukkit.util.Vector;

import com.likeapig.missions.Main;
import com.likeapig.missions.Settings;
import com.likeapig.missions.commands.MessageManager;
import com.likeapig.missions.commands.MessageManager.MessageType;
import com.likeapig.missions.utils.LocationUtils;
import com.likeapig.missions.utils.ParticleEffect;
import com.likeapig.missions.utils.Titles;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class Map {
	private String name;
	static Data data;
	private int round;
	private int floor;
	private List<NPC> round1;
	private List<NPC> round2;
	private List<NPC> round3;
	private List<NPC> round4;
	private HashMap<Integer, NPC> boss;
	private List<Location> doors;
	private List<Location> b1s;
	private List<Location> b2s;
	private List<Location> b3s;
	private List<Location> b4s;
	private Location spawn;
	private Location bossLoc;
	private Location bossLoc2;
	private Location bossLoc3;
	private Location bossLoc4;
	private Location door1;
	private Location door2;
	private Location door3;
	private Location door4;
	private MapState state;
	private ItemStack card1;
	private ItemStack card2;
	private ItemStack card3;
	private Location b3f1;
	private Location b1f1;
	private Location b2f1;
	private Location b2f2;
	private Location b4f1;
	private Location floor4;
	private Location floor3;
	private Location floor2;
	private Location floor1;
	private Location chest1;
	private Location chest2;
	private Location chest3;
	private Location chest4;
	private Location chest5;
	private Location head1;
	private Location head2;
	private Location head3;
	private Location head4;
	private Location console1;
	private Location console2;
	private Location console3;
	private Location console4;
	private List<Location> consoles;
	private List<Location> heads;
	private Location rs;
	private List<Location> chests;
	private List<Location> tempchests;
	private List<String> fifteen;
	private List<String> ten;
	private List<String> five;
	private List<String> twentyfive;
	private List<String> fifty;
	private List<String> seventyfive;
	private List<String> eighty;
	private boolean locked;
	private boolean first;
	private boolean second;
	private boolean third;
	private int i;
	private int id = 0;
	private int t = 0;
	private int t2 = 0;
	private int t3 = 0;
	private int t4 = 0;
	private HashMap<Location, Block> backup;
	NPCRegistry registry;

	public Map(final String s) {
		this.registry = CitizensAPI.getNPCRegistry();
		this.round1 = Mob.get().getRound(1);
		round2 = Mob.get().getRound(2);
		round3 = Mob.get().getRound(3);
		round4 = Mob.get().getRound(4);
		boss = Mob.get().getBoss();
		this.doors = new ArrayList<Location>();
		b2s = new ArrayList<Location>();
		b3s = new ArrayList<Location>();
		b4s = new ArrayList<Location>();
		b1s = new ArrayList<Location>();
		chests = new ArrayList<Location>();
		tempchests = new ArrayList<Location>();
		fifteen = new ArrayList<String>();
		ten = new ArrayList<String>();
		five = new ArrayList<String>();
		twentyfive = new ArrayList<String>();
		fifty = new ArrayList<String>();
		seventyfive = new ArrayList<String>();
		eighty = new ArrayList<String>();
		heads = new ArrayList<Location>();
		consoles = new ArrayList<Location>();
		backup = new HashMap<Location, Block>();
		fifteen.add(Material.IRON_INGOT.toString());
		fifteen.add(Material.IRON_HELMET.toString());
		fifteen.add(Material.IRON_CHESTPLATE.toString());
		ten.add(Material.AIR.toString());
		five.add(Material.AIR.toString());
		twentyfive.add(Material.AIR.toString());
		fifty.add(Material.AIR.toString());
		seventyfive.add(Material.AIR.toString());
		eighty.add(Material.AIR.toString());
		this.state = MapState.STOPPED;
		this.name = s;
		first = true;
		second = true;
		third = false;
		locked = false;
		floor = 1;
		round = 1;
		i = 1;
		card1 = new ItemStack(Material.PAPER);
		{
			ItemMeta meta = card1.getItemMeta();
			meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Floor 2 Keycard");
			ArrayList<String> lore = new ArrayList<>();
			lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "(Mission Item)");
			lore.add("");
			lore.add(ChatColor.GRAY + "Used to access 2nd floor.");
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.values());
			card1.setItemMeta(meta);
			card1.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
		}
		card2 = new ItemStack(Material.PAPER);
		{
			ItemMeta meta = card2.getItemMeta();
			meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Floor 3 Keycard");
			ArrayList<String> lore = new ArrayList<>();
			lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "(Mission Item)");
			lore.add("");
			lore.add(ChatColor.GRAY + "Used to access 3rd floor.");
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.values());
			card2.setItemMeta(meta);
			card2.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
		}
		card3 = new ItemStack(Material.PAPER);
		{
			ItemMeta meta = card3.getItemMeta();
			meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Storage Keycard");
			ArrayList<String> lore = new ArrayList<>();
			lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "(Mission Item)");
			lore.add("");
			lore.add(ChatColor.GRAY + "Used to access storage room on 1st floor.");
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.values());
			card3.setItemMeta(meta);
			card3.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
		}
		loadLoot();
		loadFromiConfig();
		if (this.door1 != null && this.door2 != null && this.door3 != null && this.door4 != null) {
			this.doors.add(this.door1);
			this.doors.add(this.door2);
			this.doors.add(this.door3);
			this.doors.add(this.door4);
		}
		if (b2f1 != null) {
			b2s.add(b2f1);
		}
		if (b2f2 != null) {
			b2s.add(b2f2);
		}
		if (b1f1 != null) {
			b1s.add(b1f1);
		}
		if (b3f1 != null) {
			b3s.add(b3f1);
		}
		if (b4f1 != null) {
			b4s.add(b4f1);
		}
		if (chest1 != null && chest2 != null && chest3 != null && chest4 != null && chest5 != null) {
			chests.add(chest1);
			chests.add(chest2);
			chests.add(chest3);
			chests.add(chest4);
			chests.add(chest5);
		}
		if (head1 != null && head2 != null && head3 != null && head4 != null) {
			heads.add(head1);
			heads.add(head2);
			heads.add(head3);
			heads.add(head4);
		}
		if (console1 != null && console2 != null && console3 != null && console4 != null) {
			consoles.add(console1);
			consoles.add(console2);
			consoles.add(console3);
			consoles.add(console4);
		}
		saveToConfig();
		this.checkState();
	}

	public void loadChests() {
		Random r = new Random();
		Random r2 = new Random();
		Random r3 = new Random();
		Random r4 = new Random();
		Random r5 = new Random();
		int ch = r2.nextInt(100) + 1;
		int slot = r3.nextInt(26);
		String s = Material.AIR.toString();
		String s2 = Material.AIR.toString();
		if (ch <= 5) {
			s = five.get(r4.nextInt(five.size()));
			s2 = five.get(r5.nextInt(five.size()));
		} else if (ch <= 10) {
			s = ten.get(r4.nextInt(ten.size()));
			s2 = ten.get(r5.nextInt(ten.size()));
		} else if (ch <= 15) {
			s = fifteen.get(r4.nextInt(fifteen.size()));
			s2 = fifteen.get(r5.nextInt(fifteen.size()));
		} else if (ch <= 25) {
			s = twentyfive.get(r4.nextInt(twentyfive.size()));
			s2 = twentyfive.get(r5.nextInt(twentyfive.size()));
		} else if (ch <= 50) {
			s = fifty.get(r4.nextInt(fifty.size()));
			s2 = fifty.get(r5.nextInt(fifty.size()));
		} else if (ch <= 75) {
			s = seventyfive.get(r4.nextInt(seventyfive.size()));
			s2 = seventyfive.get(r5.nextInt(seventyfive.size()));
		} else if (ch <= 80) {
			s = eighty.get(r4.nextInt(eighty.size()));
			s2 = eighty.get(r5.nextInt(eighty.size()));
		}
		Location loc = chests.get(r.nextInt(chests.size()));
		if (!tempchests.contains(loc) && i == 1) {
			Block b = loc.getBlock();
			Chest c = (Chest) b.getState();
			Inventory ci = c.getInventory();
			ci.setItem(slot, new ItemStack(Material.getMaterial(s.toUpperCase())));
			tempchests.add(loc);
			chests.remove(loc);
			i++;
		}
		if (!tempchests.contains(loc) && i == 2) {
			Block b = loc.getBlock();
			Chest c = (Chest) b.getState();
			Inventory ci = c.getInventory();
			ci.setItem(slot, new ItemStack(Material.getMaterial(s.toUpperCase())));
			ci.setItem(23, new ItemStack(Material.getMaterial(s2.toUpperCase())));
			tempchests.add(loc);
			chests.remove(loc);
			i++;
		}
		if (!tempchests.contains(loc) && i == 3) {
			Block b = loc.getBlock();
			Chest c = (Chest) b.getState();
			Inventory ci = c.getInventory();
			ci.setItem(16, new ItemStack(Material.getMaterial(s.toUpperCase())));
			ci.setItem(slot, new ItemStack(Material.getMaterial(s2.toUpperCase())));
			tempchests.add(loc);
			chests.remove(loc);
			i++;
		}
		if (!tempchests.contains(loc) && i == 4) {
			Block b = loc.getBlock();
			Chest c = (Chest) b.getState();
			Inventory ci = c.getInventory();
			ci.setItem(3, new ItemStack(Material.REDSTONE));
			ci.setItem(slot, new ItemStack(Material.getMaterial(s.toUpperCase())));
			tempchests.add(loc);
			chests.remove(loc);
			i++;
		}
		if (!tempchests.contains(loc) && i == 5) {
			Block b = loc.getBlock();
			Chest c = (Chest) b.getState();
			Inventory ci = c.getInventory();
			ci.setItem(5, new ItemStack(Material.getMaterial(s.toUpperCase())));
			ci.setItem(slot, new ItemStack(Material.getMaterial(s2.toUpperCase())));
			tempchests.add(loc);
			chests.remove(loc);
			i++;
		}
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
		if (round3.contains(npc)) {
			round3.remove(npc);
			registry.deregister(npc);
		}
		if (round4.contains(npc)) {
			round4.remove(npc);
			registry.deregister(npc);
		}
		if (boss.containsValue(npc)) {
			if (getBoss().get(2) != null) {
				if (getBoss().get(2).equals(npc)) {
					boss.clear();
					registry.deregister(npc);
					getPlayer().getInventory().addItem(card2);
					message("You picked up a new Keycard!");
					setLocked(false);
				}
			}
			if (getBoss().get(1) != null) {
				if (getBoss().get(1).equals(npc)) {
					boss.clear();
					registry.deregister(npc);
					getPlayer().getInventory().addItem(card1);
					message("You picked up a Keycard!");
					setLocked(false);
				}
			}
			if (getBoss().get(3) != null) {
				if (getBoss().get(3).equals(npc)) {
					boss.clear();
					registry.deregister(npc);
					getPlayer().getInventory().addItem(card3);
					message("You picked up a Storage Keycard!");
					setLocked(false);
				}
			}
		}
		if (getRound() == 1 && round1.size() == 0) {
			message("A guard has been deployed.");
			secondRound();
		}
		if (getRound() == 5 && round1.size() == 0) {
			Final.get().setRoundOver(1);
		}
	}

	public void loadLoot() {
		final Settings s = Settings.get();
		if (s.getLoot("loot." + getName() + ".15") != null) {
			fifteen = s.getLoot("loot." + getName() + ".15");
		}
		if (s.getLoot("loot." + getName() + ".10") != null) {
			ten = s.getLoot("loot." + getName() + ".10");
		}
		if (s.getLoot("loot." + getName() + ".5") != null) {
			five = s.getLoot("loot." + getName() + ".5");
		}
		if (s.getLoot("loot." + getName() + ".25") != null) {
			twentyfive = s.getLoot("loot." + getName() + ".25");
		}
		if (s.getLoot("loot." + getName() + ".50") != null) {
			fifty = s.getLoot("loot." + getName() + ".50");
		}
		if (s.getLoot("loot." + getName() + ".75") != null) {
			seventyfive = s.getLoot("loot." + getName() + ".75");
		}
		if (s.getLoot("loot." + getName() + ".80") != null) {
			eighty = s.getLoot("loot." + getName() + ".80");
		}
	}

	public void saveLoot() {
		Settings.get().setLoot("loot." + getName() + ".15", fifteen);
		Settings.get().setLoot("loot." + getName() + ".10", ten);
		Settings.get().setLoot("loot." + getName() + ".5", five);
		Settings.get().setLoot("loot." + getName() + ".25", twentyfive);
		Settings.get().setLoot("loot." + getName() + ".50", fifty);
		Settings.get().setLoot("loot." + getName() + ".75", seventyfive);
		Settings.get().setLoot("loot." + getName() + ".80", eighty);
	}

	public void saveToConfig() {
		if (this.spawn != null) {
			Settings.get().set("maps." + this.getName() + ".spawn", LocationUtils.locationToString(this.spawn));
		}
		if (this.bossLoc != null) {
			Settings.get().set("maps." + this.getName() + ".bossLoc", LocationUtils.locationToString(this.bossLoc));
		}
		if (this.bossLoc2 != null) {
			Settings.get().set("maps." + this.getName() + ".bossLoc2", LocationUtils.locationToString(this.bossLoc2));
		}
		if (this.bossLoc3 != null) {
			Settings.get().set("maps." + this.getName() + ".bossLoc3", LocationUtils.locationToString(this.bossLoc3));
		}
		if (this.bossLoc4 != null) {
			Settings.get().set("maps." + this.getName() + ".bossLoc4", LocationUtils.locationToString(this.bossLoc4));
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
		if (b2f1 != null) {
			Settings.get().set("maps." + this.getName() + ".b2f1", LocationUtils.locationToString(b2f1));
		}
		if (b2f2 != null) {
			Settings.get().set("maps." + this.getName() + ".b2f2", LocationUtils.locationToString(b2f2));
		}
		if (b1f1 != null) {
			Settings.get().set("maps." + this.getName() + ".b1f1", LocationUtils.locationToString(b1f1));
		}
		if (chest1 != null) {
			Settings.get().set("maps." + this.getName() + ".chest1", LocationUtils.locationToString(chest1));
		}
		if (console1 != null) {
			Settings.get().set("maps." + this.getName() + ".console1", LocationUtils.locationToString(console1));
		}
		if (console2 != null) {
			Settings.get().set("maps." + this.getName() + ".console2", LocationUtils.locationToString(console2));
		}
		if (console3 != null) {
			Settings.get().set("maps." + this.getName() + ".console3", LocationUtils.locationToString(console3));
		}
		if (console4 != null) {
			Settings.get().set("maps." + this.getName() + ".console4", LocationUtils.locationToString(console4));
		}
		if (head1 != null) {
			Settings.get().set("maps." + this.getName() + ".head1", LocationUtils.locationToString(head1));
		}
		if (head2 != null) {
			Settings.get().set("maps." + this.getName() + ".head2", LocationUtils.locationToString(head2));
		}
		if (head3 != null) {
			Settings.get().set("maps." + this.getName() + ".head3", LocationUtils.locationToString(head3));
		}
		if (head4 != null) {
			Settings.get().set("maps." + this.getName() + ".head4", LocationUtils.locationToString(head4));
		}
		if (chest2 != null) {
			Settings.get().set("maps." + this.getName() + ".chest2", LocationUtils.locationToString(chest2));
		}
		if (chest3 != null) {
			Settings.get().set("maps." + this.getName() + ".chest3", LocationUtils.locationToString(chest3));
		}
		if (chest4 != null) {
			Settings.get().set("maps." + this.getName() + ".chest4", LocationUtils.locationToString(chest4));
		}
		if (chest5 != null) {
			Settings.get().set("maps." + this.getName() + ".chest5", LocationUtils.locationToString(chest5));
		}
		if (rs != null) {
			Settings.get().set("maps." + this.getName() + ".rs", LocationUtils.locationToString(rs));
		}
		if (b3f1 != null) {
			Settings.get().set("maps." + this.getName() + ".b3f1", LocationUtils.locationToString(b3f1));
		}
		if (b4f1 != null) {
			Settings.get().set("maps." + this.getName() + ".b4f1", LocationUtils.locationToString(b4f1));
		}
		if (floor2 != null) {
			Settings.get().set("maps." + this.getName() + ".floor2", LocationUtils.locationToString(floor2));
		}
		if (floor3 != null) {
			Settings.get().set("maps." + this.getName() + ".floor3", LocationUtils.locationToString(floor3));
		}
		if (floor4 != null) {
			Settings.get().set("maps." + this.getName() + ".floor4", LocationUtils.locationToString(floor4));
		}
		if (floor1 != null) {
			Settings.get().set("maps." + this.getName() + ".floor1", LocationUtils.locationToString(floor1));
		}
	}

	public void loadFromiConfig() {
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
		if (s.get("maps." + this.getName() + ".bossLoc2") != null) {
			final String s3 = s.get("maps." + this.getName() + ".bossLoc2");
			(this.bossLoc2 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.bossLoc2.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".bossLoc3") != null) {
			final String s3 = s.get("maps." + this.getName() + ".bossLoc3");
			(this.bossLoc3 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.bossLoc3.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".bossLoc4") != null) {
			final String s3 = s.get("maps." + this.getName() + ".bossLoc4");
			(this.bossLoc4 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.bossLoc4.setYaw(LocationUtils.stringToYaw(s3));
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
		if (s.get("maps." + this.getName() + ".b2f1") != null) {
			final String s3 = s.get("maps." + this.getName() + ".b2f1");
			(this.b2f1 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.b2f1.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".b1f1") != null) {
			final String s3 = s.get("maps." + this.getName() + ".b1f1");
			(this.b1f1 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.b1f1.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".chest1") != null) {
			final String s3 = s.get("maps." + this.getName() + ".chest1");
			(this.chest1 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.chest1.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".console1") != null) {
			final String s3 = s.get("maps." + this.getName() + ".console1");
			(this.console1 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.console1.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".console2") != null) {
			final String s3 = s.get("maps." + this.getName() + ".console2");
			(this.console2 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.console2.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".console3") != null) {
			final String s3 = s.get("maps." + this.getName() + ".console3");
			(this.console3 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.console3.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".console4") != null) {
			final String s3 = s.get("maps." + this.getName() + ".console4");
			(this.console4 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.console4.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".head1") != null) {
			final String s3 = s.get("maps." + this.getName() + ".head1");
			(this.head1 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.head1.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".head2") != null) {
			final String s3 = s.get("maps." + this.getName() + ".head2");
			(this.head2 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.head2.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".head3") != null) {
			final String s3 = s.get("maps." + this.getName() + ".head3");
			(this.head3 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.head3.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".head4") != null) {
			final String s3 = s.get("maps." + this.getName() + ".head4");
			(this.head4 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.head4.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".chest2") != null) {
			final String s3 = s.get("maps." + this.getName() + ".chest2");
			(this.chest2 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.chest2.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".chest3") != null) {
			final String s3 = s.get("maps." + this.getName() + ".chest3");
			(this.chest3 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.chest3.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".chest4") != null) {
			final String s3 = s.get("maps." + this.getName() + ".chest4");
			(this.chest4 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.chest4.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".chest5") != null) {
			final String s3 = s.get("maps." + this.getName() + ".chest5");
			(this.chest5 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.chest5.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".rs") != null) {
			final String s3 = s.get("maps." + this.getName() + ".rs");
			(this.rs = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.rs.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".b3f1") != null) {
			final String s3 = s.get("maps." + this.getName() + ".b3f1");
			(this.b3f1 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.b3f1.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".b4f1") != null) {
			final String s3 = s.get("maps." + this.getName() + ".b4f1");
			(this.b4f1 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.b4f1.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".b2f2") != null) {
			final String s3 = s.get("maps." + this.getName() + ".b2f2");
			(this.b2f2 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.b2f2.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".floor2") != null) {
			final String s3 = s.get("maps." + this.getName() + ".floor2");
			(this.floor2 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.floor2.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".floor3") != null) {
			final String s3 = s.get("maps." + this.getName() + ".floor3");
			(this.floor3 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.floor3.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".floor4") != null) {
			final String s3 = s.get("maps." + this.getName() + ".floor4");
			(this.floor4 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.floor4.setYaw(LocationUtils.stringToYaw(s3));
		}
		if (s.get("maps." + this.getName() + ".floor1") != null) {
			final String s3 = s.get("maps." + this.getName() + ".floor1");
			(this.floor1 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
			this.floor1.setYaw(LocationUtils.stringToYaw(s3));
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
		if (b2f1 == null) {
			flag = true;
		}
		if (b2f2 == null) {
			flag = true;
		}
		if (chest1 == null) {
			flag = true;
		}
		if (console1 == null) {
			flag = true;
		}
		if (console2 == null) {
			flag = true;
		}
		if (console3 == null) {
			flag = true;
		}
		if (console4 == null) {
			flag = true;
		}
		if (head1 == null) {
			flag = true;
		}
		if (head2 == null) {
			flag = true;
		}
		if (head3 == null) {
			flag = true;
		}
		if (head4 == null) {
			flag = true;
		}
		if (chest2 == null) {
			flag = true;
		}
		if (chest3 == null) {
			flag = true;
		}
		if (chest4 == null) {
			flag = true;
		}
		if (chest5 == null) {
			flag = true;
		}
		if (rs == null) {
			flag = true;
		}
		if (this.bossLoc == null) {
			flag = true;
		}
		if (this.bossLoc2 == null) {
			flag = true;
		}
		if (this.bossLoc3 == null) {
			flag = true;
		}
		if (this.bossLoc4 == null) {
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
		if (floor3 == null) {
			flag = true;
		}
		if (floor4 == null) {
			flag = true;
		}
		if (floor1 == null) {
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
		loadChests();
		loadChests();
		loadChests();
		loadChests();
		loadChests();
		skinHeads();
		this.setState(MapState.STARTED);
		getPlayer().teleport(floor4);
		finalBoss();
		Lazer.get();
	}

	public void firstRound() {
		setLocked(true);
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

	public void miniBoss() {
		setLocked(true);
		setRound(3);
		Mob.get().Floor2Boss(bossLoc2);
	}

	public void thirdFloor() {
		setLocked(true);
		setRound(4);
		Titles.get().addTitle(getPlayer(), ChatColor.RED + "" + ChatColor.BOLD + "MAD SCIENTIST", 160);
		Titles.get().addSubTitle(getPlayer(),
				ChatColor.WHITE + "Good luck trying to get to me without a working teleporter! HAHAHA! >:}", 140);
		Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable() {
			@Override
			public void run() {
				Mob.get().spawnLit(bossLoc3);
				spawnRobots();
			}
		}, 160L);
	}

	public void finalBoss() {
		setLocked(true);
		setRound(5);
		Final.get().spawnBoss(bossLoc4, this);
	}

	public void spawnGuards() {
		message("Guards have been deployed.");
		Mob.get().spawnGuard(bossLoc2.clone().add(2, 0, 0));
		Mob.get().spawnGuard(bossLoc2.clone().add(0, 0, 2));
		Mob.get().spawnGuard(bossLoc2.clone().add(1, 0, 2));
		Mob.get().spawnGuard(bossLoc2.clone().add(2, 0, 1));
	}

	public void spawnRobots() {
		message("Robots have been deployed.");
		Mob.get().spawnLitGuards(bossLoc3.clone().add(2, 0, 0));
		Mob.get().spawnLitGuards(bossLoc3.clone().add(0, 0, 2));
		Mob.get().spawnLitGuards(bossLoc3.clone().add(1, 0, 2));
		Mob.get().spawnLitGuards(bossLoc3.clone().add(2, 0, 1));
	}

	public void lazerRound() {
		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			@Override
			public void run() {
				t++;
				t2++;
				t3++;
				t4++;
				Lazer.get().lazer1(t, head1, getPlayer().getLocation(), getPlayer(), getMap());
				Lazer.get().lazer2(t2, head2, getPlayer().getLocation(), getPlayer(), getMap());
				Lazer.get().lazer3(t3, head3, getPlayer().getLocation(), getPlayer(), getMap());
				Lazer.get().lazer4(t4, head4, getPlayer().getLocation(), getPlayer(), getMap());
			}
		}, 0L, 0L);
	}

	public void skinHeads() {
		if (heads.size() != 0) {
			for (Location loc : heads) {
				Block b = loc.getBlock();
				if (b.getType() != Material.SKULL) {
					b.setType(Material.SKULL);
				}
				BlockState state = b.getState();
				if (state instanceof Skull) {
					Skull skull = (Skull) state;
					skull.setSkullType(SkullType.PLAYER);
					skull.setOwner("z_kris_z");
					skull.update();
				}
			}
		}
	}
	
	public List<Location> getConsoles() {
		return consoles;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int i) {
		round = i;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean b) {
		first = b;
	}

	public boolean isSecond() {
		return second;
	}

	public void setSecond(boolean b) {
		second = b;
	}

	public boolean isThird() {
		return third;
	}

	public void setThird(boolean b) {
		third = b;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int i) {
		floor = i;
	}

	public void addPlayer(final Player p) {
		if (!this.containsPlayer(p) && this.state.canJoin()) {
			if (p.getInventory().contains(card1)) {
				p.getInventory().remove(card1);
			}
			if (p.getInventory().contains(card2)) {
				p.getInventory().remove(card2);
			}
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
			Bukkit.getServer().getScheduler().cancelTask(id);
			if (p.getInventory().contains(card1)) {
				p.getInventory().remove(card1);
			}
			if (p.getInventory().contains(card2)) {
				p.getInventory().remove(card2);
			}
			if (p.getInventory().contains(card3)) {
				p.getInventory().remove(card3);
			}
			Final.get().removeBoss();
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
		// Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable() {

		// @Override
		// public void run() {
		for (final NPC NPCs : round1) {
			registry.deregister(NPCs);
		}
		round1.clear();
		for (final NPC NPCs : round2) {
			registry.deregister(NPCs);
		}
		round2.clear();
		for (NPC NPCs : round3) {
			registry.deregister(NPCs);
		}
		round3.clear();
		for (NPC NPCs : round4) {
			registry.deregister(NPCs);
		}
		round4.clear();
		for (NPC NPCs : boss.values()) {
			registry.deregister(NPCs);
		}
		boss.clear();
		for (Location loc : tempchests) {
			chests.add(loc);
			Block b = loc.getBlock();
			Chest c = (Chest) b.getState();
			Inventory ci = c.getInventory();
			ci.clear();
		}
		tempchests.clear();
		if (isRS()) {
			Block b = rs.getBlock();
			b.setType(Material.AIR);
			b.getState().update();
		}
		i = 1;
		t = 0;
		t2 = 0;
		t3 = 0;
		t4 = 0;
		if (Lazer.class != null) {
			Lazer.get().reset();
		}
		first = true;
		second = true;
		third = false;
		Final.get().removeBoss();
		setFloor(1);
		setRound(1);
		// }
		// }, 1);
	}

	public void breakRS() {
		if (isRS()) {
			Block b = rs.getBlock();
			b.setType(Material.AIR);
			b.getState().update();
			ItemStack item = new ItemStack(Material.REDSTONE);
			getPlayer().getWorld().dropItem(rs.clone().add(0, 1, 0), item);
		}
	}

	public void resetT(int i) {
		if (i == 1) {
			t = 0;
		}
		if (i == 2) {
			t2 = 0;
		}
		if (i == 3) {
			t3 = 0;
		}
		if (i == 4) {
			t4 = 0;
		}
	}

	public boolean isStarted() {
		return this.state.equals(MapState.STARTED);
	}

	public Location getSpawn() {
		return this.spawn;
	}

	public void setSpawn(final Location l) {
		this.spawn = l;
		this.saveToConfig();
		checkState();
	}

	public void setBossLoc(int i, Location l) {
		if (i == 1) {
			bossLoc = l;
			saveToConfig();
			checkState();
		}
		if (i == 2) {
			bossLoc2 = l;
			saveToConfig();
			checkState();
		}
		if (i == 3) {
			bossLoc3 = l;
			saveToConfig();
			checkState();
		}
		if (i == 4) {
			bossLoc4 = l;
			saveToConfig();
			checkState();
		}
	}

	public List<Location> getChests() {
		return chests;
	}

	public Location getConsole(int i) {
		if (i == 1) {
			return console1;
		}
		if (i == 2) {
			return console2;
		}
		if (i == 3) {
			return console3;
		}
		if (i == 4) {
			return console4;
		} else {
			return null;
		}
	}

	public void setConsole(int i, Location l) {
		if (i == 1) {
			console1 = l;
			saveToConfig();
			checkState();
		}
		if (i == 2) {
			console2 = l;
			saveToConfig();
			checkState();
		}
		if (i == 3) {
			console3 = l;
			saveToConfig();
			checkState();
		}
		if (i == 4) {
			console4 = l;
			saveToConfig();
			checkState();
		}
	}

	public Location getHead(int i) {
		if (i == 1) {
			return head1;
		}
		if (i == 2) {
			return head2;
		}
		if (i == 3) {
			return head3;
		}
		if (i == 4) {
			return head4;
		} else {
			return null;
		}
	}

	public void setHead(int i, Location l) {
		if (i == 1) {
			head1 = l;
			saveToConfig();
			checkState();
		}
		if (i == 2) {
			head2 = l;
			saveToConfig();
			checkState();
		}
		if (i == 3) {
			head3 = l;
			saveToConfig();
			checkState();
		}
		if (i == 4) {
			head4 = l;
			saveToConfig();
			checkState();
		}
	}

	public Location getChest(int i) {
		if (i == 1) {
			return chest1;
		}
		if (i == 2) {
			return chest2;
		}
		if (i == 3) {
			return chest3;
		}
		if (i == 4) {
			return chest4;
		}
		if (i == 5) {
			return chest5;
		} else {
			return null;
		}
	}

	public void setChest(int i, Location l) {
		if (i == 1) {
			chest1 = l;
			saveToConfig();
			checkState();
		}
		if (i == 2) {
			chest2 = l;
			saveToConfig();
			checkState();
		}
		if (i == 3) {
			chest3 = l;
			saveToConfig();
			checkState();
		}
		if (i == 4) {
			chest4 = l;
			saveToConfig();
			checkState();
		}
		if (i == 5) {
			chest5 = l;
			saveToConfig();
			checkState();
		}
	}

	public Location getRS() {
		return rs;
	}

	public void setRS(Location l) {
		rs = l;
		saveToConfig();
		checkState();
	}

	public boolean isRS() {
		return rs.getBlock().getType() == Material.REDSTONE_WIRE || rs.getBlock().getType() == Material.REDSTONE;
	}

	public void setButton3(int i, Location l) {
		if (i == 1) {
			b3f1 = l;
			saveToConfig();
			checkState();
		}
	}

	public void setButton4(int i, Location l) {
		if (i == 1) {
			b4f1 = l;
			saveToConfig();
			checkState();
		}
	}

	public void setButton1(int i, Location l) {
		if (i == 1) {
			b1f1 = l;
			saveToConfig();
			checkState();
		}
	}

	public Location getButton3(int i) {
		if (i == 1) {
			return b3f1;
		} else {
			return null;
		}
	}

	public Location getButton4(int i) {
		if (i == 1) {
			return b4f1;
		} else {
			return null;
		}
	}

	public Location getButton1(int i) {
		if (i == 1) {
			return b1f1;
		} else {
			return null;
		}
	}

	public void setButton2(int i, Location l) {
		if (i == 1) {
			b2f1 = l;
			saveToConfig();
			checkState();
		}
		if (i == 2) {
			b2f2 = l;
			saveToConfig();
			checkState();
		}
	}

	public void setDoorLoc1(final Location l) {
		this.door1 = l;
		this.checkState();
		saveToConfig();
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

	public Location getBossLoc(int i) {
		if (i == 1) {
			return bossLoc;
		}
		if (i == 2) {
			return bossLoc2;
		}
		if (i == 3) {
			return bossLoc3;
		}
		if (i == 4) {
			return bossLoc4;
		} else {
			return null;
		}
	}

	public Location get2ButtonLoc(int i) {
		if (i == 1) {
			return b2f1;
		}
		if (i == 2) {
			return b2f2;
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
		if (i == 1) {
			floor1 = l;
			saveToConfig();
			checkState();
		}
		if (i == 3) {
			floor3 = l;
			saveToConfig();
			checkState();
		}
		if (i == 4) {
			floor4 = l;
			saveToConfig();
			checkState();
		}
	}

	public Location getFloor(int i) {
		if (i == 2) {
			return floor2;
		}
		if (i == 1) {
			return floor1;
		}
		if (i == 3) {
			return floor3;
		}
		if (i == 4) {
			return floor4;
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
		return round1.contains(npc) || round2.contains(npc) || round3.contains(npc) || round4.contains(npc)
				|| boss.containsValue(npc);
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean b) {
		locked = b;
	}

	public List<NPC> getRoundNPC(int i) {
		if (i == 1) {
			return round1;
		}
		if (i == 2) {
			return round2;
		}
		if (i == 3) {
			return round3;
		}
		if (i == 4) {
			return round4;
		} else {
			return null;
		}
	}

	public NPC getBoss(int i) {
		return boss.get(i);
	}

	public HashMap<Integer, NPC> getBoss() {
		return boss;
	}

	public ItemStack getCard(int i) {
		if (i == 1) {
			return card1;
		}
		if (i == 2) {
			return card2;
		}
		if (i == 3) {
			return card3;
		} else {
			return null;
		}
	}

	public String getName() {
		return this.name;
	}

	public List<Location> getButtons(int i) {
		if (i == 1) {
			return b1s;
		}
		if (i == 2) {
			return b2s;
		}
		if (i == 3) {
			return b3s;
		}
		if (i == 4) {
			return b4s;
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
