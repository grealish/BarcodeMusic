package io.uart.dev.barcodemusic;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyMusicActivity extends Activity {

    // We should get a barcode from the MyScanActivity
    // test barcode
    String cleanBarcode = "602517318465";
    String oneablumname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the view
        setContentView(R.layout.activity_my_music);


        // TODO: implement an interface to receaive barcode and type
        new WsRestRequest().execute();


        final ListView listview = (ListView) findViewById(R.id.listview);
        // TODO: replace example with query information from API
        // String of albums for example
        /*
        String[] albums = new String[]{
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

        final StableArrayAdapter adapter = new StableArrayAdapter(this, R.id.listview, albumlist);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        albumlist.remove(item);
                        adapter.notifyDataSetChanged();
                        view.setAlpha(1);
                    }
                });
            }
        });
*/
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

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    // TODO: clean this up, factor out API and URL's
    public String getAlbumName(String cleanBarcode) {
        // this is searching google with barcode


        // end of all try/catch
        return null;
    }

    // another method to handle when the result is returned from the Async HTTP call.

    public void populateListView(String wsJsonResponce) {
        // we now do the json stuff here and populate the text and list view
        String albumjson = wsJsonResponce;

        try {
            JSONArray jsonArray = new JSONArray(albumjson);
            Log.i(MyScanActivity.class.getName(), "number of albums " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.i(MyScanActivity.class.getName(), jsonObject.getString("name"));
                oneablumname = jsonObject.getString("name");
                Log.i(MyScanActivity.class.getName(), oneablumname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, oneablumname, Toast.LENGTH_LONG).show();
    }

    private class WsRestRequest extends AsyncTask<String, Void, Void> {

        // we init the request

        String searchurl = "https://api.scandit.com/v2/products/";
        String api_key = "sDhapl6kpvCL-a_9wDVAW2jUB9s98y98I8HEb8BJ0Tg";
        String final_searchurl = searchurl + cleanBarcode + "?key=" + api_key;
        String albumjson = "";
        private String Error = null;

        // we setup some ui stuff
        TextView uiAlbumName = (TextView) findViewById(R.id.albumname);

        //private ProgressDialog Dialog = new ProgressDialog(MyMusicActivity.this);
        String data = "";


        protected void onPreExecute() {
            // ui elements can be called here
            // we start progress bar
        }
        @Override
        protected Void doInBackground(String... sUrl) {

            StringBuilder builder = new StringBuilder();
            HttpClient wsclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(final_searchurl);

            try {
                // we attempt to get data
                HttpResponse wsresponce = wsclient.execute(httpGet);
                StatusLine statusLine = wsresponce.getStatusLine();
                int statusCode = statusLine.getStatusCode();

                if (statusCode == 200) {
                    HttpEntity entity = wsresponce.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    albumjson = builder.toString();
                } else {
                    Log.e(MyMusicActivity.class.toString(), "Failded to get Sandit API");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                 } catch (IOException e) {
                    e.printStackTrace();
                    }
            return null;
        }

        // after background taks we now update and pase JSON
        protected void onPostExecute(Void unused) {
            // we can call ui elements here to update Activitiy
                // we need to parse out the JSON to get the album name returned from getAlbumName
                try {
                    JSONArray jsonArray = new JSONArray(albumjson);
                    Log.i(MyScanActivity.class.getName(), "number of albums " + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.i(MyScanActivity.class.getName(), jsonObject.getString("name"));
                        oneablumname = jsonObject.getString("name");
                        Log.i(MyScanActivity.class.getName(), oneablumname);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                uiAlbumName.setText(oneablumname);
         }


    }
}
