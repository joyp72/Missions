package com.likeapig.missions.map;

import java.util.*;
import org.bukkit.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import com.likeapig.missions.*;
import com.likeapig.missions.commands.MessageManager;
import com.likeapig.missions.models.LawnMower;

import org.bukkit.plugin.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.event.NPCDamageEntityEvent;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.md_5.bungee.api.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class RaidListener implements Listener {
	private static RaidListener instance;

	static {
		RaidListener.instance = new RaidListener();
	}

	public RaidListener() {
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
		} else {
			Player p = e.getEntity();
			Map m = MapManager.get().getMap(p);
			if (m != null) {
				e.setDeathMessage(null);
				e.setKeepInventory(true);
				m.kickPlayer(p, "You died!", false);
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		HashMap<Map, Integer> edit = MapManager.get().getEdit();
		List<Player> editors = MapManager.get().getEditors();
		Map map = MapManager.get().getMap(p);
		if (editors.contains(p) && map == null) {
			if (e.getClickedBlock().getType() == Material.STONE_BUTTON && e.getAction() == Action.LEFT_CLICK_BLOCK) {
				if (edit.containsKey(MapManager.get().getMap("test"))) {
					Map m = MapManager.get().getMap("test");
					if (edit.get(m) == 21) {
						m.setButton2(1, e.getClickedBlock().getLocation());
						MessageManager.get().message(p, "You set button for " + m.getName());
						edit.clear();
						editors.clear();
						return;
					}
					if (edit.get(m) == 22) {
						m.setButton2(2, e.getClickedBlock().getLocation());
						MessageManager.get().message(p, "You set button for " + m.getName());
						edit.clear();
						editors.clear();
						return;
					}
					if (edit.get(m) == 11) {
						m.setButton1(1, e.getClickedBlock().getLocation());
						MessageManager.get().message(p, "You set button for " + m.getName());
						edit.clear();
						editors.clear();
						return;
					}
					if (edit.get(m) == 31) {
						m.setButton3(1, e.getClickedBlock().getLocation());
						MessageManager.get().message(p, "You set button for " + m.getName());
						edit.clear();
						editors.clear();
						return;
					}
				}
			}
		}
		if (map != null) {
			if (!map.isLocked()) {
				if (e.getClickedBlock() != null) {
					if (map.getButtons(2).contains(e.getClickedBlock().getLocation())) {
						if (p.getInventory().contains(map.getCard(1))) {
							if (map.getFloor() == 1) {
								p.teleport(map.getFloor(2));
								if (map.isFirst()) {
									map.miniBoss();
									map.setFirst(false);
								}
								map.setFloor(2);
								return;
							}
							if (map.getFloor() == 3) {
								p.teleport(map.getFloor(2));
								map.setFloor(2);
								return;
							}
						}
					}
					if (map.getButtons(3).contains(e.getClickedBlock().getLocation())) {
						if (p.getInventory().contains(map.getCard(2))) {
							if (map.getFloor() == 2) {
								p.teleport(map.getFloor(3));
								map.setFloor(3);
								return;
							}
						}
					}
					if (map.getButtons(1).contains(e.getClickedBlock().getLocation())) {
						if (map.getFloor() == 2) {
							p.teleport(map.getFloor(1));
							map.setFloor(1);
							return;
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onNPCHit(NPCDamageByEntityEvent e) {
		NPC npc = e.getNPC();
		if (e.getDamager() instanceof Player || e.getDamager() instanceof Arrow) {
			if (npc.hasTrait(MiniTrait.class)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onNPCDamage(NPCDamageEntityEvent e) {
		if (e.getDamaged() instanceof Player) {
			Player p = (Player) e.getDamaged();
			NPC npc = e.getNPC();
			Map m = MapManager.get().getMap(npc);
			if (m != null) {
				if (Mob.get().getRound(1).contains(npc)) {
					e.setDamage(4);
				}
				if (Mob.get().getRound(2).contains(npc)) {
					e.setDamage(5);
				}
				if (m.getBoss().containsValue(npc)) {
					if (m.getBoss().get(1) != null) {
						if (m.getBoss().get(1).equals(npc)) {
							if (!p.hasPotionEffect(PotionEffectType.SLOW)) {
								e.setDamage(6);
								p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 90, 2));
								p.sendMessage(ChatColor.RED + "You have been slowed by the Guard!");
							}
						}
					}
				}
				if (m.getRoundNPC(3).contains(npc)) {
					e.setDamage(6);
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
			if (LawnMower.get().getParts().containsValue(e.getRightClicked())) {
				e.setCancelled(true);
				LawnMower.get().setPassanger(e.getPlayer());
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
