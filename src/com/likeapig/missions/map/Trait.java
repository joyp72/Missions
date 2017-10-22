package com.likeapig.missions.map;

import org.bukkit.event.EventHandler;

import com.likeapig.missions.Main;

import net.citizensnpcs.api.util.DataKey;

//This is your trait that will be applied to a npc using the /trait mytraitname command. Each NPC gets its own instance of this class.
//the Trait class has a reference to the attached NPC class through 'npc' or getNPC().
//The Trait class also implements Listener so you can add EventHandlers directly to your trait.
public class Trait extends net.citizensnpcs.api.trait.Trait {

	public Trait() {
		super("mytraitname");
		plugin = Main.get();
	}

	Main plugin = null;

	boolean SomeSetting = false;

	// Here you should load up any values you have previously saved.
	// This does NOT get called when applying the trait for the first time, only
	// loading onto an existing npc at server start.
	// This is called AFTER onAttach so you can load defaults in onAttach and they
	// will be overridden here.
	// This is called BEFORE onSpawn so do not try to access npc.getBukkitEntity().
	// It will be null.
	public void load(DataKey key) {
		SomeSetting = key.getBoolean("SomeSetting", plugin.getConfig().getBoolean("Defaults.SomeSetting"));
	}

	// Save settings for this NPC. These values will be added to the citizens
	// saves.yml under this NPC.
	public void save(DataKey key) {
		key.setBoolean("SomeSetting", SomeSetting);
	}

	@EventHandler
	public void click(net.citizensnpcs.api.event.NPCClickEvent event) {
		// Handle a click on a NPC. The event has a getNPC() method.
		// Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on
		// this NPC!

	}

	// Run code when your trait is attached to a NPC.
	// This is called BEFORE onSpawn so do not try to access npc.getBukkitEntity().
	// It will be null.
	@Override
	public void onAttach() {
		plugin.getServer().getLogger().info(npc.getName() + "has been assigned MyTrait!");

		// This will send a empty key to the Load method, forcing it to load the
		// config.yml defaults.
		// Load will get called again with a real key if this NPC has previously been
		// saved
		load(new net.citizensnpcs.api.util.MemoryDataKey());
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

	}

	// run code when the NPC is removed. Use this to tear down any repeating tasks.
	@Override
	public void onRemove() {
	}

}
