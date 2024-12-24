import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class User {
    final private EncryptDecryptBigInt encryptDecrypt;
    private final String userName;
    private final ArrayList<BigInteger> messageBuffer = new ArrayList<>(); // Buffer for incoming messages

    // Constructor to initialize the User with a name and prime numbers for RSA
    public User(String userName, BigInteger p, BigInteger q) {
        this.userName = userName;
        this.encryptDecrypt = new EncryptDecryptBigInt(p, q);
    }

    // Encrypt message using public key
    public BigInteger encryptMessage(String message) {
        BigInteger messageBigInt = new BigInteger(message.getBytes());
        return encryptDecrypt.encrypt(messageBigInt);
    }

    // Decrypt message using private key
    public String decryptMessage(BigInteger encryptedMessage) {
        BigInteger decryptedBigInt = encryptDecrypt.decrypt(encryptedMessage);
        return new String(decryptedBigInt.toByteArray());
    }

    public String getUserName() {
        return userName;
    }

    // Getter for the public key modulus (for sharing)
    public BigInteger getPublicKeyModulus() {
        return encryptDecrypt.getPublicKeyModulus();
    }

    public BigInteger getPublicKeyExponent() {
        return encryptDecrypt.getPublicKeyExponent();
    }

    // Main method for User to connect to the server and communicate
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask for prime numbers from the user
        System.out.println("Please enter a prime number (p): ");
        BigInteger p = new BigInteger(scanner.nextLine());

        System.out.println("Please enter another prime number (q): ");
        BigInteger q = new BigInteger(scanner.nextLine());

        // Create a user with the provided primes
        System.out.println("Enter your username: ");
        String userName = scanner.nextLine();
        User user = new User(userName, p, q);

        // Establish connection to the server
        System.out.println("Connecting to the server...");
        try {
            Socket socket = new Socket("localhost", PublicServer.PORT);
            System.out.println("Successfully connected to the server!");

            // Set up input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Send username and public key to the server
            out.println(user.getUserName());
            out.println(user.getPublicKeyModulus());
            out.println(user.getPublicKeyExponent());

            // Receive handshake completion message
            String serverResponse = in.readLine();
            System.out.println("Handshake completed with server: " + serverResponse);

            // Communication loop
            while (true) {
                // Check buffer for messages
                if (!user.messageBuffer.isEmpty()) {
                    System.out.println("You have new messages:");
                    ArrayList<BigInteger> messagesToProcess = new ArrayList<>(user.messageBuffer);
                    user.messageBuffer.clear(); // Clear buffer before processing

                    for (BigInteger encryptedMessage : messagesToProcess) {
                        String decryptedMessage = user.decryptMessage(encryptedMessage);
                        System.out.println("Decrypted message: " + decryptedMessage);
                    }
                }

                // Read user input and send encrypted message to the server
                System.out.print("Enter a message to send (or 'exit' to quit): ");
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                BigInteger encryptedMessage = user.encryptMessage(message);
                out.println(encryptedMessage);

                // Read server response and add to buffer
                String serverResponseEncrypted = in.readLine();
                if (serverResponseEncrypted != null) {
                    user.messageBuffer.add(new BigInteger(serverResponseEncrypted));
                }
            }

            // Close streams and socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
