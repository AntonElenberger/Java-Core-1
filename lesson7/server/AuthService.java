package ru.geekbrains.antonelenberger.lesson7.server;

public interface AuthService {
    void start();

    String getNickByLoginPass(String login, String pass);

    void stop();
}
