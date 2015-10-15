package nu.hci.codemenao;

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;


public class ConnectingNaoActivity extends Activity {

    TextView serverIp;

    protected int my_backlog = 5;
    protected ServerSocket my_serverSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_nao);

        serverIp = (TextView)findViewById(R.id.serverIp);
        getDeviceIpAddress();
        // TCPSocketServer server = new TCPSocketServer(7200);
        // Listen on the server socket. This will run until the program is
        try {
            my_serverSocket = new ServerSocket(7200, my_backlog);
            System.out.println("TCP socket listening on port " + 7200);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SecurityException se) {
            se.printStackTrace();
        }

        // killed.
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // Listens for a connection to be made to this socket.
                        Socket socket = my_serverSocket.accept();
                        //send message to client
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println("1");

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
        }).start();
    }



    /**
     * Get ip address of the device
     */
    public void getDeviceIpAddress() {
        try {

            for (Enumeration<NetworkInterface> enumeration = NetworkInterface
                    .getNetworkInterfaces(); enumeration.hasMoreElements();) {
                NetworkInterface networkInterface = enumeration.nextElement();
                for (Enumeration<InetAddress> enumerationIpAddr = networkInterface
                        .getInetAddresses(); enumerationIpAddr
                             .hasMoreElements();) {
                    InetAddress inetAddress = enumerationIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress.getAddress().length == 4) {
                        serverIp.setText(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("ERROR:", e.toString());
        }
    }

}










