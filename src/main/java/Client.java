import java.io.*;
import java.net.*;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4343);  // Connect to the server
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Connected to Echo Server! Type your messages below:");

        String userInput;
        while ((userInput = consoleInput.readLine()) != null) {
            out.println(userInput);  // Send message to the server
            String serverResponse = in.readLine();  // Read the server's response
            System.out.println(serverResponse);  // Print the server's response
        }
        socket.close();
    }
}
