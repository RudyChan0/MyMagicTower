package com.world.creature;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class DetectorAI extends MonsterAI {

    public DetectorAI(Creature creature) {
        super(creature);
    }

    @Override
    public void onUpdate(){
        int searchRes=searchPlayer();
        switch (searchRes) {
            case -1:
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
        status.put("Note", "detect player");
        return status;
    }

}
