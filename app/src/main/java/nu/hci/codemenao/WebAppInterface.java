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

        // save NAO's position
        int posV = ConnectingNaoActivity.PositionVertical;
        int posH = ConnectingNaoActivity.PositonHorizontal;
        int size = commands.length();

        for (int i=0;i<size;i++){
            temp = Character.toString(commands.charAt(i));
            switch(temp){
                case "1": //move forward
                    if (ConnectingNaoActivity.direction.equals("r") ){
                        posV++;
                    } else if (ConnectingNaoActivity.direction.equals("l")){
                        posV--;
                    } else if (ConnectingNaoActivity.direction.equals("t")){
                        posH--;
                    } else { // b = bottom
                        posH++;
                    }
                    break;
                case "2": // turn left
                    break;
                case "3": // turn right
                    break;
            }
        }
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
    }
}