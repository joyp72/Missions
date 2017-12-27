package com.likeapig.missions.menus;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.likeapig.missions.intro.Intro;
import com.likeapig.missions.intro.IntroManager;
import com.likeapig.missions.map.Map;
import com.likeapig.missions.map.Map.MapState;
import com.likeapig.missions.map.MapManager;
import com.likeapig.missions.intro.Intro.IntroState;

import net.md_5.bungee.api.ChatColor;

public class Menus {

	private int size = 9;
	private String sm = "Story Menu";
	private static HashMap<Player, Inventory> menus = new HashMap<Player, Inventory>();

	public static HashMap<Player, Inventory> getMenus() {
		return menus;
	}

	public static boolean containsInv(Inventory i) {
		return menus.containsValue(i);
	}
	
	public static void resetInvs(Player p) {
		if (menus.get(p) != null) {
			menus.remove(p);
		}
	}

	public Menus(Player p) {
		Inventory mi = Bukkit.createInventory(p, size, sm);
		if (menus.get(p) == null) {
			menus.put(p, mi);
		}
		
		ItemStack blank = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		{
			ItemMeta meta = blank.getItemMeta();
			meta.setDisplayName(" ");
			meta.addItemFlags(ItemFlag.values());
			blank.setItemMeta(meta);
			mi.setItem(0, blank);
			mi.setItem(2, blank);
			mi.setItem(4, blank);
			mi.setItem(5, blank);
			mi.setItem(6, blank);
			mi.setItem(7, blank);
			mi.setItem(8, blank);
		}
		
		if (MapManager.get().getMaps().size() > 0) {
			if (MapManager.get().getMap(p) == null) {
				Map i = MapManager.get().getMaps().get(0);
				if (i.getState().equals(MapState.WAITING)) {
					ItemStack item = new ItemStack(Material.IRON_BLOCK);
					{
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Mission - Mad Scientist");
						ArrayList<String> lore = new ArrayList<>();
						lore.add(ChatColor.GRAY + "(Click to join)");
						lore.add(" ");
						lore.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "Online");
						lore.add(ChatColor.GRAY + "Difficulty: " + ChatColor.GREEN + "Normal");
						meta.setLore(lore);
						meta.addItemFlags(ItemFlag.values());
						item.setItemMeta(meta);
						item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
						mi.setItem(3, item);
					}
				} else if (i.isStarted()) {
					ItemStack item = new ItemStack(Material.IRON_BLOCK);
					{
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Mission - Mad Scientist");
						ArrayList<String> lore = new ArrayList<>();
						lore.add(ChatColor.GRAY + "(Unable to join)");
						lore.add(" ");
						lore.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "Running");
						lore.add(ChatColor.GRAY + "Difficulty: " + ChatColor.GREEN + "Normal");
						meta.setLore(lore);
						meta.addItemFlags(ItemFlag.values());
						item.setItemMeta(meta);
						item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
						mi.setItem(3, item);
					}
				}
			} else {
				ItemStack item = new ItemStack(Material.BEDROCK);
				{
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Leave");
					ArrayList<String> lore = new ArrayList<>();
					lore.add(ChatColor.GRAY + "Click to leave Mission.");
					meta.setLore(lore);
					item.setItemMeta(meta);
				}
				mi.setItem(3, item);
			}
		}

		if (IntroManager.get().getIntros().size() > 0) {
			if (IntroManager.get().getIntro(p) == null) {
				Intro i = IntroManager.get().getIntros().get(0);
				if (i.getState().equals(IntroState.WAITING)) {
					ItemStack item = new ItemStack(Material.BOOK);
					{
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Introduction");
						ArrayList<String> lore = new ArrayList<>();
						lore.add(ChatColor.GRAY + "(Click to join)");
						lore.add(" ");
						lore.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "Online");
						meta.setLore(lore);
						meta.addItemFlags(ItemFlag.values());
						item.setItemMeta(meta);
						item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
						mi.setItem(1, item);
					}
				} else if (i.isStarted()) {
					ItemStack item = new ItemStack(Material.BOOK);
					{
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Introduction");
						ArrayList<String> lore = new ArrayList<>();
						lore.add(ChatColor.GRAY + "(Unable to join)");
						lore.add(" ");
						lore.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "Running");
						meta.setLore(lore);
						meta.addItemFlags(ItemFlag.values());
						item.setItemMeta(meta);
						item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
						mi.setItem(1, item);
					}
				}
			} else {
				ItemStack item = new ItemStack(Material.BEDROCK);
				{
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Leave");
					ArrayList<String> lore = new ArrayList<>();
					lore.add(ChatColor.GRAY + "Click to leave intro.");
					meta.setLore(lore);
					item.setItemMeta(meta);
				}
				mi.setItem(1, item);
			}
		}
	}

}
