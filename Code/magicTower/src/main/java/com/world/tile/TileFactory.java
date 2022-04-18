package com.world.tile;

import com.asciiPanel.AsciiPanel;
import java.awt.Color;
import java.io.Serializable;

public class TileFactory {

    public static Tile newTile(TileKind kind){
        if(kind==TileKind.FLOOR)return newFloor();
        else if(kind==TileKind.WALL)return newWall();
        else if(kind==TileKind.UPSTAIR)return newUpstair();
        else if(kind==TileKind.DOWNSTAIR)return newDownstair();
        else if(kind==TileKind.DOOR_YELLOW)return newDoorYellow();
        else if(kind==TileKind.DOOR_BLUE)return newDoorBlue();
        else if(kind==TileKind.DOOR_RED)return newDoorRed();
        else return null;
    }

    public static Tile newFloor(){
        return new Tile(TileKind.FLOOR,(char)0 , AsciiPanel.coldGrey);
    }

    public static Tile newWall(){
        return new Tile(TileKind.WALL,(char) 178, AsciiPanel.wallRed);
    }

    public static Tile newUpstair(){
        return new Tile(TileKind.UPSTAIR,(char)24,AsciiPanel.brightGreen);
    }

    public static Tile newDownstair(){
        return new Tile(TileKind.DOWNSTAIR,(char)25,AsciiPanel.brightGreen);
    }

    public static Tile newDoorYellow(){
        return new Tile(TileKind.DOOR_YELLOW,(char)8,AsciiPanel.brightYellow);
    }

    public static Tile newDoorBlue(){
        return new Tile(TileKind.DOOR_BLUE,(char)8,AsciiPanel.blue);
    }

    public static Tile newDoorRed(){
        return new Tile(TileKind.DOOR_RED,(char)8,AsciiPanel.red);
    }
}
