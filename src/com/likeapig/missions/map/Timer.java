package com.likeapig.missions.map;

import java.util.*;

public class Timer
{
    private static Timer instance;
    private static List<Countdown> cd;
    
    static {
        Timer.instance = new Timer();
    }
    
    public static Timer get() {
        return Timer.instance;
    }
    
    private Timer() {
        Timer.cd = new ArrayList<Countdown>();
    }
    
    public Timer createTimer(final Map a, final String arg, final int time) {
        Timer.cd.add(new Countdown(a, arg, time));
        return this;
    }
    
    public void startTimer(final Map a, final String arg) {
        if (Timer.cd.size() > 0) {
            for (final Countdown c : Timer.cd) {
                if (c.getMap().equals(a) && c.getString().equals(arg)) {
                    c.run();
                }
            }
        }
    }
    
    public void stopTasks(final Map a) {
        try {
            if (Timer.cd.size() <= 0) {
                return;
            }
            final List<Countdown> clone = new ArrayList<Countdown>();
            for (final Countdown c : Timer.cd) {
                clone.add(c);
            }
            for (final Countdown c : clone) {
                if (c.getMap().equals(a)) {
                    Timer.cd.remove(c);
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public static boolean isCanceled(final Map a, final String arg) {
        if (Timer.cd.size() > 0) {
            for (final Countdown c : Timer.cd) {
                if (c.getMap().equals(a) && c.getString().equals(arg)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static void onTimerEnd(final Countdown c) {
        if (Timer.cd.contains(c)) {
            Timer.cd.remove(c);
        }
    }
    
    public static void updateCd(final Countdown o, final Countdown n) {
        if (Timer.cd.contains(o)) {
            Timer.cd.remove(o);
            Timer.cd.add(n);
        }
    }
}
