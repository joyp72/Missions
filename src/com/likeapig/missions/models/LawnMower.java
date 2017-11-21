package com.likeapig.missions.models;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LawnMower {

	public static LawnMower instance;

	static {
		instance = new LawnMower();
	}

	public static LawnMower get() {
		return instance;
	}

	private HashMap<String, ArmorStand> parts = new HashMap<>();

	public HashMap<String, ArmorStand> getParts() {
		return parts;
	}

	public ArmorStand NewAS(Location loc, boolean mini) {
		loc.setYaw(0);
		ArmorStand as = loc.getWorld().spawn(loc, ArmorStand.class);

		as.setVisible(false);
		as.setArms(true);
		as.setBasePlate(false);
		as.setInvulnerable(true);
		as.setGravity(true);
		as.setCanPickupItems(false);
		as.setSmall(mini);
		as.setCollidable(false);

		return as;
	}

	public void setPassanger(Player p) {
		parts.get("seat").setPassenger(p);
	}

	public void spawnMower(Location loc, Player pl) {

		Location l = loc.clone();
		Location bl = l.subtract(0, 1.37, 0);

		ArmorStand body1 = NewAS(bl, false);
		body1.setHelmet(new ItemStack(Material.BRICK_STAIRS, 1, (byte) 14));

		ArmorStand body2 = NewAS(bl.clone().add(0.62, 0, 0), false);// 0.465
		body2.setHelmet(new ItemStack(Material.CONCRETE, 1, (byte) 14));

		ArmorStand body3 = NewAS(bl.clone().subtract(0.62, 0, 0), false);// 0.465
		body3.setHelmet(new ItemStack(Material.CONCRETE, 1, (byte) 14));

		ArmorStand body4 = NewAS(bl.clone().add(0, 0, 0.62).subtract(0, 0.31, 0), false);
		body4.setHelmet(new ItemStack(Material.CONCRETE, 1, (byte) 14));

		ArmorStand body5 = NewAS(bl.clone().add(0.465, 0, 0.62).subtract(0, 0.465, 0), false);
		body5.setHelmet(new ItemStack(Material.CONCRETE, 1, (byte) 14));

		ArmorStand body6 = NewAS(bl.clone().subtract(0.465, 0.465, 0).add(0, 0, 0.62), false);
		body6.setHelmet(new ItemStack(Material.CONCRETE, 1, (byte) 14));

		ArmorStand body7 = NewAS(bl.clone().add(0, 0.31, 1.24), false);
		body7.setHelmet(new ItemStack(Material.CONCRETE, 1, (byte) 14));

		ArmorStand body8 = NewAS(bl.clone().add(0, 1, 1.4), true);
		body8.setHelmet(new ItemStack(Material.THIN_GLASS));

		ArmorStand body9 = NewAS(bl.clone().add(0.02, 0, 1.2).subtract(0, 0.19, 0), false);
		// ArmorStand body9 = NewAS(bl.clone().add(0, 0.8, 1.1), true);
		body9.setHelmet(new ItemStack(Material.RECORD_11));

		ArmorStand seat = NewAS(bl, false);
		seat.setPassenger(pl);
		seat.setGlowing(true);

		parts.put("body1", body1);
		parts.put("body2", body2);
		parts.put("body3", body3);
		parts.put("body4", body4);
		parts.put("body5", body5);
		parts.put("body6", body6);
		parts.put("body7", body7);
		parts.put("body8", body8);
		parts.put("body9", body9);
		parts.put("seat", seat);
	}

	public void removeMower() {
		if (parts != null) {
			if (!parts.isEmpty()) {
				for (ArmorStand as : parts.values()) {
					as.remove();
				}
				parts.clear();
			}
		}
	}

}
