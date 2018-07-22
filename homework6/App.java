package ru.geekbrains.antonelenberger.javacore1.homework6;

/**
 * @author Anton Elenberger
 */

public final class App {
    public static void main(String[] args) {
        new Thread(Server :: new).start();
        new Thread(ClientWindow :: new).start();
    }
}
