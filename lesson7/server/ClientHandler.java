package ru.geekbrains.antonelenberger.lesson7.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    private ClientServer clientServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;

    public String getName() {
        return name;
    }

    public ClientHandler(ClientServer clientServer, Socket socket) {
        try {

            this.clientServer = clientServer;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.name = "";
            new Thread(() -> {
                try {
                    while (true) {
                        String auth = in.readUTF();
                        if (auth.startsWith("/auth")) {
                            String[] parts = auth.split("\\s");
                            String nick = clientServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                            if (nick != null) {
                                if (!clientServer.isNickBusy(nick)) {
                                    sendMessage("/authok " + nick);
                                    name = nick;
                                    clientServer.broadcastMessage(name + " заглянул в чатике");
                                    clientServer.subscribe(this);
                                    break;
                                } else sendMessage("Логин уже активен");
                            } else {
                                sendMessage("Неверный логин/пароль");
                            }
                        }
                    }
                    while (true) {
                        String message = in.readUTF();
                        if (message.startsWith("/w")) {
                            String[] partsOfMessage = message.split("\\s");
                            String addrName = partsOfMessage[1];
                            System.out.println("От" + name + " для: " + addrName + message);
                            if ("/end".equals(message)) break;
                            clientServer.broadcastMessage(name + "для " + addrName + ": " + message);
                        } else {
                            System.out.println("От " + name + ": " + message);
                            if ("/end".equals(message)) break;
                            clientServer.broadcastMessage(name + ": " + message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    clientServer.unsubscribe(this);
                    clientServer.broadcastMessage(name + " ушел из чатика");
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании обработчика");
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
