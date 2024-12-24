import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class PublicServer {
    final static int PORT = 12345;
    private static final ArrayList<String> messageBuffer = new ArrayList<>(); // Buffer to hold messages

    public static void main(String[] args) {
        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started, waiting for client connection...");

            // Accept a client connection (single connection for this demo)
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            // Set up input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read messages from the client and handle them
            String message;
            while ((message = in.readLine()) != null) {
                // Add message to the buffer
                messageBuffer.add(message);
                System.out.println("Buffer updated: " + messageBuffer);

                // Echo the message back to the client
                out.println(message);
            }

            // Close connections
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
