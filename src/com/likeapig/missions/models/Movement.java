package com.likeapig.missions.models;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle;

public class Movement extends PacketAdapter {

	public Movement(Plugin plugin, ListenerPriority listenerPriority, PacketType[] types) {
		super(plugin, listenerPriority, types);
	}

	private double sinus = 0.08715574274765817;
	private double cosinus = 0.9961946980917455;
	private float degrees = 5;

	public Location rotateRight() {
		Location origin = LawnMower.get().getParts().get("seat").getLocation().clone();
		Location vec = null;
		for (ArmorStand entity : LawnMower.get().getParts().values()) {
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

	public Location rotateLeft() {
		Location origin = LawnMower.get().getParts().get("seat").getLocation().clone();
		Location vec = null;
		for (ArmorStand entity : LawnMower.get().getParts().values()) {
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

	public Location forward() {
		Location origin = LawnMower.get().getParts().get("seat").getLocation().clone();
		Location vec = null;
		for (ArmorStand entity : LawnMower.get().getParts().values()) {
			Location point = entity.getLocation();
			point = point.subtract(origin);
			double x = point.getX() * cosinus + point.getZ() * sinus;
			double y = point.getZ() * cosinus - point.getX() * sinus;
			point.setX(x);
			point.setZ(y);
			point = point.add(origin);
			point.setPitch(point.getPitch());
			entity.setVelocity(entity.getLocation().getDirection().normalize().multiply(0.2).setY(-2));
			tpSeat(entity, point);
		}
		return vec;
	}

	public Location backward() {
		Location origin = LawnMower.get().getParts().get("seat").getLocation().clone();
		Location vec = null;
		for (ArmorStand entity : LawnMower.get().getParts().values()) {
			Location point = entity.getLocation();
			point = point.subtract(origin);
			double x = point.getX() * cosinus + point.getZ() * sinus;
			double y = point.getZ() * cosinus - point.getX() * sinus;
			point.setX(x);
			point.setZ(y);
			point = point.add(origin);
			point.setPitch(point.getPitch());
			entity.setVelocity(entity.getLocation().getDirection().normalize().multiply(-0.2).setY(-2));
			tpSeat(entity, point);
		}
		return vec;
	}

	public void tpSeat(ArmorStand as, Location loc) {
		if (as.getPassenger() != null) {
			Player p = (Player) as.getPassenger();
			as.eject();
			as.teleport(loc);
			p.teleport(loc);
			as.setPassenger(p);
		}
	}

	public void onPacketReceiving(PacketEvent e) {
		if (e.getPacketType() == PacketType.Play.Client.STEER_VEHICLE
				&& e.getPlayer().getVehicle() instanceof ArmorStand) {

			ArmorStand seat = (ArmorStand) e.getPlayer().getVehicle();

			if (!LawnMower.get().getParts().containsValue(seat)) {
				return;
			}

			PacketPlayInSteerVehicle packet = (PacketPlayInSteerVehicle) e.getPacket().getHandle();

			float forward = packet.b();
			float side = packet.a();
			Block b = seat.getLocation().getBlock();

			if (forward > 0) {
				forward();
			} else if (forward < 0) {
				backward();
			}
			if (side > 0) {
				rotateLeft();
			} else if (side < 0) {
				rotateRight();
			}
		}
	}

}
