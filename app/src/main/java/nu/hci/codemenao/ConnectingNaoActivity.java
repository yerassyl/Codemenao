package nu.hci.codemenao;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
import nu.hci.codemenao.model.Level;


public class ConnectingNaoActivity extends Activity implements View.OnClickListener{

    TextView serverIp, waitTxt;
    TextView fontChanged1,fontChanged2,fontChanged3,fontChanged4;

    private Button btn;

    protected ServerSocket my_serverSocket;
    public static BlockingQueue<String> q;
    Handler handler;
    BufferedReader in;

    // Maze declaration
    public static int[][] maze = {
            {0,1,1,0,0,1},
            {1,1,1,1,1,1},
            {1,0,0,1,1,0},
            {0,0,1,1,1,1},
            {0,1,1,1,1,1}
    };
    public static int posV = 4; // current position of the robot
    public static int posH = 1;
    public static String direction = "r";  // r=right, l=left, t=top,b=bottom
    /*
     ---> H
     |
     |
     V
     */
    public static int playing_level;
    // levels and their corresponding starting and finish positions with starting directions of the robot
    // this code is not best practice
    // should be improved later
    public static Level level1 = new Level(4,1,4,2,"r"); // level 1
    public static Level level2 = new Level(4,2,4,5,"r"); // level 2
    public static Level level3 = new Level(4,5,4,5,"r"); // level 3
    public static Level level4 = new Level(4,5,2,4,"t"); // level 4
    public static Level level5 = new Level(2,4,0,1,"t"); // level 5
    public static Level level6 = new Level(0,1,2,0,"t"); // level 6
    public static Level level7 = new Level(2,0,1,4,"t"); // level 7
    public static Level[] levelDetails = { level1,level2,level3,level4,level5,level6,level7};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_nao);

        btn = (Button) findViewById(R.id.startGameBtn);
        btn.setOnClickListener(this);

        serverIp = (TextView)findViewById(R.id.serverIp);
        waitTxt = (TextView)findViewById(R.id.waitTxt);
        fontChanged1 = (TextView) findViewById(R.id.ip);
        fontChanged2 = (TextView) findViewById(R.id.serverIp);
        fontChanged3 = (TextView) findViewById(R.id.textView);
        fontChanged4 = (TextView) findViewById(R.id.waitTxt);

        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/kidsbold.otf");
        btn.setTypeface(face);

        fontChanged1.setTypeface(face);
        fontChanged2.setTypeface(face);
        fontChanged3.setTypeface(face);
        fontChanged4.setTypeface(face);
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
                                    if (msg!=null && msg.equals("CE")){
                                        out.println("CE");
                                        Intent intent = new Intent(ConnectingNaoActivity.this,MainActivity.class);
                                        startActivity(intent);

                                    }
                                    if (msg!=null && msg.equals("LVL")){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (VisualEditorActivity.myWebView!=null) {
                                                    VisualEditorActivity.myWebView.loadUrl("javascript:showRunBtn()");
                                                }
                                            }
                                        });
                                        String temp = q.take();
                                        out.println(temp);
                                    }
                                    if (msg!=null && msg.equals("ACK")){
                                        Log.d("yerchik/ack", "ack: " + msg);
                                        String temp = q.take();
                                        out.println(temp);
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

    // just for testing
        //Intent intent = new Intent(ConnectingNaoActivity.this,MainActivity.class);
        //startActivity(intent);
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










