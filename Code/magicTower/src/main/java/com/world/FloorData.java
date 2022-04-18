package com.world;

import java.io.Serializable;
import java.util.List;

import com.world.creature.Creature;
import com.world.tile.Tile;

import javafx.util.Pair;

public class FloorData implements Serializable{
    private String[][] map;
    private int width;
    private int height;
    private Tile[][] tiles;
    private List<Creature> creatures;

    private Pair<Integer, Integer> upstairCoords;
    private Pair<Integer, Integer> downstairCoords;

    public FloorData(Floor floor){
        this.map=floor.map();
        this.width=floor.width();
        this.height=floor.height();
        this.tiles=floor.tiles();
        this.creatures=floor.creatures();
        this.upstairCoords=floor.upstairCoords();
        this.downstairCoords=floor.downstairCoords();
    }

    public String[][]map(){return map;}

    public int width(){return width;}

    public int height(){return height;}

    public Tile[][]tiles(){return tiles;}

    public List<Creature> creatures(){return creatures;}

    public Pair<Integer, Integer> upstairCoords(){return upstairCoords;}

    public Pair<Integer, Integer> downstairCoords(){return downstairCoords;}

}