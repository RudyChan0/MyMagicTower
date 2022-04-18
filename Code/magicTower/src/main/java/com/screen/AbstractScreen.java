package com.screen;

import java.awt.event.KeyEvent;

import com.ApplicationMain;
import com.asciiPanel.AsciiPanel;
import com.world.World;


public class AbstractScreen implements Screen {

    public void displayOutput(AsciiPanel terminal){

    }

    public Screen respondToUserInput(KeyEvent key){
        return this;
    }

    public void update(){
        
    }

    public Screen checkPlayer(){
        return this;
    }

}
