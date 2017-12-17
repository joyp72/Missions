package com.likeapig.missions.models;

import org.bukkit.Bukkit;
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

	public void onPacketReceiving(PacketEvent e) {
		if (e.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {

			PacketPlayInSteerVehicle packet = (PacketPlayInSteerVehicle) e.getPacket().getHandle();

			float forward = packet.b();
			float side = packet.a();

			if (forward > 0) {
				Bukkit.getServer().broadcastMessage("forward");
				//LawnMower.get().forward();
			} else if (forward < 0) {
				//LawnMower.get().backward();
				Bukkit.getServer().broadcastMessage("back");
			}
			if (side > 0) {
				//LawnMower.get().rotateLeft();
				Bukkit.getServer().broadcastMessage("left");
			} else if (side < 0) {
				//LawnMower.get().rotateRight();
				Bukkit.getServer().broadcastMessage("right");
			}
		}
	}

}
