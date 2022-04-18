package com.world;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;

import javafx.util.Pair;
import com.world.creature.Creature;
import com.world.tile.Tile;

public class World {
    private Floor[] floors;
    private Floor currentFloor;
    private int floorNum;
    private Creature player;
    private int width;
    private int height;

    public World(int width,int height){
        this.floorNum=0;
        this.width=width;
        this.height=height;
    }

    public World(int floorNum,int width,int height){
        this.floorNum=floorNum;
        this.width=width;
        this.height=height;
    }

    //operate floor
    public void activateThreads(){
        currentFloor.activateThreads();
    }

    public void shutdwonThreads(){
        currentFloor.shutdwonThreads();
    }

    public void update(){
        currentFloor.update();
    }

    public void goUpstair(){
        currentFloor.shutdwonThreads();
        currentFloor.remove(player);
        floorNum++;
        currentFloor=floors[floorNum];
        Pair<Integer,Integer> downstairCoords=currentFloor.downstairCoords();
        currentFloor.addCreature(player, downstairCoords.getKey(), downstairCoords.getValue());
        currentFloor.activateThreads();
    }

    public void goDownstair(){
        currentFloor.shutdwonThreads();
        currentFloor.remove(player);
        floorNum--;
        currentFloor=floors[floorNum];
        Pair<Integer,Integer> upstairCoords=currentFloor.upstairCoords();
        currentFloor.addCreature(player, upstairCoords.getKey(), upstairCoords.getValue());
        currentFloor.activateThreads();
    }

    public List<Creature> creatures() {
        return currentFloor.creatures();
    }

    public char glyph(int x, int y) {
        return currentFloor.glyph(x, y);
    }

    public Color color(int x, int y) {
        return currentFloor.color(x, y);
    }

    public Tile tile(int x, int y) {
        return currentFloor.tile(x, y);
    }

    public void setPlayer(Creature player){
        this.player=player;
    }

    public void addPlayerToBegin(Creature player){
        floors[0].addCreature(player, 6, 11);
    }

    public void addPlayerToMaze(Creature player,int id){
        int[] xs={1,1,11,11};
        int[] ys={1,11,1,11};
        floors[0].addCreature(player, xs[id], ys[id]);
    }

    public void setFloors(Floor[] floors){
        this.floors=floors;
        this.currentFloor=floors[this.floorNum];
    }

    public void setCurrentFloor(Floor floor){
        this.currentFloor=floor;
    }

    public int floorNum(){
        return floorNum;
    }

    public Floor currentFloor(){
        return currentFloor;
    }

    public Floor[] floors(){
        return floors;
    }

    public Creature player(){
        return player;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

}