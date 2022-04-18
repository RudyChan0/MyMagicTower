package com.screen;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.security.cert.PKIXRevocationChecker.Option;

import com.asciiPanel.AsciiPanel;
import com.world.Archive;
import com.world.World;

import javafx.concurrent.Worker;

public class SaveScreen extends PlayScreen {

    private OptionScreen optionScreen;
    private PlayScreen playScreen;
    private int index;
    private final int SaveNum = 4;
    private String[] status;

    public SaveScreen(OptionScreen optionScreen, PlayScreen playScreen) {
        this.optionScreen = optionScreen;
        this.playScreen = playScreen;
        index = 0;
        status = saveStatus();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        int yOffSet = 3;
        final int TitleOffSet = 25;
        final int SaveOffSet = 25;
        final int StatusOffSet = SaveOffSet + 1;
        terminal.write("Saves", 26, yOffSet);
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
                if (inStream != null) {
                    ObjectInputStream in = new ObjectInputStream(inStream);
                    Archive archive = (Archive) in.readObject();
                    status[i] = archive.time();
                    inStream.close();
                    in.close();
                }
            } catch (Exception e) {
                System.out.println("save status error");
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
                    //ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("target/classes/saves/save" + index + ".dat"));
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("src/main/resources/saves/save" + index + ".dat"));
                    out.writeObject(Archive.saveWorld(playScreen.world()));
                } catch (Exception e) {
                    System.out.println("save failed");
                    System.out.println(e);
                }
                return playScreen;
                //return new SaveScreen(optionScreen, playScreen);
            case KeyEvent.VK_ESCAPE:
                return optionScreen;
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
