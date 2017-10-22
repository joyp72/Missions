package com.likeapig.missions.map;

import org.bukkit.*;
import org.bukkit.ChatColor;

import com.likeapig.missions.*;
import com.likeapig.missions.utils.*;
import java.util.*;
import java.util.List;

import org.bukkit.entity.*;
import net.md_5.bungee.api.*;
import com.likeapig.missions.commands.*;

public class Map
{
    private String name;
    private static List<Data> datas;
    private List<LivingEntity> mobs;
    private List<Location> doors;
    private Location spawn;
    private Location bossLoc;
    private Location door1;
    private Location door2;
    private Location door3;
    private Location door4;
    private MapState state;
    
    public Map(final String s) {
        this.mobs = Mob.get().getMobs();
        this.doors = new ArrayList<Location>();
        this.state = MapState.STOPPED;
        this.name = s;
        Map.datas = new ArrayList<Data>();
        this.loadFromConfig();
        if (this.door1 != null && this.door2 != null && this.door3 != null && this.door4 != null) {
            this.doors.add(this.door1);
            this.doors.add(this.door2);
            this.doors.add(this.door3);
            this.doors.add(this.door4);
        }
        this.saveToConfig();
        this.checkState();
    }
    
    public void saveToConfig() {
        if (this.spawn != null) {
            Settings.get().set("maps." + this.getName() + ".spawn", LocationUtils.locationToString(this.spawn));
        }
        if (this.bossLoc != null) {
            Settings.get().set("maps." + this.getName() + ".bossLoc", LocationUtils.locationToString(this.bossLoc));
        }
        if (this.door1 != null) {
            Settings.get().set("maps." + this.getName() + ".door1", LocationUtils.locationToString(this.door1));
        }
        if (this.door2 != null) {
            Settings.get().set("maps." + this.getName() + ".door2", LocationUtils.locationToString(this.door2));
        }
        if (this.door3 != null) {
            Settings.get().set("maps." + this.getName() + ".door3", LocationUtils.locationToString(this.door3));
        }
        if (this.door4 != null) {
            Settings.get().set("maps." + this.getName() + ".door4", LocationUtils.locationToString(this.door4));
        }
    }
    
    public void loadFromConfig() {
        final Settings s = Settings.get();
        if (s.get("maps." + this.getName() + ".spawn") != null) {
            final String s2 = s.get("maps." + this.getName() + ".spawn");
            (this.spawn = LocationUtils.stringToLocation(s2)).setPitch(LocationUtils.stringToPitch(s2));
            this.spawn.setYaw(LocationUtils.stringToYaw(s2));
        }
        if (s.get("maps." + this.getName() + ".bossLoc") != null) {
            final String s3 = s.get("maps." + this.getName() + ".bossLoc");
            (this.bossLoc = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
            this.bossLoc.setYaw(LocationUtils.stringToYaw(s3));
        }
        if (s.get("maps." + this.getName() + ".door1") != null) {
            final String s3 = s.get("maps." + this.getName() + ".door1");
            (this.door1 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
            this.door1.setYaw(LocationUtils.stringToYaw(s3));
        }
        if (s.get("maps." + this.getName() + ".door2") != null) {
            final String s3 = s.get("maps." + this.getName() + ".door2");
            (this.door2 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
            this.door2.setYaw(LocationUtils.stringToYaw(s3));
        }
        if (s.get("maps." + this.getName() + ".door3") != null) {
            final String s3 = s.get("maps." + this.getName() + ".door3");
            (this.door3 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
            this.door3.setYaw(LocationUtils.stringToYaw(s3));
        }
        if (s.get("maps." + this.getName() + ".door4") != null) {
            final String s3 = s.get("maps." + this.getName() + ".door4");
            (this.door4 = LocationUtils.stringToLocation(s3)).setPitch(LocationUtils.stringToPitch(s3));
            this.door4.setYaw(LocationUtils.stringToYaw(s3));
        }
    }
    
    public void onTimerTick(final String arg, final int timer) {
        arg.equalsIgnoreCase("endround");
    }
    
    public void onTimerEnd(final String arg) {
        if (arg.equalsIgnoreCase("round1end")) {
            this.stop();
        }
    }
    
    private void checkState() {
        boolean flag = false;
        if (this.spawn == null) {
            flag = true;
        }
        if (this.bossLoc == null) {
            flag = true;
        }
        if (this.door1 == null) {
            flag = true;
        }
        if (this.door2 == null) {
            flag = true;
        }
        if (this.door3 == null) {
            flag = true;
        }
        if (this.door4 == null) {
            flag = true;
        }
        if (flag) {
            this.setState(MapState.STOPPED);
            return;
        }
        this.setState(MapState.WAITING);
    }
    
    public void setState(final MapState a) {
        this.state = a;
    }
    
    public void start() {
        this.setState(MapState.STARTED);
        this.firstRound();
    }
    
    public void firstRound() {
        Mob.get().spawnBoss(this.bossLoc);
        for (final Location door : this.doors) {
            Mob.get().spawnRound1(door);
        }
        Timer.get().createTimer(this.getMap(), "round1end", 60).startTimer(this.getMap(), "round1end");
    }
    
    public void addPlayer(final Player p) {
        if (!this.containsPlayer(p) && this.state.canJoin() && this.getNumberOfPlayers() < 3) {
            final Data d = new Data(p, this);
            Map.datas.add(d);
            p.teleport(this.spawn);
            this.message(ChatColor.GREEN + p.getName() + " joined the raid!");
            if (this.getNumberOfPlayers() == 3) {
                this.start();
            }
        }
    }
    
    public void removePlayer(final Player p) {
        if (this.containsPlayer(p)) {
            final Data d = this.getData(p);
            d.restore();
            Map.datas.remove(d);
            if (this.state.equals(MapState.STARTING) || (this.state.equals(MapState.STARTED) && this.getNumberOfPlayers() < 2)) {
                this.stop();
                this.message(ChatColor.RED + "Players left, stopping game.");
            }
        }
    }
    
    public void kickAll(final boolean b) {
        for (final Player p : this.getPlayers()) {
            if (b) {
                this.kickPlayer(p);
            }
            else {
                this.removePlayer(p);
            }
        }
    }
    
    public void kickPlayer(final Player p) {
        this.kickPlayer(p, "", true);
    }
    
    public void kickPlayer(final Player p, final String message, final boolean showLeaveMessage) {
        if (message != "") {
            MessageManager.get().message(p, "Kicked for: " + message);
        }
        this.removePlayer(p);
        if (showLeaveMessage) {
            this.message(ChatColor.RED + p.getName() + " left the arena!");
            MessageManager.get().message(p, "You left the arena!", MessageManager.MessageType.BAD);
        }
    }
    
    public void stop() {
        this.setState(MapState.WAITING);
        this.kickAll(true);
        for (final LivingEntity le : this.mobs) {
            le.remove();
        }
    }
    
    public boolean isStarted() {
        return this.state.equals(MapState.STARTED);
    }
    
    public int getNumberOfPlayers() {
        return Map.datas.size();
    }
    
    public Location getSpawn() {
        return this.spawn;
    }
    
    public void setSpawn(final Location l) {
        this.spawn = l;
        this.checkState();
        this.saveToConfig();
    }
    
    public void setBossLoc(final Location l) {
        this.bossLoc = l;
        this.checkState();
        this.saveToConfig();
    }
    
    public void setDoorLoc1(final Location l) {
        this.door1 = l;
        this.checkState();
        this.saveToConfig();
    }
    
    public void setDoorLoc2(final Location l) {
        this.door2 = l;
        this.checkState();
        this.saveToConfig();
    }
    
    public void setDoorLoc3(final Location l) {
        this.door3 = l;
        this.checkState();
        this.saveToConfig();
    }
    
    public void setDoorLoc4(final Location l) {
        this.door4 = l;
        this.checkState();
        this.saveToConfig();
    }
    
    public Location getBossLoc() {
        return this.bossLoc;
    }
    
    public void message(final String message) {
        for (final Player p : this.getPlayers()) {
            MessageManager.get().message(p, message);
        }
    }
    
    public List<Player> getPlayers() {
        final List<Player> players = new ArrayList<Player>();
        for (final Data d : Map.datas) {
            if (d.getMap() == this) {
                players.add(d.getPlayer());
            }
        }
        return players;
    }
    
    public List<String> getPNames() {
        final List<String> names = new ArrayList<String>();
        for (final Data d : Map.datas) {
            if (d.getMap() == this) {
                names.add(d.getPlayer().getName());
            }
        }
        return names;
    }
    
    public boolean containsPlayer(final Player p) {
        for (final Data d : Map.datas) {
            if (d.getMap() == this && d.getPlayer().equals(p)) {
                return true;
            }
        }
        return false;
    }
    
    public Data getData(final Player p) {
        for (final Data d : Map.datas) {
            if (d.getMap() == this && d.getPlayer().equals(p)) {
                return d;
            }
        }
        return null;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Map getMap() {
        return this;
    }
    
    public String getStateName() {
        return this.state.getName();
    }
    
    public MapState getState() {
        return this.state;
    }
    
    public void onRemoved() {
        if (this.isStarted()) {
            this.stop();
        }
        else {
            for (final Player p : this.getPlayers()) {
                this.removePlayer(p);
            }
        }
        this.setState(MapState.STOPPED);
    }
    
    public enum MapState
    {
        WAITING("WAITING", 0, "WAITING", 0, "WAITING", true), 
        STARTING("STARTING", 1, "STARTING", 1, "STARTING", true), 
        STARTED("STARTED", 2, "STARTED", 2, "STARTED", false), 
        STOPPED("STOPPED", 3, "STOPPED", 3, "STOPPED", false);
        
        private boolean allowJoin;
        private String name;
        
        private MapState(final String s2, final int n2, final String s, final int n, final String name, final Boolean allowJoin) {
            this.allowJoin = allowJoin;
            this.name = name;
        }
        
        public boolean canJoin() {
            return this.allowJoin;
        }
        
        public String getName() {
            return this.name;
        }
    }
}
