package ru.geekbrains.antonelenberger.lesson7.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * @author Anton Elenberger
 */
public class ClientWindow extends JFrame {
    public static void main(String[] args) {
        new ClientWindow();
    }

    private JTextField jtfLogin;
    private JTextField jtfPass;
    private JTextField jtfMessage;
    private JTextArea jtaField;
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8186;
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientWindow() {
        try {
            clientSocket = new Socket("localhost", 8186);
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            setAuthorized(false);
            Thread t = new Thread(() -> {
                try {
                    while(true) {
                        String nickok = in.readUTF();
                        if(nickok.startsWith("/authok")) {
                            setAuthorized(true);
                            break;
                        }
                        jtaField.append(nickok + "\n");
                    }
                    while(true) {
                        String ending = in.readUTF();
                        if("/end".equals(ending)) {
                            break;
                        }
                        jtaField.append(ending + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setAuthorized(false);
                }
            });
            t.setDaemon(true);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setBounds(500,202,400,600);
        setTitle("Чатик");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jtaField = new JTextArea();
        jtaField.setEditable(false);
        jtaField.setLineWrap(true);
        final JScrollPane jsp = new JScrollPane(jtaField);
        add(jsp, BorderLayout.CENTER);
        JPanel bottompanel1 = new JPanel(new BorderLayout());
        add(bottompanel1,BorderLayout.SOUTH);
        JButton buttonSendMessage = new JButton("Отправить");
        bottompanel1.add(buttonSendMessage, BorderLayout.EAST);
        jtfMessage = new JTextField();
        bottompanel1.add(jtfMessage, BorderLayout.CENTER);
        JPanel upperPanal = new JPanel(new BorderLayout());
        add(upperPanal, BorderLayout.NORTH);
        jtfLogin = new JTextField();
        upperPanal.add(jtfLogin, BorderLayout.PAGE_START);
        jtfPass = new JTextField();
        upperPanal.add(jtfPass, BorderLayout.CENTER);
        JButton jbAuth = new JButton("Auth");
        upperPanal.add(jbAuth, BorderLayout.EAST);

        buttonSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if(!jtfMessage.getText().trim().isEmpty()) {
                    sendMessage();
                    jtfMessage.grabFocus();
                }
            }
        });
        jbAuth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                onAuthClick();
            }
        });
        jtfMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try{
                    out.writeUTF("end");
                    out.flush();
                    clientSocket.close();
                    out.close();
                    in.close();
                } catch (IOException exp) {
                }
            }
        });
        setVisible(true);
    }

    public void setAuthorized(boolean v){

    }

    public void onAuthClick() {
        try {
            String login = jtfLogin.getText();
            String pass = jtfPass.getText();
            out.writeUTF("/auth " + login + " " + pass);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        try {
            String outMessage = jtfMessage.getText();
            String name = jtfLogin.getText();
            out.writeUTF("/w " + name + " " + outMessage);
            jtfMessage.setText("");
        } catch (IOException e) {
            System.out.println("Ошибка отправки");
        }
    }
}
