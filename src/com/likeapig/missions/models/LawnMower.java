package com.likeapig.missions.models;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;

import de.Ste3et_C0st.FurnitureLib.Crafting.Project;
import de.Ste3et_C0st.FurnitureLib.main.FurnitureLib;
import de.Ste3et_C0st.FurnitureLib.main.ObjectID;
import de.Ste3et_C0st.FurnitureLib.main.Type.SQLAction;
import de.Ste3et_C0st.FurnitureLib.main.entity.fEntity;
import net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle;

public class LawnMower {

	public static LawnMower instance;
	private FurnitureLib lib = FurnitureLib.getInstance();
	private Location mowerLoc;
	Project mower;
	List<ObjectID> ids;
	private double sinus = 0.08715574274765817;
	private double cosinus = 0.9961946980917455;
	private float degrees = 5;

	static {
		instance = new LawnMower();
	}

	public static LawnMower get() {
		return instance;
	}

	public List<ObjectID> getID() {
		return ids;
	}

	public void mower(Location loc) {
		mower = lib.getFurnitureManager().getProject("sleigh");
		lib.spawn(mower, loc);
		mowerLoc = loc;
		ids = getNearbyProject(mowerLoc, mower.getName(), 2);
	}

	public void remove() {
		for (ObjectID id : getNearbyProject(mowerLoc, mower.getName(), 2)) {
			id.remove(false);
		}
	}

	public Project getMower() {
		return mower;
	}

	public Location rotateRight() {
		Location origin = mowerLoc;
		Location vec = null;
		for (ObjectID id : ids) {
			for (fEntity entity : id.getPacketList()) {
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
			}
		}
		return vec;
	}

	public Location rotateLeft() {
		Location origin = mowerLoc;
		Location vec = null;
		for (ObjectID id : ids) {
			for (fEntity entity : id.getPacketList()) {
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
			}
		}
		return vec;
	}

	public Location forward() {
		Location origin = mowerLoc;
		Location vec = null;
		for (ObjectID id : ids) {
			for (fEntity entity : id.getPacketList()) {
				Location point = entity.getLocation();
				point = point.subtract(origin);
				double x = point.getX() * cosinus + point.getZ() * sinus;
				double y = point.getZ() * cosinus - point.getX() * sinus;
				point.setX(x);
				point.setZ(y);
				point = point.add(origin);
				point.setPitch(point.getPitch());
				((Entity) entity).setVelocity(entity.getLocation().getDirection().normalize().multiply(0.2).setY(-2));
			}
		}
		return vec;
	}
	
	public Location backward() {
		Location origin = mowerLoc;
		Location vec = null;
		for (ObjectID id : ids) {
			for (fEntity entity : id.getPacketList()) {
				Location point = entity.getLocation();
				point = point.subtract(origin);
				double x = point.getX() * cosinus + point.getZ() * sinus;
				double y = point.getZ() * cosinus - point.getX() * sinus;
				point.setX(x);
				point.setZ(y);
				point = point.add(origin);
				point.setPitch(point.getPitch());
				((Entity) entity).setVelocity(entity.getLocation().getDirection().normalize().multiply(-0.2).setY(-2));
			}
		}
		return vec;
	}

	public Location getMowerLoc() {
		return mowerLoc;
	}

	public static List<ObjectID> getNearbyProject(Location l, String str, int radius) {
		List<ObjectID> idList = new ArrayList<ObjectID>();
		for (ObjectID id : FurnitureLib.getInstance().getFurnitureManager().getObjectList()) {
			if (id.getSQLAction().equals(SQLAction.REMOVE))
				continue;
			if (id.getProject().startsWith(str)) {
				if (id.getStartLocation().getWorld().equals(l.getWorld())) {
					if (id.getStartLocation().distance(l) <= radius)
						idList.add(id);

				}
			}
		}
		return idList;
	}
}
