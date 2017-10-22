package com.likeapig.missions.map;

import org.bukkit.scheduler.*;
import com.likeapig.missions.*;
import org.bukkit.plugin.*;

public class Countdown extends BukkitRunnable
{
    private int timer;
    private Map a;
    private boolean done;
    private String arg;
    
    public Countdown(final Map a, final String arg, final int time) {
        this.timer = time;
        this.arg = arg;
        this.a = a;
    }
    
    public Map getMap() {
        return this.a;
    }
    
    public String getString() {
        return this.arg;
    }
    
    public int getTimer() {
        return this.timer;
    }
    
    public void run() {
        Timer.get();
        if (Timer.isCanceled(this.a, this.arg)) {
            return;
        }
        if (this.timer <= 0) {
            Timer.onTimerEnd(this);
            this.a.onTimerEnd(this.arg);
            return;
        }
        this.a.onTimerTick(this.arg, this.timer);
        --this.timer;
        final Countdown c = new Countdown(this.a, this.arg, this.timer);
        Timer.updateCd(this, c);
        c.runTaskLater((Plugin)Main.get(), 20L);
    }
    
    public boolean isDone() {
        return this.done;
    }
}
