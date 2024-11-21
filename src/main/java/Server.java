import java.util.ArrayList;
import java.io.*;
import java.net.*;
 
public class Server implements Runnable {
    private Socket socket;
    private ArrayList<User> userList = new ArrayList<User>();
    
    public Server (Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Client says: " + clientMessage);
                out.println("Echo: " + clientMessage);  // Echo the message back
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4343);  // Listen on port 4343
        System.out.println("Echo server is running...");

        

        while (true) {
            Socket clientSocket = serverSocket.accept();  // Wait for a client
            new Thread(new EchoServer(clientSocket)).start();  // Create a new thread for each client
        }
    }
}
