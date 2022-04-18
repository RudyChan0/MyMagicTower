package com.screen;

import java.awt.event.KeyEvent;

import com.ApplicationMain;
import com.asciiPanel.AsciiPanel;

public class StartScreen extends AbstractScreen {

    private int index;

    public StartScreen() {
        index = 0;
    }

    private void displayTitle(AsciiPanel terminal) {
        terminal.write("|  \\/  |           (_)       |__   __|", 0, 1);
        terminal.write("| \\  / | __ _  __ _ _  ___      | |", 0, 2);
        terminal.write("| |\\/| |/ _` |/ _` | |/ __|     | |/ _ \\ \\ /\\ / / _ \\ '__|", 0, 3);
        terminal.write("| |  | | (_| | (_| | | (__      | | (_) \\ V  V /  __/ |", 0, 4);
        terminal.write("|_|  |_|\\__,_|\\__, |_|\\___|     |_|\\___/ \\_/\\_/ \\___|_|", 0, 5);
        terminal.write("               __/ |", 0, 6);
        terminal.write("              |___/ ", 0, 7);
    }

    private void displayOption(AsciiPanel terminal) {
        final int XOOFSET = 23;
        int YOOFSET = 11;
        char glyph = (char) 27;
        String[] options = {
                "New Game",
                "Load Game",
                "Host Game",
                "Join Game",
                "Exit", };
        for (int i = 0; i < 5; i++) {
            if (i == index)
                terminal.write(options[i] + " " + glyph, XOOFSET, i + YOOFSET++, AsciiPanel.brightYellow);
            else
                terminal.write(options[i], XOOFSET, i + YOOFSET++);
        }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        displayTitle(terminal);
        displayOption(terminal);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                switch (index) {
                    case 0:
                        return new PlayScreen();
                    case 1:
                        return new LoadScreen(this);
                    case 2:
                        return new HostScreen();
                    case 3:
                        return new ClientScreen();
                    case 4:
                        System.exit(0);
                }

            case KeyEvent.VK_UP:
                index = index > 0 ? --index : 0;
                break;
            case KeyEvent.VK_DOWN:
                index = index < 4 ? ++index : 4;
                break;

        }
        return this;
    }
}
