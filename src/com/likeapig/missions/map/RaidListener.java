package com.likeapig.missions.map;

import java.util.*;
import org.bukkit.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import com.likeapig.missions.*;
import org.bukkit.plugin.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDamageEntityEvent;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.md_5.bungee.api.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class RaidListener implements Listener {
	private static RaidListener instance;
	private List<NPC> round1;
	private List<NPC> round2;
	private NPCRegistry registry = CitizensAPI.getNPCRegistry();
	private Map map = MapManager.get().getMap("test");

	static {
		RaidListener.instance = new RaidListener();
	}

	public RaidListener() {
		this.round1 = Mob.get().getRound(1);
		round2 = Mob.get().getRound(2);
	}

	public static RaidListener get() {
		return RaidListener.instance;
	}

	public void setup() {
		Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.get());
	}

	@EventHandler
	public void onNPCDeath(NPCDeathEvent e) {
		NPC npc = e.getNPC();
		Map m = MapManager.get().getMap(npc);
		if (m != null) {
			m.handleNPCDeath(e);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (e.getEntity().hasMetadata("NPC")) {
			e.setDeathMessage(null);
		}
	}

	@EventHandler
	public void onNPCDamage(NPCDamageEntityEvent e) {
		if (e.getDamaged() instanceof Player) {
			Player p = (Player) e.getDamaged();
			NPC npc = e.getNPC();
			Map m = MapManager.get().getMap(npc);
			if (m != null) {
				if (Mob.get().getBoss(1).contains(npc)) {
					if (!p.hasPotionEffect(PotionEffectType.SLOW)) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 90, 2));
						p.sendMessage(ChatColor.RED + "You have been slowed by the Guard!");
					}
				}
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked() instanceof ArmorStand) {
			if (Boss.get().getParts().containsValue(e.getRightClicked())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerGlide(final EntityToggleGlideEvent e) {
		final Entity entity = e.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}
		final Player pl = (Player) e.getEntity();
		if (pl.isGliding()) {
			e.setCancelled(true);
		}
	}
}
