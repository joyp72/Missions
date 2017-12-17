package com.likeapig.missions.intro;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.likeapig.missions.Main;
import com.likeapig.missions.Settings;

public class IntroManager {
	public static IntroManager instance;
	public List<Intro> intros;

	static {
		instance = new IntroManager();
	}

	public static IntroManager get() {
		return instance;
	}
	
	private IntroManager() {
		intros = new ArrayList<Intro>();
	}
	
	public void registerIntro(String s) {
		if (getIntro(s) == null) {
			Intro i = new Intro(s);
			intros.add(i);
		}
	}
	
	public void setupIntros() {
		intros.clear();
		if (Settings.get().get("intros") != null) {
			for (String s : Settings.get().getConfigSection().getKeys(false)) {
				try {
					registerIntro(s);
				} catch (Exception ex) {
					Main.get().getLogger().info("Exception ocurred when loading intro: " + s);
					ex.printStackTrace();
				}
			}
		}
	}
	
	
	public void removeIntro(Intro i) {
		if (intros.contains(i)) {
			intros.remove(i);
			i.onRemoved();
		}
	}
	
	public Intro getIntro(String name) {
		for (Intro i : intros) {
			if (i.getName().equalsIgnoreCase(name)) {
				return i;
			}
		}
		return null;
	}
	
	public Intro getIntro(Player p) {
		for (Intro i : intros) {
			if (i.containsPlayer(p)) {
				return i;
			}
		}
		return null;
	}
	
	public List<Intro> getIntros() {
		return intros;
	}
	
}
