package ru.geekbrains.antonelenberger.javacore1.homework6;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public Server() {
        ServerSocket server = null;
        Socket socket = null;
        try {
            server = new ServerSocket(8186);
            System.out.println("Сервер запущен");
            socket = server.accept();
            System.out.println("Клиент подключился");
            Scanner incomingStream = new Scanner(socket.getInputStream());
            PrintWriter outgoingStream = new PrintWriter(socket.getOutputStream());
            new Thread(() -> receiveInput(incomingStream)).start();
            new Thread(() -> output(outgoingStream)).start();
        } catch (IOException e) {
            System.out.println("Ошибка сервера");
        } finally {
            try {
                assert server != null;
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveInput(Scanner incomingStream) {
        while (true) {
            String text = incomingStream.nextLine();
            if(text.equals("end")) break;
            System.out.println(text);
        }
    }

    private void output(PrintWriter outgoingStream) {
        Scanner input = new Scanner(System.in);
        while (true) {
            String text = input.nextLine();
            if(text.equals("end")) break;
            outgoingStream.println(text);
            outgoingStream.flush();
        }
    }
}
