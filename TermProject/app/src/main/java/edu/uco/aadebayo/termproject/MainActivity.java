package edu.uco.aadebayo.termproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG2 = "check";
    private RelativeLayout main;
    private ImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requesting to turn the title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // make it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set game panel as the view
        //setContentView(new GamePanel(this));

        main = (RelativeLayout) findViewById(R.id.main);
        pic = (ImageView) findViewById(R.id.pic);

        setContentView(R.layout.activity_main);
        Log.d(TAG, "View added");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getActionBar().hide();
        return true;
    }

    public void click_screen(View view){
        Log.d(TAG2, "VIEW");
        Intent gameActivity = new Intent(MainActivity.this, GameActivity.class);
        startActivity(gameActivity);
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
    protected void onDestroy() {
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }
    @Override
    protected void onStop() {
        Log.d(TAG, "Stopping...");
        super.onStop();
    }

}

