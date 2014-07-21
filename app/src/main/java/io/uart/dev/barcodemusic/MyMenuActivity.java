package io.uart.dev.barcodemusic;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class MyMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu);

        // button activites here
        final Button scanbutton = (Button)findViewById(R.id.scanbtn);
        scanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.screenBrightness = 80;
                getWindow().setAttributes(params);

                Toast.makeText(view.getContext(), "Going for Barcode Scan", Toast.LENGTH_LONG).show();
                Intent scanIntent = new Intent(MyMenuActivity.this, MyScanActivity.class);
                // now we start activity for scanning
                MyMenuActivity.this.startActivity(scanIntent);
            }
        });

        final Button searchbutton = (Button)findViewById(R.id.searchbtn);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent searchMusic = new Intent(MyMenuActivity.this, MyMusicActivity.class);
                MyMenuActivity.this.startActivity(searchMusic);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);

        return true;
    }
}
