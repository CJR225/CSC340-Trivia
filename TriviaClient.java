import java.io.*;
import java.net.*;

public class TriviaClient {
    private String hostname;
    private int port;
    private String clientID;

    public TriviaClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void connectToServer() {
        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("Connected to the server at " + hostname + " on port " + port);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Read the client ID assigned by the server
            clientID = in.readLine().replace("Your Client ID: ", "").trim();
            System.out.println("Assigned Client ID: " + clientID);
            
            // Now, launch the GUI with the ClientID
            javax.swing.SwingUtilities.invokeLater(() -> {
                new ClientWindow(clientID,"./questions.json"); // Assuming ClientWindow is updated to accept clientID as a parameter
            });
            
        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        TriviaClient client = new TriviaClient("localhost", 5000);
        client.connectToServer();
    }
}
