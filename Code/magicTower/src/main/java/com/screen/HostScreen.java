package com.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.asciiPanel.AsciiPanel;
import com.net.MyServer;
import com.world.SnapShot;
import com.world.World;
import com.world.WorldBuilder;
import com.world.creature.Creature;
import com.world.creature.CreatureFactory;

import java.awt.event.KeyEvent;

public class HostScreen extends PlayScreen{
    private MyServer server;

    public HostScreen(){
        this.world=WorldBuilder.buildMaze(screenWidth, screenWidth);
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();
        this.player = CreatureFactory.newPlayer(this.messages,world,0);
        this.scheduledExecutorService = Executors.newScheduledThreadPool(10);
        this.server=new MyServer(world);
        world.activateThreads();
        scheduledExecutorService.scheduleAtFixedRate(sendThread(), 0, 50, TimeUnit.MILLISECONDS);
    }

    private Thread sendThread(){
        return new Thread(()->{
            SnapShot shot=new SnapShot(world.currentFloor());
            server.sendSnapShot(new SnapShot(world.currentFloor()));
        });
    }

    @Override
    protected void displayStatus(AsciiPanel terminal, Map<String, String> status) {
        final int offsetX = FLOOR_WIDTH + 4;
        int x = 0;
        int y = 0;
        for (String key : status.keySet()) {
            terminal.write(key + ": " + status.get(key), x + offsetX, y);
            y++;
            y++;
        }
        //displayItems(terminal, y);
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        // Terrain and creatures
        displayFloor(terminal);
        displayStatus(terminal, player.status());
        //displayItemInfo(terminal);
        //displayMonsterInfo(terminal);
        //displayMessages(terminal, this.messages);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.moveBy(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                player.moveBy(1, 0);
                break;
            case KeyEvent.VK_UP:
                player.moveBy(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                player.moveBy(0, 1);
                break;
            case KeyEvent.VK_ESCAPE:
                return new OptionScreen(this.shutdwonThreads());
        }
        return this;
    }

    @Override
    public Screen checkPlayer() {
        if (player.isAlive())
            return this;
        else
            return new DeadScreen();
    }

}
