package com.world.creature;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.world.Floor;
import com.world.World;
import com.world.item.Item;
import com.world.tile.Tile;

public class Creature implements Serializable {
    // Location
    private transient World world;
    private transient Floor floor;
    private int floorNum;
    private int x;
    private int y;
    // Appearance
    private char glyph;
    private Color color;
    private CreatureAI ai;
    private CreatureThread cThread;
    // Nums
    private int maxHP;
    private int hp;
    private int attackValue;
    private int defenseValue;
    private int visionRadius;
    //Item
    private List<Item>items;



    public Creature(World world, char glyph, Color color, int maxHP, int attack, int defense, int visionRadius) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.attackValue = attack;
        this.defenseValue = defense;
        this.visionRadius = visionRadius;
        this.items=new ArrayList<>();
    }

    public Creature(World world, Floor floor, char glyph, Color color, int maxHP, int attack, int defense,
            int visionRadius) {
        this.world = world;
        this.floor = floor;
        this.glyph = glyph;
        this.color = color;
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.attackValue = attack;
        this.defenseValue = defense;
        this.visionRadius = visionRadius;
        this.items=new ArrayList<>();
    }

    public void update(){
        ai.onUpdate();
    }

    public void moveBy(int mx, int my) {
        // TODO: meet others
        Creature creature =floor.creature(x + mx, y + my);
        if(creature==null){
        ai.onEnter(x + mx, y + my, floor.tile(x + mx, y + my));
        }
        else{
            attack(creature);
        }
    }

    public void attack(Creature other){
        boolean isAlive=other.modifyHP(-(getDamage(other)));
        this.notify("You attack the '%s' for %d damage.", other.glyph, getDamage(other));
        other.notify("The '%s' attacks you for %d damage.", glyph, getDamage(other));
        
        //if(isAlive)other.attack(this);
    }

    public Map<String,String> status(){
        return ai.status();
    }

    public List<Item> items(){
        return items;
    }

    public void visitItem(Item item){
        item.accept(this);
    }

    public void getItem(Item item){
        items.add(item);
    }

    public void goUpstair(){
        world.goUpstair();
    }

    public void goDownstair(){
        world.goDownstair();
    }

    public int getDamage(Creature other){
        return Math.max(0, this.attackValue-other.defenseValue);
    }

    public Thread thread(){
        return this.cThread;
    }

    public void setThread(CreatureThread cThread){
        this.cThread=cThread;
    }

    public CreatureAI ai(){
        return this.ai;
    }

    public void setAI(CreatureAI ai) {
        this.ai = ai;
    }

    public boolean modifyHP(int amount) {
        this.hp += amount;
        // TODO: hp influence
        if (this.hp < 1) {
        floor.remove(this);
        return false;
        }
        return true;
    }

    public boolean isAlive(){
        return hp>0;
    }

    public void modifyAttack(int amount) {
        this.attackValue+=amount;
    }

    public void modifyDefence(int amount) {
        this.defenseValue+=amount;
    }

    public Floor floor(){
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public void setWorld(World world){
        this.world=world;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public void setMessages(List<String> messages){
        if(ai instanceof PlayerAI){
            ((PlayerAI)ai).setMessages(messages);
        }
    }

    public int floorNum() {
        return floorNum;
    }

    public Tile tile(int x,int y){
        return floor.tile(x, y);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int x() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int y() {
        return y;
    }

    public char glyph() {
        return this.glyph;
    }

    public Color color() {
        return this.color;
    }

    public int maxHP() {
        return this.maxHP;
    }

    public int hp() {
        return this.hp;
    }

    public int attackValue() {
        return this.attackValue;
    }

    public int defenseValue() {
        return this.defenseValue;
    }

    public int visionRadius() {
        return this.visionRadius;
    }

    public void notify(String message, Object... params) {
        ai.onNotify(String.format(message, params));
    }
}
