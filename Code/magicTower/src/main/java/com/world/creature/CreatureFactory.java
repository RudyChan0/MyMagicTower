package com.world.creature;


import java.io.Serializable;
import java.security.Guard;
import java.util.List;

import com.world.World;
import java.awt.Color;

import com.world.Floor;
import com.asciiPanel.AsciiPanel;

public class CreatureFactory {
    // private World world;

    // public CreatureFactory(World world) {
    //     this.world = world;
    // }

    public static Creature newPlayer(List<String> messages,World world){
        Creature player = new Creature(world, (char)2, AsciiPanel.brightWhite, 40, 15, 5, 5);
        world.setPlayer(player);
        world.addPlayerToBegin(player);
        new PlayerAI(player,messages);
        new CreatureThread(player,"player");
        return player;
    }

    public static Creature newPlayer(List<String> messages,World world,int id){
        Color[] colors={AsciiPanel.brightWhite,AsciiPanel.brightBlue,AsciiPanel.brightYellow,AsciiPanel.brickRed};
        Creature player = new Creature(world, (char)2, colors[id], 40, 10, 5, 5);
        world.addPlayerToMaze(player,id);
        new PlayerAI(player,messages);
        new CreatureThread(player,"player");
        return player;
    }

    public static Creature newMonster(World world){
        Creature monster = new Creature(world, (char)64, AsciiPanel.brightCyan, 20, 10, 5, 2);
        new MonsterAI(monster);
        new CreatureThread(monster,"monster");
        return monster;
    }

    public static Creature newDetector(World world){
        Creature detector = new Creature(world, (char)143, AsciiPanel.green, 10, 20, 5, 4);
        new DetectorAI(detector);
        new CreatureThread(detector,"detetor");
        return detector;
    }

    public static Creature newGuard(World world){
        Creature guard = new Creature(world, (char)234, AsciiPanel.cyan, 30, 10, 10, 1);
        new GuardAI(guard);
        new CreatureThread(guard,"guard");
        return guard;
    }
}
