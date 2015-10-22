package nu.hci.codemenao;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ConnectingNaoActivity extends Activity {

    TextView serverIp;

    protected int my_backlog = 1;
    protected ServerSocket my_serverSocket;
    protected static BlockingQueue<String> q;
    BufferedReader in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_nao);
        serverIp = (TextView)findViewById(R.id.serverIp);
        getDeviceIpAddress();

        // Listen on the server socket. This will run until the program is
        try {
            my_serverSocket = new ServerSocket(7200, my_backlog);
            System.out.println("TCP socket listening on port " + 7200);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SecurityException se) {
            se.printStackTrace();
        }

        q = new LinkedBlockingQueue<String>();
        // killed.
        new Thread(new Runnable() {
            @Override
            public void run() {
               // while (true) {
                    try {
                        // Listens for a connection to be made to this socket.
                        Socket socket = my_serverSocket.accept();
                        //send message to client
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        //out.println("1");

                       in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                       while(true){
                                Log.d("yerchik/q", "queue is not empty");
                                try {
                                    String msg = in.readLine();
                                    System.out.println("message is: " + msg);
                                    String temp = q.take();
                                    out.println(temp);
                                }catch(InterruptedException e){
                                    Log.d("yerchik", "couldn't take from queue: "+ e.getMessage());
                                }

                            // Wrap a buffered reader round the socket input stream.
                            // Read the javadoc to understand why we do this rather than dealing
                            // with reading from raw sockets.
                        }

                        // tidy up
                        //in.close();
                        //socket.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    } catch (SecurityException se) {
                        se.printStackTrace();
                    }
                //}
            }
        }).start();


    }

    public void startLevel1(View view) {
        Intent intent = new Intent(ConnectingNaoActivity.this,VisualEditorActivity.class);
        startActivity(intent);
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










