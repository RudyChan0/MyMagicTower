package com.world;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

import com.world.FloorData;
import com.world.creature.Creature;
import com.world.tile.Tile;

public class Archive implements Serializable {
    private String time;
    private int floorNum;
    private int width;
    private int height;
    private Creature player;
    private FloorData[] floorDatas;
    

    public Archive(World world){
        this.floorNum=world.floorNum();
        this.width=world.width();
        this.height=world.height();
        this.player=world.player();
        this.floorDatas=new FloorData[world.floors().length];
        for(int i=0;i<world.floors().length;i++){
            this.floorDatas[i]=world.floors()[i].getFloorData();
        }

        Date now=new Date();
        SimpleDateFormat ft=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.time=ft.format(now);
    }


    public static Archive saveWorld(World world) {
        return new Archive(world);
    }

    public World toWorld(){
        World world=new World(floorNum,width,height);
        Floor[] floors=buildfloors(world);
        world.setFloors(floors);
        world.setCurrentFloor(floors[world.floorNum()]);
        world.setPlayer(player(world));
        return world;
    }

    public String time(){return time;};

    public int floorNum(){return floorNum;}

    public int width(){return width;}

    public int height(){return height;}

    public Creature player(World world){
        player.setWorld(world);
        player.setFloor(world.currentFloor());
        return player;
    }

    public Floor[] buildfloors(World world){
        Floor[] floors=new Floor[this.floorDatas.length];
        for(int i=0;i<this.floorDatas.length;i++){
            floors[i]=Floor.dataToFloor(floorDatas[i], world);
        }
        return floors;
    }

    
}