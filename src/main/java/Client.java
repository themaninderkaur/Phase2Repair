import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4343);  // Connect to the server on port 4343
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // To read data from the server
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);  // To send data to the server
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));  // To read user input

        try {
            System.out.println("Connected to the Social Media Server!");
            String serverResponse;

            // Read the initial welcome message from the server
            if ((serverResponse = in.readLine()) != null) {
                System.out.println(serverResponse);
            }

            while (true) {
                System.out.print("Enter command (signup, login, exit, etc.): ");
                String userInput = consoleInput.readLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    out.println(userInput);  // Send 'exit' command to the server
                    break;  // Exit the loop and close the client
                }

                out.println(userInput);  // Send the command to the server

                // Handle server responses until the server indicates it's waiting for the next command
                while ((serverResponse = in.readLine()) != null && !serverResponse.isEmpty()) {
                    System.out.println(serverResponse);
                    if (serverResponse.contains("Awaiting command...")) {
                        break;  // The server is ready for the next command
                    }
                }
            }

            System.out.println("Disconnecting from server...");
        } finally {
            socket.close();  // Ensure the socket is closed on exit
        }
    }
}
