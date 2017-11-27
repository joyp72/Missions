package com.likeapig.missions;

import java.io.*;
import java.util.List;

import org.bukkit.plugin.*;
import org.bukkit.configuration.file.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.configuration.*;

public class Settings {
	private static Settings instance;
	private File mapFile;
	private File lootFile;
	private FileConfiguration mapConfig;
	private FileConfiguration lootConfig;
	private Plugin plugin;

	static {
		Settings.instance = new Settings();
	}

	public static Settings get() {
		return Settings.instance;
	}

	public void setup(final Plugin p) {
		this.plugin = p;
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdirs();
		}
		this.mapFile = new File(p.getDataFolder() + "/maps.yml");
		lootFile = new File(p.getDataFolder() + "/loot.yml");
		if (!this.mapFile.exists()) {
			try {
				this.mapFile.createNewFile();
			} catch (Exception e) {
				p.getLogger().info("Failed to generate map file!");
				e.printStackTrace();
			}
		}
		if (!this.lootFile.exists()) {
			try {
				this.lootFile.createNewFile();
			} catch (Exception e) {
				p.getLogger().info("Failed to generate map file!");
				e.printStackTrace();
			}
		}
		this.mapConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(this.mapFile);
		this.lootConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(this.lootFile);
	}

	public void set(final String path, final Object value) {
		this.mapConfig.set(path, value);
		try {
			this.mapConfig.save(this.mapFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setLoot(final String path, List<String> value) {
		this.lootConfig.set(path, value);
		try {
			this.lootConfig.save(this.lootFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public <T> T get(final String path) {
		return (T) this.mapConfig.get(path);
	}

	public <T> T getLoot(final String path) {
		return (T) this.lootConfig.get(path);
	}

	public ConfigurationSection getConfigSection() {
		return this.mapConfig.getConfigurationSection("maps");
	}

	public ConfigurationSection getLootConfigSection() {
		return this.lootConfig.getConfigurationSection("loot");
	}

	public ConfigurationSection createConfiguration(final String path) {
		final ConfigurationSection s = this.mapConfig.createSection(path);
		try {
			this.mapConfig.save(this.mapFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public ConfigurationSection createLootConfiguration(final String path) {
		final ConfigurationSection s = this.lootConfig.createSection(path);
		try {
			this.lootConfig.save(this.lootFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public Plugin getPlugin() {
		return this.plugin;
	}
}
