package com.net;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.world.SnapShot;
import com.world.World;
import com.world.WorldBuilder;
import com.world.creature.CreatureFactory;

public class MyServer {
    final int PORT = 4444;

    private World world;

    private int clientNum = 0;

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    private ScheduledExecutorService scheduledExecutorService;
    private ExecutorService executorService;

    private List<SnapShot> readyToSend;
    // private List<String> messages;

    public MyServer(World world) {
        this.world = world;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            System.out.println("server initial failed...");
        }
        System.out.println("server initial success");
        scheduledExecutorService = Executors.newScheduledThreadPool(2);
        executorService = Executors.newFixedThreadPool(1);
        executorService.execute(serverThread());
        // executorService.execute(acceptThread());
        // scheduledExecutorService.scheduleAtFixedRate(acceptThread(), 0, 2,
        // TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(writeThread(), 0, 50, TimeUnit.MILLISECONDS);
        // scheduledExecutorService.scheduleAtFixedRate(writeThread(), 0, 50,
        // TimeUnit.MILLISECONDS);
        readyToSend = new ArrayList<>();
        // messages = new ArrayList<>();
    }

    public Thread serverThread() {
        return new Thread(() -> {
            try {
                while (true) {
                    selector.select();
                    selector.selectedKeys().forEach(key -> {
                        if (key.isAcceptable()) {
                            try {
                                SocketChannel socketChannel = serverSocketChannel.accept();
                                if (socketChannel != null) {
                                    socketChannel.configureBlocking(false);
                                    // socketChannel.register(selector, SelectionKey.OP_WRITE);
                                    socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                                    sendId(socketChannel, ++clientNum);

                                    System.out.println("connect to client!");
                                    CreatureFactory.newPlayer(null, world, clientNum);
                                }
                            } catch (Exception e) {
                                System.out.println("connect to client failed...");
                            }
                        } else if (key.isReadable()) {
                            read(key);
                        }
                    });
                }
            } catch (Exception e) {
                System.out.println("serverThread failed...");
            }
        });
    }

    private void sendId(SocketChannel socketChannel, int id) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(12);
            buffer.clear();
            buffer.put(ByteUtil.getByteBuffer(id + ""));
            buffer.flip();
            socketChannel.write(buffer);
        } catch (Exception e) {
            System.out.println("send ID failed...");
        }
    }

    private void read(SelectionKey key) {
        try {
            ByteBuffer readBuffer = ByteBuffer.allocate(48);
            SocketChannel socketChannel = (SocketChannel) key.channel();
            if (socketChannel.read(readBuffer) != 0 && socketChannel.read(readBuffer) != -1) {
                readBuffer.flip();
                Arrays.asList(ByteUtil.getString(readBuffer).split("#")).forEach(action -> {
                    handleAction(action);
                });
            }
        } catch (Exception e) {
            // System.out.println("server read failed...");
        }
    }

    private Thread writeThread() {
        return new Thread(() -> {
            try {
                if (!readyToSend.isEmpty()) {
                    SnapShot snapShot = readyToSend.get(0);
                    readyToSend.remove(0);
                    selector.select();
                    selector.selectedKeys().forEach(key -> {
                        try {
                            if (key.isWritable()) {
                                ByteBuffer buffer = ByteBuffer.allocate(4096);
                                buffer.clear();
                                buffer.put(ByteUtil.getByteBuffer(snapShot));
                                buffer.flip();
                                ((SocketChannel) key.channel()).write(buffer);
                            }
                        } catch (Exception e) {
                            System.out.println("server write to channel failed...");
                        }
                    });

                }
            } catch (Exception e) {
                System.out.println("server select write failed...");
            }
        });
    }

    public void sendSnapShot(SnapShot snapShot) {
        readyToSend.add(snapShot);
    }

    private void handleAction(String action) {
        System.out.println("handle action: " + action);
        int id = action.charAt(0) - 48;
        char move = action.charAt(1);
        // 1 up 2 down 3 left 4 right
        switch (move) {
            case '1':
                world.creatures().get(id).moveBy(0, -1);
                break;
            case '2':
                world.creatures().get(id).moveBy(0, 1);
                break;
            case '3':
                world.creatures().get(id).moveBy(-1, 0);
                break;
            case '4':
                world.creatures().get(id).moveBy(1, 0);
                break;
        }
    }

    public static void main(String[] args) {
        World world = null;
        MyServer server = new MyServer(world);
        MyClient client = new MyClient();
        MyClient client2 = new MyClient();
        client.send("1");
        client.send("2");
        client2.send("3");
    }
}
