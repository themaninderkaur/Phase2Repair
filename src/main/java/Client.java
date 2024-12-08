package src.main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static JTextArea textArea;
    private static JTextField inputField;
    private static JButton sendButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Social Media Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        frame.add(inputField, BorderLayout.SOUTH);

        sendButton = new JButton("Send");
        frame.add(sendButton, BorderLayout.EAST);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        frame.setVisible(true);

        try {
            socket = new Socket("localhost", 4343);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            receiveMessages();
        } catch (IOException e) {
            e.printStackTrace();
            textArea.append("Error connecting to server: " + e.getMessage() + "\n");
        }
    }

    private static void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            inputField.setText("");
        }
    }

    private static void receiveMessages() {
        new Thread(() -> {
            String serverMessage;
            try {
                while ((serverMessage = in.readLine()) != null) {
                    textArea.append("Server: " + serverMessage + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                textArea.append("Error receiving message: " + e.getMessage() + "\n");
            }
        }).start();
    }
}