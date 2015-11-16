package nu.hci.codemenao;


import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;
    String command,temp;
    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;

    }


    // send commands to NAO
    @JavascriptInterface
    public void sendToNao(String commands) {
        // check if the code is correct

        // determine NAO's position depending on level

        int posV=ConnectingNaoActivity.posV;
        int posH=ConnectingNaoActivity.posH;
        int size = commands.length();


        for (int i=0;i<size;i++){
            temp = Character.toString(commands.charAt(i));
            switch(temp){
                case "1": //move forward
                    if (ConnectingNaoActivity.direction.equals("r") ){
                        posH++;
                    } else if (ConnectingNaoActivity.direction.equals("l")){
                        posH--;
                    } else if (ConnectingNaoActivity.direction.equals("t")){
                        posV--;
                    } else { // b = bottom
                        posV++;
                    }
                    break;
                case "2": // turn left
                    switch (ConnectingNaoActivity.direction){
                        case "r":
                            ConnectingNaoActivity.direction = "t";
                            break;
                        case "t":
                            ConnectingNaoActivity.direction = "l";
                            break;
                        case "l":
                            ConnectingNaoActivity.direction = "b";
                            break;
                        case "b":
                            ConnectingNaoActivity.direction = "r";
                            break;
                    }
                    break;
                case "3": // turn right
                    switch (ConnectingNaoActivity.direction){
                        case "r":
                            ConnectingNaoActivity.direction = "b";
                            break;
                        case "t":
                            ConnectingNaoActivity.direction = "r";
                            break;
                        case "l":
                            ConnectingNaoActivity.direction = "t";
                            break;
                        case "b":
                            ConnectingNaoActivity.direction = "l";
                            break;
                    }
                    break;
            }
        }

        if (posV==(int)ConnectingNaoActivity.levelDetails[ConnectingNaoActivity.playing_level-1].getFinishPos().first
                && posH == (int)ConnectingNaoActivity.levelDetails[ConnectingNaoActivity.playing_level-1].getFinishPos().second){
            Log.d("yerchik", "correct code");
            Log.d("yerchik", "level: "+ConnectingNaoActivity.playing_level);
            Log.d("yerchik", "posV:"+ConnectingNaoActivity.posV);
            Log.d("yerchik", "posH:"+ConnectingNaoActivity.posH);
            Log.d("yerchik", "end correct code logs");
            // send command to nao robot
            for(int i=0;i<size;i++){
                temp = Character.toString(commands.charAt(i));
                try {
                    ConnectingNaoActivity.q.put(temp);
                    Log.d("yerchik/q", "put to queue");
                }catch(InterruptedException e){
                    Log.d("yerchik/q", "couldn't put into queue: "+ e.getMessage());
                }
            }
            // send 0, indicates that level is finished
            try {
                ConnectingNaoActivity.q.put("0");
            } catch(InterruptedException e){
                Log.d("yerchik/q", "couldn't send end of level:"+e.getMessage());
            }
            Toast.makeText(mContext, commands, Toast.LENGTH_SHORT).show();
            ConnectingNaoActivity.posV = posV;
            ConnectingNaoActivity.posH = posH;
            ConnectingNaoActivity.playing_level++;
            try {
                ConnectingNaoActivity.q.put("" + ConnectingNaoActivity.playing_level);
            }catch(InterruptedException e){

            }
        }else {
            // wrong code
            Log.d("yerchik","wrong code");
            Log.d("yerchik", "level: " + ConnectingNaoActivity.playing_level);
            Log.d("yerchik", "posV:"+ConnectingNaoActivity.posV);
            Log.d("yerchik", "posH:"+ConnectingNaoActivity.posH);
            Log.d("yerchik", "end error code logs");
            VisualEditorActivity.myWebView.loadUrl("javascript:showRunBtn()");
            try {
                ConnectingNaoActivity.q.put("-1");
            }catch(InterruptedException e){
                // some unhandled error
            }

        }

    }

}