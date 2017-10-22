package com.likeapig.missions;

import java.io.*;
import org.bukkit.plugin.*;
import org.bukkit.configuration.file.*;
import org.bukkit.configuration.*;

public class Settings
{
    private static Settings instance;
    private File mapFile;
    private FileConfiguration mapConfig;
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
        if (!this.mapFile.exists()) {
            try {
                this.mapFile.createNewFile();
            }
            catch (Exception e) {
                p.getLogger().info("Failed to generate map file!");
                e.printStackTrace();
            }
        }
        this.mapConfig = (FileConfiguration)YamlConfiguration.loadConfiguration(this.mapFile);
    }
    
    public void set(final String path, final Object value) {
        this.mapConfig.set(path, value);
        try {
            this.mapConfig.save(this.mapFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public <T> T get(final String path) {
        return (T)this.mapConfig.get(path);
    }
    
    public ConfigurationSection getConfigSection() {
        return this.mapConfig.getConfigurationSection("maps");
    }
    
    public ConfigurationSection createConfiguration(final String path) {
        final ConfigurationSection s = this.mapConfig.createSection(path);
        try {
            this.mapConfig.save(this.mapFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
    
    public Plugin getPlugin() {
        return this.plugin;
    }
}
