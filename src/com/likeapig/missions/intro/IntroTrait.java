package com.likeapig.missions.intro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftStructureBlock;
import org.bukkit.event.EventHandler;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;

@TraitName("IntroTrait")
public class IntroTrait extends Trait {

	Intro intro = IntroManager.get().getIntro("intro");

	public IntroTrait() {
		super("IntroTrait");
	}

	@Persist("mysettingname")
	boolean automaticallyPersistedSetting = false;

	public void load(DataKey key) {
	}

	public void save(DataKey key) {
	}

	@EventHandler
	public void click(net.citizensnpcs.api.event.NPCClickEvent event) {

	}

	@Override
	public void run() {
	}

	@Override
	public void onAttach() {
		npc.data().set(NPC.DEFAULT_PROTECTED_METADATA, true);
	}

	@Override
	public void onDespawn() {
	}

	@Override
	public void onSpawn() {
	}

	@Override
	public void onRemove() {
	}
}
