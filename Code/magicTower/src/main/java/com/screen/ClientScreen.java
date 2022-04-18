package com.screen;

import com.asciiPanel.AsciiPanel;
import com.net.MyClient;
import com.world.SnapShot;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.awt.Color;

public class ClientScreen extends PlayScreen{
    private MyClient client;
    private int id;
    //private List<SnapShot> snapShots;

    public ClientScreen(){
        this.client=new MyClient();
        this.id=client.id();
        this.scheduledExecutorService = Executors.newScheduledThreadPool(10);
        
    }

    private void displaySnapShot(AsciiPanel terminal){
        SnapShot snapShot=client.snapShot();
        final int X_OFFSET = 2;
        if(snapShot!=null){
            for (int x = 0; x < FLOOR_WIDTH; x++) {
                for (int y = 0; y < FLOOR_HEIGHT; y++) {
                    terminal.write(snapShot.glyph(x, y),x+ X_OFFSET,y,snapShot.color(x, y));
                }
            } 
            final int offsetX = FLOOR_WIDTH + 4;
            int x=0,y=0;
            Color[] colors={AsciiPanel.brightWhite,AsciiPanel.brightBlue,AsciiPanel.brightYellow,AsciiPanel.brickRed};
            if(snapShot.players().size()>id){
                terminal.write("Player: ", x + offsetX, y);
                terminal.write((char)2, x + offsetX+8, y,colors[id]);
                y+=2;
                Map<String, String> status=snapShot.status(id);
                for (String key : status.keySet()) {
                    terminal.write(key + ": " + status.get(key), x + offsetX, y);
                    y+=2;
                }
            }
        }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        try{
        displaySnapShot(terminal);
        //System.out.println("displayed...");
        }
        catch (Exception e){
            System.out.println(e);
            System.exit(1);
        }
    }

    
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                client.send(id+"3");
                break;
            case KeyEvent.VK_RIGHT:
                client.send(id+"4");
                break;
            case KeyEvent.VK_UP:
                client.send(id+"1");
                break;
            case KeyEvent.VK_DOWN:
                client.send(id+"2");
                break;
            case KeyEvent.VK_ESCAPE:
                return new OptionScreen(this.shutdwonThreads());
        }
        return this;
    }

}
