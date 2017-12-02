package com.likeapig.missions.map;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.mcmonkey.sentinel.SentinelTrait;

import com.likeapig.missions.models.LawnMower;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class Boss {

	public static Boss instance;
	private double sinus = 0.08715574274765817;
	private double cosinus = 0.9961946980917455;
	private float degrees = 5;
	private boolean min = false;

	private HashMap<String, ArmorStand> parts = new HashMap<>();
	Location chairLoc;
	NPCRegistry registry = CitizensAPI.getNPCRegistry();
	NPC npc;
	NPC npcLook;

	static {
		instance = new Boss();
	}

	public static Boss get() {
		return instance;
	}

	public Location getChairLoc() {
		return chairLoc;
	}

	public String getAS() {
		return Integer.toString(parts.size());
	}

	public HashMap<String, ArmorStand> getParts() {
		return parts;
	}

	public void setNPC() {
		npc = this.registry.createNPC(EntityType.PLAYER, "§4§lSCIENTIST");
		npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, "scientist");
		npc.addTrait(SentinelTrait.class);
		npc.spawn(getParts().get("body2").getLocation());
		if (npc.isSpawned()) {
			Damageable entity = (Damageable) npc.getEntity();
			entity.setMaxHealth(500);
			entity.setHealth(500);
			((HumanEntity) npc.getEntity()).getInventory().setItemInMainHand(new ItemStack(Material.REDSTONE_TORCH_ON));
		}
		getParts().get("body2").setPassenger(npc.getEntity());
	}

	public void spawnLook() {
		npcLook = registry.createNPC(EntityType.PLAYER, "Robot");
		npcLook.data().set(NPC.DEFAULT_PROTECTED_METADATA, true);
		npcLook.data().set(NPC.COLLIDABLE_METADATA, false);
		npcLook.data().set(NPC.TARGETABLE_METADATA, false);
		npcLook.addTrait(SentinelTrait.class);
		npcLook.spawn(getParts().get("body2").getLocation());
		npcLook.getTrait(SentinelTrait.class).getLivingEntity()
				.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
	}

	public NPC getLook() {
		return npcLook;
	}

	public void look(Location l) {
		npcLook.faceLocation(l);
	}

	public void setNPCYaw(Location l) {
		npc.getEntity().getLocation().setYaw(l.getYaw());
	}

	public NPC getNPC() {
		return npc;
	}

	public void removeNPC() {
		registry.deregister(npc);
		registry.deregister(npcLook);
	}

	public void tpSeat(ArmorStand as, Location loc) {
		if (as.getPassenger() != null) {
			Entity e = as.getPassenger();
			as.eject();
			as.teleport(loc);
			e.teleport(loc);
			as.setPassenger(e);
			e.getLocation().setYaw(loc.getYaw());
		}
	}

	public Location rotateRight() {
		Location origin = getChairLoc();
		Location vec = null;
		for (ArmorStand entity : getParts().values()) {
			Location point = entity.getLocation();
			point = point.subtract(origin);
			double x = point.getX() * cosinus - point.getZ() * sinus;
			double y = point.getZ() * cosinus + point.getX() * sinus;
			point.setX(x);
			point.setZ(y);
			point = point.add(origin);
			point.setYaw(point.getYaw() + degrees);
			point.setPitch(point.getPitch());
			entity.teleport(point);
			tpSeat(entity, point);
		}
		return vec;
	}

	public Location hover(int i) {
		Location origin = getChairLoc();
		Location vec = null;
		for (ArmorStand entity : getParts().values()) {
			Location point = entity.getLocation();
			Location clone = origin.clone();
			clone = clone.subtract(origin);
			point = point.subtract(origin);
			double y = point.getY();
			if (i >= 0 && i < 12) {
				y = point.getY() - 0.01;
			}
			if (i >= 12 && i <= 24) {
				y = point.getY() + 0.01;
			}
			point.setY(y);
			point = point.add(origin);
			clone.add(origin);
			entity.teleport(point);
			tpSeat(entity, point);
		}
		return vec;
	}

	public Location rotateLeft() {
		Location origin = getChairLoc();
		Location vec = null;
		for (ArmorStand entity : getParts().values()) {
			Location point = entity.getLocation();
			point = point.subtract(origin);
			double x = point.getX() * cosinus + point.getZ() * sinus;
			double y = point.getZ() * cosinus - point.getX() * sinus;
			point.setX(x);
			point.setZ(y);
			point = point.add(origin);
			point.setYaw(point.getYaw() - degrees);
			point.setPitch(point.getPitch());
			entity.teleport(point);
			tpSeat(entity, point);
		}
		return vec;
	}

	public static Vector getDirection(Location location, Location destination) {
		double x1, y1, z1;
		double x0, y0, z0;

		x1 = destination.getX();
		y1 = destination.getY();
		z1 = destination.getZ();

		x0 = location.getX();
		y0 = location.getY();
		z0 = location.getZ();

		return new Vector(x1 - x0, y1 - y0, z1 - z0);
	}

	public ArmorStand NewArmorStand(Location location, boolean visible, boolean mini) {
		location.setYaw(0);
		ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);

		as.setVisible(visible);
		as.setArms(true);
		as.setBasePlate(false);
		as.setInvulnerable(true);
		as.setGravity(false);
		as.setCanPickupItems(false);
		as.setSmall(mini);

		return as;
	}

	public void TPChair(Location location) {

		Location l = location.clone();
		Location la = l.clone().subtract(0.53, 0, 0).add(0, 1.3, 0);
		Location ra = la.clone().add(2.3, 0, 0);
		Location bm = l.clone().subtract(0, 0, 0.62).add(0, 0.3, 0);
		Location b = l.clone().subtract(0, 0, 0.62).add(0, 0.3, 0);

		parts.get("body").teleport(l);
		parts.get("body2").teleport(l.clone().add(0.62, 0, 0));
		parts.get("body3").teleport(l.clone().add(1.24, 0, 0));
		parts.get("body4").teleport(l.clone().add(0, 0, 0.62));
		parts.get("body5").teleport(l.clone().add(0.62, 0, 0.62));
		parts.get("body6").teleport(l.clone().add(1.24, 0, 0.62));
		parts.get("leftarm").teleport(la);
		parts.get("leftarm2").teleport(la.clone().add(0, 0, 0.4));
		parts.get("leftarm3").teleport(la.clone().add(0, 0, 0.7));
		parts.get("leftbutton").teleport(la.clone().add(0, 0, 0.9));
		parts.get("rightarm").teleport(ra);
		parts.get("rightarm2").teleport(ra.clone().add(0, 0, 0.4));
		parts.get("rightarm3").teleport(ra.clone().add(0, 0, 0.7));
		parts.get("rightbutton").teleport(ra.clone().add(0, 0, 0.9));
		parts.get("backmid").teleport(bm);
		parts.get("backmid2").teleport(bm.clone().add(0.62, 0, 0));
		parts.get("backmid3").teleport(bm.clone().add(1.24, 0, 0));
		parts.get("back").teleport(b);
		parts.get("back2").teleport(b.clone().add(0.62, 0, 0));
		parts.get("back3").teleport(b.clone().add(1.24, 0, 0));
		parts.get("back4").teleport(b.clone().add(0, 0.62, 0));
		parts.get("back5").teleport(b.clone().add(0.62, 0.62, 0));
		parts.get("back6").teleport(b.clone().add(1.24, 0.62, 0));
		// parts.get("back7").teleport(b.clone().add(0, 1.24, 0));
		parts.get("back8").teleport(b.clone().add(0.62, 1.24, 0));
		// parts.get("back9").teleport(b.clone().add(1.24, 1.24, 0));
		parts.get("back10").teleport(b.clone().subtract(0.62, 0, 0).add(0, 0.3, 0.1));
		parts.get("back11").teleport(b.clone().add(1.55, 0.3, 0.1));
		parts.get("red").teleport(l.clone().add(0.62, 0, 1.2).subtract(0, 0.5, 0));
		parts.get("chain").teleport(l.clone().add(0.76, 0.25, 1.2).subtract(0, 0, 0));
		parts.get("chain2").teleport(l.clone().add(0.48, 0.25, 1.2).subtract(0, 0, 0));

	}

	public void Chair(Location location) {

		Location l = location.clone();
		Location la = l.clone().subtract(0.53, 0, 0).add(0, 1.3, 0);
		Location ra = la.clone().add(2.3, 0, 0);
		Location bm = l.clone().subtract(0, 0, 0.62).add(0, 0.3, 0);
		Location b = l.clone().subtract(0, 0, 0.62).add(0, 0.3, 0);

		ArmorStand body = NewArmorStand(l, false, false);
		body.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand body2 = NewArmorStand(l.clone().add(0.62, 0, 0), false, false);
		body2.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));
		chairLoc = body2.getLocation();

		ArmorStand body3 = NewArmorStand(l.clone().add(1.24, 0, 0), false, false);
		body3.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand body4 = NewArmorStand(l.clone().add(0, 0, 0.62), false, false);
		body4.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand body5 = NewArmorStand(l.clone().add(0.62, 0, 0.62), false, false);
		body5.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand body6 = NewArmorStand(l.clone().add(1.24, 0, 0.62), false, false);
		body6.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand leftarm = NewArmorStand(la, false, true);
		leftarm.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand leftarm2 = NewArmorStand(la.clone().add(0, 0, 0.4), false, true);
		leftarm2.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand leftarm3 = NewArmorStand(la.clone().add(0, 0, 0.7), false, true);
		leftarm3.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand leftbutton = NewArmorStand(la.clone().add(0, 0, 0.9), false, true);
		leftbutton.setHelmet(new ItemStack(Material.DISPENSER, (byte) 2));

		ArmorStand rightarm = NewArmorStand(ra, false, true);
		rightarm.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand rightarm2 = NewArmorStand(ra.clone().add(0, 0, 0.4), false, true);
		rightarm2.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand rightarm3 = NewArmorStand(ra.clone().add(0, 0, 0.7), false, true);
		rightarm3.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand rightbutton = NewArmorStand(ra.clone().add(0, 0, 0.9), false, true);
		rightbutton.setHelmet(new ItemStack(Material.DISPENSER, (byte) 2));

		ArmorStand backmid = NewArmorStand(bm, false, false);
		backmid.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));
		backmid.setHeadPose(new EulerAngle(315f, 0, 0));

		ArmorStand backmid2 = NewArmorStand(bm.clone().add(0.62, 0, 0), false, false);
		backmid2.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));
		backmid2.setHeadPose(new EulerAngle(315f, 0, 0));

		ArmorStand backmid3 = NewArmorStand(bm.clone().add(1.24, 0, 0), false, false);
		backmid3.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));
		backmid3.setHeadPose(new EulerAngle(315f, 0, 0));

		ArmorStand back = NewArmorStand(b, false, false);
		back.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand back2 = NewArmorStand(b.clone().add(0.62, 0, 0), false, false);
		back2.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand back3 = NewArmorStand(b.clone().add(1.24, 0, 0), false, false);
		back3.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand back4 = NewArmorStand(b.clone().add(0, 0.62, 0), false, false);
		back4.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand back5 = NewArmorStand(b.clone().add(0.62, 0.62, 0), false, false);
		back5.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		ArmorStand back6 = NewArmorStand(b.clone().add(1.24, 0.62, 0), false, false);
		back6.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		// ArmorStand back7 = NewArmorStand(b.clone().add(0, 1.24, 0), false, false);
		// back7.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte)1));

		ArmorStand back8 = NewArmorStand(b.clone().add(0.62, 1.24, 0), false, false);
		back8.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));

		// ArmorStand back9 = NewArmorStand(b.clone().add(1.24, 1.24, 0), false, false);
		// back9.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte)1));

		ArmorStand back10 = NewArmorStand(b.clone().subtract(0.62, 0, 0).add(0, 0.3, 0.1), false, false);
		back10.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));
		back10.setHeadPose(new EulerAngle(0, 0, 315));

		ArmorStand back11 = NewArmorStand(b.clone().add(1.55, 0.3, 0.1), false, false);
		back11.setHelmet(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 1));
		back11.setHeadPose(new EulerAngle(0, 0, 315));

		ArmorStand red = NewArmorStand(l.clone().add(0.62, 0, 1.2).subtract(0, 0.5, 0), false, false);
		red.setHelmet(new ItemStack(Material.REDSTONE));

		ArmorStand chain = NewArmorStand(l.clone().add(0.76, 0.25, 1.2).subtract(0, 0, 0), false, false);
		chain.setHelmet(new ItemStack(Material.IRON_FENCE));
		chain.setHeadPose(new EulerAngle(0, 0, Math.toRadians(270)));

		ArmorStand chain2 = NewArmorStand(l.clone().add(0.48, 0.25, 1.2).subtract(0, 0, 0), false, false);
		chain2.setHelmet(new ItemStack(Material.IRON_FENCE));
		chain2.setHeadPose(new EulerAngle(0, 0, Math.toRadians(90)));

		parts.put("body", body);
		parts.put("body2", body2);
		parts.put("body3", body3);
		parts.put("body4", body4);
		parts.put("body5", body5);
		parts.put("body6", body6);
		parts.put("leftarm", leftarm);
		parts.put("leftarm2", leftarm2);
		parts.put("leftarm3", leftarm3);
		parts.put("rightarm", rightarm);
		parts.put("rightarm2", rightarm2);
		parts.put("rightarm3", rightarm3);
		parts.put("backmid", backmid);
		parts.put("backmid2", backmid2);
		parts.put("backmid3", backmid3);
		parts.put("back", back);
		parts.put("back2", back2);
		parts.put("back3", back3);
		parts.put("back4", back4);
		parts.put("back5", back5);
		parts.put("back6", back6);
		// parts.put("back7", back7);
		parts.put("back8", back8);
		// parts.put("back9", back9);
		parts.put("back10", back10);
		parts.put("back11", back11);
		parts.put("red", red);
		parts.put("chain", chain);
		parts.put("chain2", chain2);
		parts.put("leftbutton", leftbutton);
		parts.put("rightbutton", rightbutton);
	}

	public void removeChair() {
		if (!parts.isEmpty()) {
			for (ArmorStand as : parts.values()) {
				as.remove();
			}
			parts.clear();
		}
	}

}
