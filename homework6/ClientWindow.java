package ru.geekbrains.antonelenberger.javacore1.homework6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ClientWindow extends JFrame {
    private JTextField jtfName;
    private JTextField jtfMessage;
    private JTextArea jtaField;
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8186;
    private Socket clientSocket;
    private Scanner in;
    private PrintWriter out;

    public ClientWindow() {
        try {
            clientSocket = new Socket(SERVER_ADDR, SERVER_PORT);
            in = new Scanner(clientSocket.getInputStream());
            out = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        setBounds(500, 200, 400, 600);
        setTitle("Чатик");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jtaField = new JTextArea();
        jtaField.setEditable(false);
        jtaField.setLineWrap(true);
        final JScrollPane jsp = new JScrollPane(jtaField);
        add(jsp, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);
        JButton buttonSendMessege = new JButton("Отправить");
        bottomPanel.add(buttonSendMessege, BorderLayout.EAST);
        jtfMessage = new JTextField("Ваше сообщение: ");
        bottomPanel.add(jtfMessage, BorderLayout.CENTER);
        jtfName = new JTextField("Ваше имя: ");
        bottomPanel.add(jtfName, BorderLayout.WEST);
        buttonSendMessege.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!jtfMessage.getText().trim().isEmpty() && !jtfName.getText().trim().isEmpty()) {
                    jtaField.append(jtfName.getText() + ":" + jtfMessage.getText());
                    sendMessage(out);
                    jtfMessage.grabFocus();
                }
            }
        });
        jtfMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!jtfMessage.getText().trim().isEmpty() && !jtfName.getText().trim().isEmpty()) {
                    jtaField.append(jtfName.getText() + ":" + jtfMessage.getText());
                    sendMessage(out);
                    jtfMessage.grabFocus();
                }
            }
        });
        jtfMessage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(final FocusEvent e) {
                jtfMessage.setText("");
            }
        });
        jtfName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(final FocusEvent e) {
                jtfName.setText("");
            }
        });

        new Thread(() -> inputChat(in)).start();
        new Thread(() -> sendMessage(out)).start();

        addWindowListener(new WindowAdapter() {
            @Override
            public final void windowClosing(final WindowEvent e) {
                super.windowClosing(e);
                try {
                    out.println("end");
                    out.flush();
                    clientSocket.close();
                    out.close();
                    in.close();
                } catch(IOException exs){
                }
            }
        });
        setVisible(true);
    }

    public void inputChat(Scanner in) {
        try {
            while (true) {
                if (in.hasNext()) {
                    String message = in.nextLine();
                    if (message.equals("end")) break;
                    jtaField.append(message + "\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(PrintWriter out) {
        String message = jtfName.getText() + ":" + jtfMessage.getText();
        out.println(message);
        out.flush();
        jtfMessage.setText("");
    }
}


