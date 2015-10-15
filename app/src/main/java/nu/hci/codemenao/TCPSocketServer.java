package nu.hci.codemenao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;

/**
 * A simple class that opens a server socket, and prints each message received
 * to the console.
 * @author Graeme Stevenson (graeme.stevenson@ucd.ie)
 */
public class TCPSocketServer {

    /**
     * Accept this many connections.
     */
    private int my_backlog = 5;

    /**
     * The server socket.
     */
    private ServerSocket my_serverSocket;

    /**
     * Create the server socket.
     * @param a_port the port that the server socket should listen on.
     */
    public TCPSocketServer(int a_port) {
        try {
            my_serverSocket = new ServerSocket(a_port, my_backlog);
            System.out.println("TCP socket listening on port " + a_port);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }

    /**
     * Method that listens on the server socket forever and prints each incoming
     * message to the console.
     */
    public void listen() {
        while (true) {
            try {
                // Listens for a connection to be made to this socket.
                Socket socket = my_serverSocket.accept();

                //send message to client
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("yerassyl");

                // Wrap a buffered reader round the socket input stream.
                // Read the javadoc to understand why we do this rather than dealing
                // with reading from raw sockets.
                BufferedReader in = new BufferedReader(new InputStreamReader(socket
                        .getInputStream()));

                String msg = in.readLine();

                System.out.println("message is: " + msg);


                // tidy up
                in.close();
                socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * Parse the arguments to the program and create the server socket.
     * @param args the program arguments. Should take the form &lt;port&gt;
     */
    public static void main(String[] args) {
        int port = 7200;

        TCPSocketServer server = new TCPSocketServer(port);

        // Listen on the server socket. This will run until the program is
        // killed.
        server.listen();

    }
} // end class

