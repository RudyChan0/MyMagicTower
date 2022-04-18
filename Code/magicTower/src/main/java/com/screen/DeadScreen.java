package com.screen;

import java.awt.event.KeyEvent;

import com.ApplicationMain;
import com.asciiPanel.AsciiPanel;

public class DeadScreen extends AbstractScreen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("You are Dead...", 0, 0);
        terminal.write("Press ENTER to restart...", 0, 1);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                return new PlayScreen();
            default:
                return this;
        }
    }
}
