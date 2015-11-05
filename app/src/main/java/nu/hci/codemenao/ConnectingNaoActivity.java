package nu.hci.codemenao;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
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


public class ConnectingNaoActivity extends Activity implements View.OnClickListener{

    TextView serverIp, waitTxt;
    Button startGameButton;
    TextView fontChanged1,fontChanged2,fontChanged3,fontChanged4;
    protected ServerSocket my_serverSocket;
    public static BlockingQueue<String> q;
    Handler handler;

    BufferedReader in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_nao);
        serverIp = (TextView)findViewById(R.id.serverIp);
        waitTxt = (TextView)findViewById(R.id.waitTxt);
        fontChanged1 = (TextView) findViewById(R.id.ip);
        fontChanged2 = (TextView) findViewById(R.id.serverIp);
        fontChanged3 = (TextView) findViewById(R.id.textView);
        fontChanged4 = (TextView) findViewById(R.id.waitTxt);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/kidsbold.otf");
        fontChanged1.setTypeface(face);
        fontChanged2.setTypeface(face);
        fontChanged3.setTypeface(face);
        fontChanged4.setTypeface(face);
        startGameButton = (Button) findViewById(R.id.startGameBtn);
        startGameButton.setTypeface(face);
        startGameButton.setOnClickListener(this);
        getDeviceIpAddress();
        handler = new Handler();

        // Listen on the server socket. This will run until the program is
        try {
            my_serverSocket = new ServerSocket(7200);
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
                while (true) {
                    try {
                        // Listens for a connection to be made to this socket.
                        Socket socket = my_serverSocket.accept();
                        //send message to client
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        //out.println("1");

                       //while(true){
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String msg = in.readLine();
                                try {
                                    if (msg!=null && msg.equals("ACK")){
                                        Log.d("yerchik/ack", "ack: " + msg);
                                        String temp = q.take();
                                        out.println(temp);
                                    }
                                    if (msg!=null && msg.equals("CE")){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                startGameButton.setVisibility(View.VISIBLE);
                                                waitTxt.setVisibility(View.GONE);
                                            }
                                        });
                                        out.println("CE");
                                    }

                                }catch(InterruptedException e){
                                    Log.d("yerchik", "couldn't take from queue: "+ e.getMessage());
                                }

                            // Wrap a buffered reader round the socket input stream.
                            // Read the javadoc to understand why we do this rather than dealing
                            // with reading from raw sockets.
                        //}


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

    /*public void startLevel1(View view) {
        Intent intent = new Intent(ConnectingNaoActivity.this,VisualEditorActivity.class);
        startActivity(intent);
    }*/

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

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}










