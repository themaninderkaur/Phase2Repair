//package src.main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client() {
        try {
            socket = new Socket("localhost", 4343);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            showLandingPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLandingPage() {
        LandingPage lp = new LandingPage(in, out);
        lp.setVisible(true);
    }

    public static void main(String[] args) {
        new Client();
    }
}
