package com.screen;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.asciiPanel.AsciiPanel;
import com.world.Archive;
import com.world.World;

import javafx.concurrent.Worker;


public class OptionScreen extends AbstractScreen {

    private PlayScreen playScreen;
    private int index;

    public OptionScreen(PlayScreen playScreen) {
        this.playScreen = playScreen;
        index = 0;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        final int yOffSet = 3;
        final int TitleOffSet = 25;
        final int OptionOffSet = 23;
        final int OptionNum = 4;
        terminal.write("Options", 25, yOffSet);
        terminal.write("_______________", 21, yOffSet + 1);
        char glyph = (char) 27;
        String[] options = {
                "resume game",
                "save game",
                "load game",
                "exit" };
        for (int i = 0; i < OptionNum; i++) {
            if (i == index) {
                terminal.write(options[i] + " " + glyph, OptionOffSet, 2 * i + 3 + yOffSet, AsciiPanel.brightYellow);
            } else {
                terminal.write(options[i], OptionOffSet, 2 * i + 3 + yOffSet);
            }
        }
        terminal.write("_______________", 21, yOffSet + 11);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                switch (index) {
                    case 0:
                        return playScreen.resumeThreads();
                    case 1:
                        return new SaveScreen(this, playScreen);
                    case 2:
                        return new LoadScreen(this);
                    case 3:
                        System.exit(0);
                }
            case KeyEvent.VK_ESCAPE:
                return playScreen.resumeThreads();
            case KeyEvent.VK_UP:
                index = index > 0 ? --index : 0;
                break;
            case KeyEvent.VK_DOWN:
                index = index < 3 ? ++index : 3;
                break;
        }
        return this;
    }
}
