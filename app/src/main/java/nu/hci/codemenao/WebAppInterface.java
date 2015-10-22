package nu.hci.codemenao;


import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;
    String command;
    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }


    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String commands) {
        // send command to nao robot
        //Resource.addString("0"); // 0 means beginning of command
        for(int i=0;i<commands.length();i++){
            String temp = Character.toString(commands.charAt(i));
            try {
                ConnectingNaoActivity.q.put(temp);
                Log.d("yerchik/q", "put to queue");
            }catch(InterruptedException e){
                Log.d("yerchik/q", "couldn't put into queue: "+ e.getMessage());
            }
        }

        Toast.makeText(mContext, commands, Toast.LENGTH_SHORT).show();
    }
}