package com.screen;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Principal;

import com.asciiPanel.AsciiPanel;
import com.world.Archive;
import com.world.FloorBuilder;
import com.world.World;

import javafx.concurrent.Worker;

public class LoadScreen extends AbstractScreen {

    private Screen preScreen;
    private int index;
    private final int SaveNum = 4;
    private String[] status;

    public LoadScreen(Screen preScreen) {
        this.preScreen = preScreen;
        index = 0;
        status = saveStatus();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        int yOffSet = 3;
        final int TitleOffSet = 25;
        final int SaveOffSet = 25;
        final int StatusOffSet = SaveOffSet + 1;
        terminal.write("Loads", 26, yOffSet);
        terminal.write("_______________", 21, yOffSet + 1);
        char glyph = (char) 27;
        String[] options = {
                "SAVE-1",
                "SAVE-2",
                "SAVE-3",
                "SAVE-4", };
        for (int i = 0; i < SaveNum; i++) {
            if (i == index) {
                terminal.write(options[i] + "   " + glyph, SaveOffSet, 2 * i + 3 + yOffSet++, AsciiPanel.brightYellow);
                if (status[i] == "null")
                    terminal.write(status[i], StatusOffSet, 2 * i + 3 + yOffSet++, AsciiPanel.brightYellow);
                else
                    terminal.write(status[i], StatusOffSet - 7, 2 * i + 3 + yOffSet++, AsciiPanel.brightYellow);
            } else {
                terminal.write(options[i], SaveOffSet, 2 * i + 3 + yOffSet++);
                if (status[i] == "null")
                    terminal.write(status[i], StatusOffSet, 2 * i + 3 + yOffSet++);
                else
                    terminal.write(status[i], StatusOffSet - 7, 2 * i + 3 + yOffSet++);
            }
        }
        terminal.write("_______________", 21, yOffSet + 10);
    }

    private String[] saveStatus() {
        String[] status = { "null", "null", "null", "null" };
        for (int i = 0; i < SaveNum; i++) {
            try {
                InputStream inStream = Archive.class.getClassLoader().getResourceAsStream("saves/save" + i + ".dat");
                ObjectInputStream in = new ObjectInputStream(inStream);
                // ObjectInputStream in = new ObjectInputStream(new
                // FileInputStream("src/main/resources/saves/save"+i+".dat"));
                Archive archive = (Archive) in.readObject();
                status[i] = archive.time();
                in.close();
            } catch (Exception e) {
                if (e instanceof FileNotFoundException) {
                } else
                    System.out.println(e);
            }
        }
        return status;
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                try {
                    InputStream inStream = Archive.class.getClassLoader()
                            .getResourceAsStream("saves/save" + index + ".dat");
                    if (inStream != null) {
                        ObjectInputStream in = new ObjectInputStream(inStream);
                        Archive archive = (Archive) in.readObject();
                        in.close();
                        return new PlayScreen(archive.toWorld());
                    }
                } catch (Exception e) {
                    System.out.println("load failed");
                    System.out.println(e);
                }

            case KeyEvent.VK_ESCAPE:
                return preScreen;
            case KeyEvent.VK_UP:
                index = index > 0 ? --index : 0;
                break;
            case KeyEvent.VK_DOWN:
                index = index < SaveNum - 1 ? ++index : SaveNum - 1;
                break;
        }
        return this;
    }
}
