package Server;

import Client.Controller;
import Client.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {

    private ArrayList<ClientHandler> clients; // List to store references to all connected client handlers
    private Socket socket; // Socket for communication with a client
    private BufferedReader reader; // Reader to receive messages from the client
    private PrintWriter writer; // Writer to send messages to the client

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            // Initialize the reader and writer for the socket's input/output streams
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String msg;
            // Read messages from the client as long as they are available
            while ((msg = reader.readLine()) != null) {
                if (msg.equalsIgnoreCase( "exit")) {
                    // If the client sends the "exit" message, break the loop and terminate the thread
                    break;
                }
                // Send the received message to all connected clients (including the sender)
                for (ClientHandler cl : clients) {
                    cl.writer.println(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                // Close the reader, writer, and socket when the thread finishes or encounters an exception
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
