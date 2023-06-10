package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    // ArrayList to store client handlers for all connected clients
    private static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket socket;
        try {
            // Create a server socket on port 8889
            serverSocket = new ServerSocket(8889);

            while(true) {
                System.out.println("Waiting for clients...");

                // Wait for a client to connect and accept the socket connection
                socket = serverSocket.accept();
                System.out.println("Connected");

                // Create a new client handler for the connected client
                ClientHandler clientThread = new ClientHandler(socket, clients);

                // Add the client handler to the list of clients
                clients.add(clientThread);

                // Start the client handler thread
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
