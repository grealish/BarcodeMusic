package io.uart.dev.barcodemusic;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import io.uart.dev.barcodemusic.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyMusicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_music);

        final ListView listview = (ListView) findViewById(R.id.listview);
        // TODO: replace example with query information from API
        // String of albums for example
        String[] albums = new String[] {
                "Coldplay - Clocks (Kungs Edit)",
                "Coldplay X & Y Album",
                "Daft Punk - Random Access Memories (2013) [FLAC]",
                "Enigma MCMXC a.D. (Limited Edition)",
                "Frames - Burn The Maps - (2005)",
                "Frames - The Cost - (2006)",
                "Idealism + Bonus Tracks",
                "Jose Gonzalez - Veneer",
                "Joseph Arthur-2013-The Ballad Of Boogie Christ",
                "Michael Cassette - Temporarity (2010)",
                "Poolside-Pacific Standard Time",
                "Sunlounger - Balearic Beauty (Inspiron)",
                "Tesla Boy - The Universe Made of Darkness (2013)",
                "Tycho - Awake (2014) [FLAC]",
                "Tycho - Dive (2011)",


        };

        final ArrayList<String> albumlist = new ArrayList<String>();
        for (int i = 0; i < albums.length; ++i) {
            albumlist.add(albums[i]);
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,)
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_music, menu);
        return true;
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

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer>
    }
}
