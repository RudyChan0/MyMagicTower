package com.world;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.util.Pair;
import java.awt.Color;
import java.io.Serializable;

import com.world.creature.Creature;
import com.world.creature.CreatureFactory;
import com.world.item.ItemFactory;
import com.world.tile.Tile;
import com.world.tile.TileFactory;
import com.world.tile.TileKind;

public class Floor{
    private World world;
    private String[][] map;
    private int width;
    private int height;
    private Tile[][] tiles;
    private List<Creature> creatures;

    private Pair<Integer, Integer> upstairCoords;
    private Pair<Integer, Integer> downstairCoords;

    private ExecutorService executorService;
    private final int UPDATE_TIME = 1; // TimeUnit:second

    public void update() {
        creatures.forEach(Creature::update);
    }

    public void activateThreads() {
        executorService = Executors.newCachedThreadPool();
        creatures.forEach(creature -> {
            executorService.submit(creature.thread());
        });
    }

    public void shutdwonThreads() {
        executorService.shutdownNow();
    }

    public void addCreature(Creature creature, int x, int y) {
        creatures.add(creature);
        creature.setFloor(this);
        creature.setX(x);
        creature.setY(y);
    }

    public void remove(Creature creature) {
        creatures.remove(creature);
    }

    public void changeTile(int x,int y,TileKind kind){
        tiles[x][y]=TileFactory.newTile(kind);
    }

    public Floor setWorld(World world){
        this.world=world;
        return this;
    }

    public Floor buildFromMap() {
        height = map.length;
        width = map[0].length;
        tiles = new Tile[height][width];
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                switch (map[h][w]) {
                    case "0":
                        tiles[w][h] = TileFactory.newFloor();
                        break;
                    case "1":
                        tiles[w][h] = TileFactory.newWall();
                        break;
                    case "upstair":
                        tiles[w][h] = TileFactory.newUpstair();
                        upstairCoords = new Pair<>(w, h);
                        break;
                    case "downstair":
                        tiles[w][h] = TileFactory.newDownstair();
                        downstairCoords = new Pair<>(w, h);
                        break;
                    case "door_yellow":
                        tiles[w][h] = TileFactory.newDoorYellow();
                        break;
                    case "door_blue":
                        tiles[w][h] = TileFactory.newDoorBlue();
                        break;
                    case "door_red":
                        tiles[w][h] = TileFactory.newDoorRed();
                        break;
                    case "monster":
                        tiles[w][h] = TileFactory.newFloor();
                        addCreature(CreatureFactory.newMonster(world), w, h);
                        break;
                    case "detector":
                        tiles[w][h] = TileFactory.newFloor();
                        addCreature(CreatureFactory.newDetector(world), w, h);
                        break;
                    case "guard":
                        tiles[w][h] = TileFactory.newFloor();
                        addCreature(CreatureFactory.newGuard(world), w, h);
                        break;
                    case "heart":
                        tiles[w][h] = TileFactory.newFloor().setItem(ItemFactory.newHeart());
                        break;
                    case "attack":
                        tiles[w][h] = TileFactory.newFloor().setItem(ItemFactory.newAttack());
                        break;
                    case "defence":
                        tiles[w][h] = TileFactory.newFloor().setItem(ItemFactory.newDefence());
                        break;
                    case "key_yellow":
                        tiles[w][h] = TileFactory.newFloor().setItem(ItemFactory.newKeyYellow());
                        break;
                    case "key_blue":
                        tiles[w][h] = TileFactory.newFloor().setItem(ItemFactory.newKeyBlue());
                        break;
                    case "key_red":
                        tiles[w][h] = TileFactory.newFloor().setItem(ItemFactory.newKeyRed());
                        break;

                }
            }
        }

        return this;
    }

    public Creature creature(int x, int y) {
        for (Creature creature : creatures) {
            if (creature.x() == x && creature.y() == y)
                return creature;
        }
        return null;
    }

    public List<Creature> creatures() {
        return this.creatures;
    }

    public char glyph(int x, int y) {
        if (tiles[x][y].hasItem())
            return tiles[x][y].item().glyph();
        else
            return tiles[x][y].glyph();
    }

    public Color color(int x, int y) {
        if (tiles[x][y].hasItem())
            return tiles[x][y].item().color();
        else
            return tiles[x][y].color();
    }

    public Tile tile(int x, int y) {
        return tiles[x][y];
    }

    public Tile[][] tiles(){
        return tiles;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public Floor setMap(String[][] map) {
        this.map = map;
        return this;
    }

    public String[][] map() {
        return map;
    }

    public Pair<Integer, Integer> upstairCoords() {
        return upstairCoords;
    }

    public Pair<Integer, Integer> downstairCoords() {
        return downstairCoords;
    }

    public Floor() {
        map = null;
        tiles = null;
        creatures = new ArrayList<>();

    }

    public FloorData getFloorData(){
        return new FloorData(this);
    }

    public static Floor dataToFloor(FloorData floorData,World world){
        Floor floor=new Floor();
        floor.world=world;

        floor.map=floorData.map();
        floor.width=floorData.width();
        floor.height=floorData.height();
        floor.tiles=floorData.tiles();
        floor.creatures=floorData.creatures();
        floor.upstairCoords=floorData.upstairCoords();
        floor.downstairCoords=floorData.downstairCoords();
        floor.creatures.forEach(creature->{
            creature.setFloor(floor);
            creature.setWorld(world);
        });
        return floor;
    }

    public static void main(String[] args) {
        // Floor floor = new Floor(1);
        int x = 0;
    }
}
