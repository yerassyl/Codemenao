package nu.hci.codemenao;


import android.content.Context;
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
        // send commands to nao robot

        Toast.makeText(mContext, commands, Toast.LENGTH_SHORT).show();
    }
}