package com.likeapig.missions.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.likeapig.missions.Main;
import com.likeapig.missions.utils.ParticleEffect;

public class Test extends Commands {
	public Test() {
		super("raid.admin", "Test", "", new String[] { "t" });
	}

	int i = 0;

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			Location loc = p.getLocation().clone().add(0.0, 1.6, 0);
			@Override
			public void run() {
				
				loc.add(loc.getDirection().multiply(1).normalize());
				displayColoredParticle(loc, "ffb90f");
			}
		}, 0L, 0L);
	}
	
	public static void displayColoredParticle(Location loc, ParticleEffect type, String hexVal, float xOffset,
			float yOffset, float zOffset) {
		int R = 0;
		int G = 0;
		int B = 0;

		if (hexVal.length() <= 6) {
			R = Integer.valueOf(hexVal.substring(0, 2), 16);
			G = Integer.valueOf(hexVal.substring(2, 4), 16);
			B = Integer.valueOf(hexVal.substring(4, 6), 16);
			if (R <= 0) {
				R = 1;
			}
		} else if (hexVal.length() <= 7 && hexVal.substring(0, 1).equals("#")) {
			R = Integer.valueOf(hexVal.substring(1, 3), 16);
			G = Integer.valueOf(hexVal.substring(3, 5), 16);
			B = Integer.valueOf(hexVal.substring(5, 7), 16);
			if (R <= 0) {
				R = 1;
			}
		}

		loc.setX(loc.getX() + Math.random() * (xOffset / 2 - -(xOffset / 2)));
		loc.setY(loc.getY() + Math.random() * (yOffset / 2 - -(yOffset / 2)));
		loc.setZ(loc.getZ() + Math.random() * (zOffset / 2 - -(zOffset / 2)));

		if (type == ParticleEffect.RED_DUST || type == ParticleEffect.REDSTONE) {
			ParticleEffect.RED_DUST.display(R, G, B, 0.004F, 0, loc, 255.0);
		} else if (type == ParticleEffect.SPELL_MOB || type == ParticleEffect.MOB_SPELL) {
			ParticleEffect.SPELL_MOB.display((float) 255 - R, (float) 255 - G, (float) 255 - B, 1, 0, loc, 255.0);
		} else if (type == ParticleEffect.SPELL_MOB_AMBIENT || type == ParticleEffect.MOB_SPELL_AMBIENT) {
			ParticleEffect.SPELL_MOB_AMBIENT.display((float) 255 - R, (float) 255 - G, (float) 255 - B, 1, 0, loc,
					255.0);
		} else {
			ParticleEffect.RED_DUST.display(0, 0, 0, 0.004F, 0, loc, 255.0D);
		}
	}

	public static void displayColoredParticle(Location loc, String hexVal) {
		int R = 0;
		int G = 0;
		int B = 0;

		if (hexVal.length() <= 6) {
			R = Integer.valueOf(hexVal.substring(0, 2), 16);
			G = Integer.valueOf(hexVal.substring(2, 4), 16);
			B = Integer.valueOf(hexVal.substring(4, 6), 16);
			if (R <= 0) {
				R = 1;
			}
		} else if (hexVal.length() <= 7 && hexVal.substring(0, 1).equals("#")) {
			R = Integer.valueOf(hexVal.substring(1, 3), 16);
			G = Integer.valueOf(hexVal.substring(3, 5), 16);
			B = Integer.valueOf(hexVal.substring(5, 7), 16);
			if (R <= 0) {
				R = 1;
			}
		}
		ParticleEffect.RED_DUST.display(R, G, B, 0.004F, 0, loc, 257D);
	}

	public static void displayColoredParticle(Location loc, String hexVal, float xOffset, float yOffset,
			float zOffset) {
		int R = 0;
		int G = 0;
		int B = 0;

		if (hexVal.length() <= 6) {
			R = Integer.valueOf(hexVal.substring(0, 2), 16);
			G = Integer.valueOf(hexVal.substring(2, 4), 16);
			B = Integer.valueOf(hexVal.substring(4, 6), 16);
			if (R <= 0) {
				R = 1;
			}
		} else if (hexVal.length() <= 7 && hexVal.substring(0, 1).equals("#")) {
			R = Integer.valueOf(hexVal.substring(1, 3), 16);
			G = Integer.valueOf(hexVal.substring(3, 5), 16);
			B = Integer.valueOf(hexVal.substring(5, 7), 16);
			if (R <= 0) {
				R = 1;
			}
		}

		loc.setX(loc.getX() + Math.random() * (xOffset / 2 - -(xOffset / 2)));
		loc.setY(loc.getY() + Math.random() * (yOffset / 2 - -(yOffset / 2)));
		loc.setZ(loc.getZ() + Math.random() * (zOffset / 2 - -(zOffset / 2)));

		ParticleEffect.RED_DUST.display(R, G, B, 0.004F, 0, loc, 257D);
	}
}
