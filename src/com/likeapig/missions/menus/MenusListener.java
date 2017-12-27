package com.likeapig.missions.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.likeapig.missions.Main;
import com.likeapig.missions.commands.MessageManager;
import com.likeapig.missions.commands.MessageManager.MessageType;
import com.likeapig.missions.intro.Intro;
import com.likeapig.missions.intro.IntroManager;
import com.likeapig.missions.map.Map;
import com.likeapig.missions.map.MapManager;

public class MenusListener implements Listener {

	private static MenusListener instance;

	static {
		instance = new MenusListener();
	}

	public static MenusListener get() {
		return instance;
	}

	public void setup() {
		Bukkit.getPluginManager().registerEvents(this, Main.get());
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (Menus.getMenus() != null) {
			if (Menus.getMenus().containsValue(e.getInventory())) {
				if (e.getCurrentItem() == null) {
					return;
				}
				if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
					e.setCancelled(true);
				}
				if (e.getCurrentItem().getType() == Material.BOOK) {
					e.setCancelled(true);
					Intro i = IntroManager.get().getIntros().get(0);
					if (e.getWhoClicked() instanceof Player) {
						Player p = (Player) e.getWhoClicked();
						p.closeInventory();
						if (IntroManager.get().getIntro(p) != null) {
							MessageManager.get().message(p, "You are already in an Intro",
									MessageManager.MessageType.BAD);
							return;
						}
						if (MapManager.get().getMap(p) != null) {
							MessageManager.get().message(p, "You are already in an Mission",
									MessageManager.MessageType.BAD);
							return;
						}
						if (i.isStarted()) {
							MessageManager.get().message(p, "Intro is being used!", MessageType.BAD);
							return;
						}
						i.addPlayer(p);
					}
				}
				if (e.getCurrentItem().getType() == Material.IRON_BLOCK) {
					e.setCancelled(true);
					Map i = MapManager.get().getMaps().get(0);
					if (e.getWhoClicked() instanceof Player) {
						Player p = (Player) e.getWhoClicked();
						p.closeInventory();
						if (MapManager.get().getMap(p) != null) {
							MessageManager.get().message(p, "You are already in an Map",
									MessageManager.MessageType.BAD);
							return;
						}
						if (IntroManager.get().getIntro(p) != null) {
							MessageManager.get().message(p, "You are already in an Intro",
									MessageManager.MessageType.BAD);
							return;
						}
						if (p.getInventory().contains(Material.REDSTONE)
								|| p.getInventory().contains(Material.REDSTONE_BLOCK)) {
							MessageManager.get().message(p, "Redstone is not allowed inside the mission!",
									MessageType.BAD);
							return;
						}
						if (p.getInventory().contains(Material.ENDER_PEARL)) {
							MessageManager.get().message(p, "Ender Pearls is not allowed inside the mission!",
									MessageType.BAD);
							return;
						}
						if (p.getInventory().getItemInOffHand().getType() != Material.SHIELD) {
							if (!(p.getInventory().contains(Material.SHIELD))) {
								MessageManager.get().message(p, "This mission requires you to equip a shield.",
										MessageType.BAD);
								return;
							}
						}
						if (i.isStarted()) {
							MessageManager.get().message(p, "Map is being used!", MessageType.BAD);
							return;
						}
						i.addPlayer(p);
					}
				}
				if (e.getCurrentItem().getType() == Material.BEDROCK) {
					e.setCancelled(true);
					if (e.getWhoClicked() instanceof Player) {
						Player p = (Player) e.getWhoClicked();
						p.closeInventory();
						Intro i = IntroManager.get().getIntro(p);
						Map m = MapManager.get().getMap(p);
						if (m == null && i == null) {
							MessageManager.get().message(p, "You are not in a mission or intro.", MessageType.BAD);
							return;
						}
						if (m != null) {
							m.kickPlayer(p);
							return;
						}
						if (i != null) {
							i.removePlayer(p);
							return;
						}
					}
				}
			}
		}
	}

}
