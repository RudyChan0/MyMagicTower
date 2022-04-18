package com.world;

public class WorldBuilder {

    public static World build(int width,int height){
        World world=new World(width, height);
        world.setFloors(FloorBuilder.buildFloors(world));
        return world;
    }

    public static World buildMaze(int width,int height){
        World world=new World(width, height);
        world.setFloors(FloorBuilder.buildMaze(world));
        return world;
    }
    
}
