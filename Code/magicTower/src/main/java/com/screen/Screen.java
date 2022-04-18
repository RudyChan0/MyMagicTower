package com.screen;

import java.awt.event.KeyEvent;

import com.asciiPanel.AsciiPanel;
import com.world.World;

import javafx.application.Application;

/**
 *
 * @author Aeranythe Echosong
 */
public interface Screen {

    public void displayOutput(AsciiPanel terminal);

    public Screen respondToUserInput(KeyEvent key);

    public void update();

    public Screen checkPlayer();

}
