package com.likeapig.missions.intro;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IntroData {

	private UUID id;
	private Location location;
	private Intro intro;
	private ItemStack[] contents;
	private ItemStack[] armorContents;

	protected IntroData(Player p, Intro i) {
		id = p.getUniqueId();
		location = p.getLocation();
		intro = i;
		contents = p.getInventory().getContents();
		armorContents = p.getInventory().getArmorContents();
	}

	protected void restore() {
		Player p = Bukkit.getPlayer(id);
		p.getInventory().setContents(contents);
		p.getInventory().setArmorContents(armorContents);
	}

	public Intro getIntro() {
		return intro;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(id);
	}

	public Location getLocation() {
		return location;
	}

}
