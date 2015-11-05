package nu.hci.codemenao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ChooseLevel extends AppCompatActivity implements ListView.OnItemClickListener{

    ListView listView;
    ArrayList<String> listLevels;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);

        listView = (ListView) findViewById(R.id.listView);
        listLevels = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listLevels);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/kidsbold.otf");

        int lastLevel = getIntent().getIntExtra("current_level",1);
        for (int i=1;i<=lastLevel;i++) {
            listLevels.add("Задание " + i);
        }
        listView.setOnItemClickListener(this);

        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String item = (String) listView.getItemAtPosition(position);
            switch(position) {
                case 0:
                    try{ConnectingNaoActivity.q.put("1");}
                    catch (InterruptedException e){e.printStackTrace();}
                    Toast.makeText(getApplicationContext(),ConnectingNaoActivity.q.peek(),Toast.LENGTH_SHORT).show();
                    Intent  i1 = new Intent(this,VisualEditorActivity.class);
                    i1.putExtra("current_level",1);
                    startActivity(i1);
                    break;
                case 1:
                    try{ConnectingNaoActivity.q.put("2");}
                    catch (InterruptedException e){e.printStackTrace();}
                    Intent  i2 = new Intent(this,VisualEditorActivity.class);
                    i2.putExtra("current_level",2);
                    startActivity(i2);
                    break;
                case 2:
                    try{ConnectingNaoActivity.q.put("3");}
                    catch (InterruptedException e){e.printStackTrace();}
                    Intent  i3 = new Intent(this,VisualEditorActivity.class);
                    i3.putExtra("current_level",3);
                    startActivity(i3);
                    break;
                case 3:
                    try{ConnectingNaoActivity.q.put("4");}
                    catch (InterruptedException e){e.printStackTrace();}
                    Intent  i4 = new Intent(this,VisualEditorActivity.class);
                    i4.putExtra("current_level",4);
                    startActivity(i4);
                    break;
                case 4:
                    try{ConnectingNaoActivity.q.put("5");}
                    catch (InterruptedException e){e.printStackTrace();}
                    Intent  i5 = new Intent(this,VisualEditorActivity.class);
                    i5.putExtra("current_level",5);
                    startActivity(i5);
                    break;
                case 5:
                    try{ConnectingNaoActivity.q.put("6");}
                    catch (InterruptedException e){e.printStackTrace();}
                    Intent  i6 = new Intent(this,VisualEditorActivity.class);
                    i6.putExtra("current_level",6);
                    startActivity(i6);
                    break;
                case 6:
                    try{ConnectingNaoActivity.q.put("7");}
                    catch (InterruptedException e){e.printStackTrace();}
                    Intent  i7 = new Intent(this,VisualEditorActivity.class);
                    i7.putExtra("current_level",7);
                    startActivity(i7);
                    break;
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_level, menu);
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
