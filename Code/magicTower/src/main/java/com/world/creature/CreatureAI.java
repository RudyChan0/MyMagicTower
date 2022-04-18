package com.world.creature;

import java.io.Serializable;
import java.util.Map;

import com.world.tile.Tile;

public class CreatureAI implements Serializable{
    protected Creature creature;

    public CreatureAI(Creature creature){
        this.creature=creature;
        creature.setAI(this);
    }

    public void onEnter(int x,int y,Tile tile){
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
        }
    }

    public void meetCreature(Creature creature){
        
    }

    public void onUpdate(){

    }

    public void onNotify(String message){

    }

    public boolean canSee(Creature other){
        int x1=creature.x();
        int y1=creature.y();
        int x2=other.x();
        int y2=other.y();
        int a=y2-y1;
        int b=x1-x2;
        int c=x2*y1-x1*y2;
        for(int i=Math.min(x1, x2);i<=Math.max(x1, x2);i++){
            for(int j=Math.min(y1, y2);j<=Math.max(y1, y2);j++){
                double distence=Math.pow((double)(a*i+b*j+c), 2)/(double)(a*a+b*b);
                if(distence<0.5&&creature.tile(i,j).canBlockSight()){
                    return false;
                }
            }
        }
        return true;
    }

    public Map<String,String> status(){
        return null;
    }
}
