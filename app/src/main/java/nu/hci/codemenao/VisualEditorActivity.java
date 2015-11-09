package nu.hci.codemenao;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class VisualEditorActivity extends ActionBarActivity {

    public static WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_editor);

        final ProgressDialog pd = ProgressDialog.show(this, "", "Пожалуйста, подождите...", true);
        Intent i = getIntent();
        ConnectingNaoActivity.playing_level = i.getIntExtra("current_level",1);
        // depending on level set position of the robot
        switch(ConnectingNaoActivity.playing_level){
            case 1:
                ConnectingNaoActivity.posV = 4;
                ConnectingNaoActivity.posH = 1;
                break;
            case 2:
                ConnectingNaoActivity.posV = 4;
                ConnectingNaoActivity.posH = 2;
                break;
            case 3:
                ConnectingNaoActivity.posV = 4;
                ConnectingNaoActivity.posH = 5;
                break;
            case 4:
                ConnectingNaoActivity.posV = 4;
                ConnectingNaoActivity.posH = 5;
                break;
            case 5:
                ConnectingNaoActivity.posV = 2;
                ConnectingNaoActivity.posH = 4;
                break;
            case 6:
                ConnectingNaoActivity.posV = 0;
                ConnectingNaoActivity.posH = 1;
                break;
            case 7:
                ConnectingNaoActivity.posV = 2;
                ConnectingNaoActivity.posH = 0;
                break;
        }

         myWebView = (WebView) findViewById(R.id.visualEditorView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // load visual editor
        myWebView.addJavascriptInterface(new WebAppInterface(VisualEditorActivity.this), "Android");
        myWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pd.show();
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
                String webUrl = myWebView.getUrl();

            }

        });
        myWebView.loadUrl("http://codemenao.herokuapp.com/");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visual_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
