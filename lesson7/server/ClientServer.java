package ru.geekbrains.antonelenberger.lesson7.server;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientServer {

    private ServerSocket server;
    private List<ClientHandler> clients;
    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    private final int PORT = 8186;

    public static void main(String[] args) {
        new ClientServer();
    }

    public ClientServer() {
        try {
            server = new ServerSocket(PORT);
            Socket socket = null;
            authService = new BaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            while (true) {
                System.out.println("Сервер в режиме ожидания");
                socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            System.out.println("Ошибка Сервера");
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            authService.stop();
        }
    }

    public synchronized boolean isNickBusy(String nick) {
        for(ClientHandler o : clients) {
            if(o.getName().equals(nick)) return true;
        } return false;
    }

    public synchronized void sendPrivateMessage(ClientHandler name, String nickTo, String message) {
        for(ClientHandler o : clients) {
            if(o.getName().equals(nickTo)) {
                o.sendMessage("от " + nickTo + ": " + message);
                name.sendMessage("клиенту " + nickTo + ": " + message);
                return;
            }
        }
    }

    public synchronized void broadcastMessage(String message) {
        for(ClientHandler o : clients) {
            o.sendMessage(message);
        }
    }

    public synchronized void unsubscribe(ClientHandler o) {
        clients.remove(o);
    }

    public synchronized void subscribe(ClientHandler o) {
        clients.add(o);
    }

}
