package com.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.world.SnapShot;
import com.world.World;

public class MyClient {
    final String ADDRESS = "127.0.0.1";
    final int PORT = 4444;
    private int id;

    // private World world;

    private SocketChannel socketChannel;

    private List<String> toSend;

    // private List<SnapShot> snapShots;
    private SnapShot snapShot;

    public MyClient() {
        this.id = -1;
        // this.world = world;
        toSend = new ArrayList<>();
        snapShot = null;
        // snapShots = new ArrayList<>();
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            if (!socketChannel.connect(new InetSocketAddress(ADDRESS, PORT))) {
                while (!socketChannel.finishConnect()) {
                    System.out.println("client" + id + " connect failed...");
                }
            }
            readId(socketChannel);
            System.out.println("client" + id + " connect success");
            initialThread();
            // do something
        } catch (Exception e) {
            System.out.println("client" + id + " connect failed with exception...");
            System.out.println(e);
        }
    }

    private void readId(SocketChannel socketChannel) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(12);
            if (socketChannel.read(buffer) != 0 && socketChannel.read(buffer) != -1) {
                buffer.flip();
                this.id = Integer.parseInt(ByteUtil.getString(buffer));
            }
            else{
                readId(socketChannel);
            }
        } catch (Exception e) {
            System.out.println("read ID failed...");
            readId(socketChannel);
        }
    }

    // keeps reading snapShot and put in list
    private void initialThread() {
        Thread readThread = new Thread(() -> {
            try {
                ByteBuffer readBuffer = ByteBuffer.allocate(4096);
                if (socketChannel.read(readBuffer) != 0 && socketChannel.read(readBuffer) != -1) {
                    snapShot = (SnapShot) ByteUtil.getObject(readBuffer);
                    System.out.println("client receive snapShot!");
                    // snapShots.add((SnapShot) ByteUtil.getObject(readBuffer));
                }

            } catch (Exception e) {
                System.out.println("read from server failed...");
                System.out.print(e);
                // System.exit(1);
            }
        });
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(readThread, 0, 10, TimeUnit.MILLISECONDS);
    }

    public boolean send(String string) {
        // socketChannel.
        try {
            ByteBuffer buffer = ByteBuffer.allocate(48);
            buffer.clear();
            buffer.put(ByteUtil.getByteBuffer(string + "#"));
            buffer.flip();
            socketChannel.write(buffer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int id() {
        return id;
    }

    // public List<SnapShot> snapShots(){
    // return snapShots;
    // }

    public SnapShot snapShot() {
        return snapShot;
    }

    public static void main(String[] args) {
        String s = "1#2#";
        System.out.println(s.split("#"));
    }
}
