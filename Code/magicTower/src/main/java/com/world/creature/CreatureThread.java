package com.world.creature;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class CreatureThread extends Thread implements Serializable{
    private Creature creature;
    private final int SLEEP_TIME = 1000; // TimeUnit: MILLISECONDS
    private volatile boolean isShutdownRequested=false;

    public CreatureThread(Creature creature,String name) {
        setName(name);
        this.creature = creature;
        creature.setThread(this);
    }

    @Override
    public void run() {
        try {
            while (!isShutdownRequested) {
                if (!creature.isAlive()) {
                    shutdown();
                }
                creature.update();
                Thread.sleep(SLEEP_TIME);
            }
        } catch (InterruptedException e) {
            System.out.println(this + " Exiting via interrupt");
        }
    }

    private void shutdown(){
        interrupt();
        isShutdownRequested=true;
    }
}
