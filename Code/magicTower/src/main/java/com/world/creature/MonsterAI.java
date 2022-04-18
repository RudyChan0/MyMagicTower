package com.world.creature;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class MonsterAI extends CreatureAI {

    public MonsterAI(Creature creature) {
        super(creature);
    }

    // up,dnow,left,right:1,2,3,4
    public int searchPlayer() {
        int x = creature.x();
        int y = creature.y();
        int r = creature.visionRadius();
        for (int i = Math.max(x - r, 0); i <= Math.min(x + r,12); i++) {
            for (int j = Math.max(y - r, 0); j <= Math.min(y + r,12); j++) {
                if (creature.floor().creature(i, j)!=null) {
                    if (creature.floor().creature(i, j).ai() instanceof PlayerAI) {
                        if (j > y){
                            if(creature.tile(x, y+1).canGoThrough())return 1;
                            else return i<x?3:4;
                        }
                        if (j < y){
                            if(creature.tile(x, y-1).canGoThrough())return 2;
                            else return i<x?3:4;
                        }
                        if (i < x){
                            if(creature.tile(x-1, y).canGoThrough())return 3;
                            else return i<y?2:1;
                        }
                        if (i > x){
                            if(creature.tile(x+1, y).canGoThrough())return 4;
                            else return i<y?2:1;
                        }
                    }
                }
            }
        }
        return -1;
    }

    public void randomMove() {
        Random random = new Random();
        switch (random.nextInt(4)) {
            case 1:
                creature.moveBy(1, 0);
                break;
            case 2:
                creature.moveBy(-1, 0);
                break;
            case 3:
                creature.moveBy(0, 1);
                break;
            case 4:
                creature.moveBy(0, -1);
                break;
        }
    }

    @Override
    public void onUpdate() {
        int searchRes=searchPlayer();
        switch (searchRes) {
            case -1:
                randomMove();
                break;
            case 1:
                creature.moveBy(0, 1);
                break;
            case 2:
                creature.moveBy(0, -1);
                break;
            case 3:
                creature.moveBy(-1, 0);
                break;
            case 4:
                creature.moveBy(1, 0);
                break;
        }
    }

    @Override
    public Map<String,String> status(){
        Map<String,String> status=new LinkedHashMap<>();
        status.put("glyph", creature.glyph()+"");
        status.put("HP", creature.maxHP()+"");
        status.put("Attack", creature.attackValue()+"");
        status.put("Defence", creature.defenseValue()+"");
        status.put("Note", "move randomly");
        return status;
    }

}
