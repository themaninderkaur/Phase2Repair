package src.main.java;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4343);  // Connect to the server on port 4343
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // To read data from the server
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);  // To send data to the server
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));  // To read user input
        boolean exit = false;
        Scanner s = new Scanner(System.in);

        try {
            System.out.println("Connected to the Social Media Server!");

            while (!exit) {
                String input = in.readLine();
                if (input != null) {
                    System.out.println(input);
                    String scan = s.nextLine();

                    if (scan.equals("exit")) {
                        exit = true;
                    }
                    out.print(scan);
                    out.println();
                    out.flush();
                }
            }
            
        } finally {
            socket.close();  // Ensure the socket is closed on exit
        }
    }
}
