package com.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;

import com.world.creature.Creature;
import com.world.creature.PlayerAI;
import com.world.tile.Tile;

import javafx.scene.effect.ColorAdjust;

public class SnapShot implements Serializable {
    private char[][] glyphs;
    private Color[][] colors;
    //private Color[] playerColors;
    private List<Map<String, String>> players;

    public SnapShot(Floor floor) {
        this.glyphs = new char[floor.height()][floor.width()];
        this.colors = new Color[floor.height()][floor.width()];
        //this.playerColors=new Color[floor.creatures().size()];
        players = new ArrayList<>();
        // tiles
        for (int i = 0; i < floor.height(); i++) {
            for (int j = 0; j < floor.width(); j++) {
                if (floor.tile(i, j).hasItem()) {
                    this.glyphs[i][j] = floor.tile(i, j).item().glyph();
                    this.colors[i][j] = floor.tile(i, j).item().color();
                } else {
                    this.glyphs[i][j] = floor.tile(i, j).glyph();
                    this.colors[i][j] = floor.tile(i, j).color();
                }
            }
        }
        // creatures
        for (Creature creature : floor.creatures()) {
            glyphs[creature.x()][creature.y()] = creature.glyph();
            colors[creature.x()][creature.y()]=creature.color();
            if (creature.ai() instanceof PlayerAI) {
                players.add(getStatus(creature));
            }
        }

    }


    private Map<String, String> getStatus(Creature creature) {
        Map<String, String> status = new LinkedHashMap<>();
        status.put("hp", creature.hp() + "");
        status.put("attack", creature.attackValue() + "");
        status.put("defence", creature.defenseValue() + "");
        return status;
    }

    public char glyph(int x,int y){
        return glyphs[x][y];
    }

    public Color color(int x,int y){
        return colors[x][y];
    }

    public Map<String, String> status(int id){
        return players.get(id);
    }

    public List<Map<String, String>> players(){
        return players;
    }
}
