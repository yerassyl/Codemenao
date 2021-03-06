package nu.hci.codemenao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private LinearLayout layout;
    private Typeface face;
    private Button newGame, resume, chooseLevel, manual;
    public static SharedPreferences levels;

    public static int level = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newGame = (Button) findViewById(R.id.new_game);
        resume = (Button) findViewById(R.id.resume);
        chooseLevel = (Button) findViewById(R.id.choose);
        manual = (Button) findViewById(R.id.manual);

        layout = (LinearLayout) findViewById(R.id.layout);
        newGame.setOnClickListener(this);
        resume.setOnClickListener(this);
        chooseLevel.setOnClickListener(this);
        manual.setOnClickListener(this);
        // get ip and port from shared prefs
        levels = getSharedPreferences("LEVELS", MODE_PRIVATE);
        levels.edit().putInt("current_level",level).apply();
        face = Typeface.createFromAsset(getAssets(),"fonts/kidsbold.otf");
        newGame.setTypeface(face);
        resume.setTypeface(face);
        chooseLevel.setTypeface(face);
        manual.setTypeface(face);
        // This code just for testing
        //Intent intent = new Intent(MainActivity.this,ConnectingNaoActivity.class);
        //startActivity(intent);
        // ends here

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_game:
                Intent fromNew = new Intent(this,VisualEditorActivity.class);
                level = 1;
                fromNew.putExtra("current_level", level);
                startActivity(fromNew);
                break;

            case R.id.resume:
                Intent fromResume = new Intent(this,VisualEditorActivity.class);
                fromResume.putExtra("current_level", levels.getInt("current_level",1));
                startActivity(fromResume);
                break;

            case R.id.choose:
                Intent fromChoose = new Intent(this,ChooseLevel.class);
                fromChoose.putExtra("current_level",levels.getInt("current_level",1));
                startActivity(fromChoose);
                break;
            case R.id.manual:
                layout.addView(createNewTextView("Вам будут предложены 7 заданий разной сложности. Чтобы перейти к следующему заданию" +
                        ",следуйте указаниям робота. Перетаскивайте и составляйте блоки из команд слева в части экрана справа и нажмите" +
                        "запустить, и робот выполнит это."));
                break;
        }
    }
    private TextView createNewTextView(String text){
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView= new TextView(this);
        textView.setLayoutParams(params);
        textView.setTextSize(20f);
        textView.setText(text);
        return textView;
    }
}
