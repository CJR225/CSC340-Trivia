import java.io.*;
import java.net.*;

public class TriviaClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 5000;

        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("Connected to the server at " + hostname + " on port " + port);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // Sending a message to the server upon connecting
            out.println("Hello, server! The GUI will now launch.");
            
            // Now, launch the GUI
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new ClientWindow(); // Assuming ClientWindow is updated to be runnable
                }
            });
            
        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }
}
