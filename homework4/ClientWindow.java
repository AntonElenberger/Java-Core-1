package ru.geekbrains.antonelenberger.javacore1.homework4;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


public class ClientWindow extends JFrame {
    private JTextField jtfName;
    private JTextField jtfMessege;
    private JTextArea jtaField;

    public ClientWindow() {
        setBounds(500,200,400,600);
        setTitle("Чатик");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jtaField = new JTextArea();
        jtaField.setEditable(false);
        jtaField.setLineWrap(true);

        JScrollPane jsp = new JScrollPane(jtaField);
        add(jsp, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);

        JButton buttonSendMessege = new JButton("Отправить");
        bottomPanel.add(buttonSendMessege, BorderLayout.EAST);

        jtfMessege = new JTextField("Ваше сообщение: ");
        bottomPanel.add(jtfMessege, BorderLayout.CENTER);

        jtfName = new JTextField("Ваше имя: ");
        bottomPanel.add(jtfName, BorderLayout.WEST);

        buttonSendMessege.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!jtfMessege.getText().trim().isEmpty() && !jtfName.getText().trim().isEmpty()) {
                    sendMessege();
                    jtfMessege.grabFocus();
                }
            }
        });

         jtfMessege.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent actionEvent) {
                     if (!jtfMessege.getText().trim().isEmpty() && !jtfName.getText().trim().isEmpty()) {
                         sendMessege();
                         jtfMessege.grabFocus();
                     }
             }
         });

        jtfMessege.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfMessege.setText("");
            }
        });

        jtfName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfName.setText("");
            }
        });

    }

    public void sendMessege() {
        String messege = jtfName.getText() + ":" + jtfMessege.getText();
        jtaField.append(messege);
        jtaField.append("\n");
        jtfMessege.setText("");
    }
}
