import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TriviaServer {
    private static final int port = 5000;
    private static AtomicInteger clientIDCounter = new AtomicInteger(0);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int clientID = clientIDCounter.incrementAndGet();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress() + " with ID: " + clientID);

                new ClientHandler(clientSocket, clientID).start();
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private int clientID;

    public ClientHandler(Socket socket, int clientID) {
        this.clientSocket = socket;
        this.clientID = clientID;
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            // Send the client ID to the client
            out.println("Your Client ID: " + clientID);
            // You can add more communication logic here
            // For example, sending questions, handling answers, etc.
        } catch (IOException e) {
            System.out.println("Exception in ClientHandler: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing the client socket: " + e.getMessage());
            }
        }
    }
}
