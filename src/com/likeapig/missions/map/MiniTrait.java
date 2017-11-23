package com.likeapig.missions.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.likeapig.missions.Main;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;

@TraitName("MiniTrait")

public class MiniTrait extends Trait {
	public MiniTrait() {
		super("MiniTrait");
		plugin = JavaPlugin.getPlugin(Main.class);
	}

	Plugin plugin = null;
	Map map = MapManager.get().getMap("test");

	boolean SomeSetting = false;

	// see the 'Persistence API' section
	@Persist("mysettingname")
	boolean automaticallyPersistedSetting = false;

	// Here you should load up any values you have previously saved (optional).
	// This does NOT get called when applying the trait for the first time, only
	// loading onto an existing npc at server start.
	// This is called AFTER onAttach so you can load defaults in onAttach and they
	// will be overridden here.
	// This is called BEFORE onSpawn, npc.getBukkitEntity() will return null.
	public void load(DataKey key) {
		SomeSetting = key.getBoolean("SomeSetting", false);
	}

	// Save settings for this NPC (optional). These values will be persisted to the
	// Citizens saves file
	public void save(DataKey key) {
		key.setBoolean("SomeSetting", SomeSetting);
	}

	// An example event handler. All traits will be registered automatically as
	// Bukkit Listeners.
	@EventHandler
	public void click(net.citizensnpcs.api.event.NPCClickEvent event) {
		// Handle a click on a NPC. The event has a getNPC() method.
		// Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on
		// this NPC!

	}

	// Called every tick
	@Override
	public void run() {
		if (npc.isSpawned()) {
			for (Entity entity : getEntitiesAroundPoint(npc.getEntity().getLocation(), 4.0)) {
				if (map.getPlayer() != null) {
					if (entity.getUniqueId() == map.getPlayer().getUniqueId()) {
						Player p = (Player) entity;
						npc.getNavigator().setPaused(true);
						npc.faceLocation(p.getLocation());
					} else {
						//npc.getNavigator().setPaused(false);
					}
				}
			}
		}
	}

	// Run code when your trait is attached to a NPC.
	// This is called BEFORE onSpawn, so npc.getBukkitEntity() will return null
	// This would be a good place to load configurable defaults for new NPCs.
	@Override
	public void onAttach() {
		npc.data().set(NPC.DEFAULT_PROTECTED_METADATA, false);
		npc.data().set(NPC.DROPS_ITEMS_METADATA, false);
		npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, "z_kris_z");

	}

	// Run code when the NPC is despawned. This is called before the entity actually
	// despawns so npc.getBukkitEntity() is still valid.
	@Override
	public void onDespawn() {
	}

	// Run code when the NPC is spawned. Note that npc.getBukkitEntity() will be
	// null until this method is called.
	// This is called AFTER onAttach and AFTER Load when the server is started.
	@Override
	public void onSpawn() {
		npc.getNavigator().setTarget(map.getPlayer(), true);

	}

	// run code when the NPC is removed. Use this to tear down any repeating tasks.
	@Override
	public void onRemove() {
	}

	public List<Entity> getEntitiesAroundPoint(Location location, double radius) {
		List<Entity> entities = new ArrayList<Entity>();
		World world = location.getWorld();

		int smallX = (int) (location.getX() - radius) >> 4;
		int bigX = (int) (location.getX() + radius) >> 4;
		int smallZ = (int) (location.getZ() - radius) >> 4;
		int bigZ = (int) (location.getZ() + radius) >> 4;

		for (int x = smallX; x <= bigX; x++) {
			for (int z = smallZ; z <= bigZ; z++) {
				if (world.isChunkLoaded(x, z)) {
					entities.addAll(Arrays.asList(world.getChunkAt(x, z).getEntities()));
				}
			}
		}

		Iterator<Entity> entityIterator = entities.iterator();
		while (entityIterator.hasNext()) {
			Entity e = entityIterator.next();
			if (e.getWorld().equals(location.getWorld())
					&& e.getLocation().distanceSquared(location) > radius * radius) {
				entityIterator.remove();
			} else if (e instanceof Player && ((Player) e).getGameMode().equals(GameMode.SPECTATOR)) {
				entityIterator.remove();
			}
		}

		return entities;
	}

}
