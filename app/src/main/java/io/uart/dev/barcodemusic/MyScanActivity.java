package io.uart.dev.barcodemusic;

// import io.uart.dev.barcodemusic.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.view.MotionEvent;
import android.widget.Toast;

import io.uart.dev.barcodemusic.R;

import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

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

public class MyScanActivity extends Activity implements ScanditSDKListener {


    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final boolean TOGGLE_ON_CLICK = true;
//    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
//    private SystemUiHider mSystemUiHider;
    private ScanditSDK mBarcodePicker;

    private static final String sScaditSdkAppKey = "01f2EAYFEeSXa2I8Q1x/drTyJQp9WxDJFaobe0dR9Ww";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scan);
        // we start the barcode recognition
        initializeAndStartBarcodeScanning();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_scan, menu);
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

    public void initializeAndStartBarcodeScanning() {
        // we go to fullscreen mde
        // get rid of this..
/*        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);*/

        ScanditSDKAutoAdjustingBarcodePicker picker = new ScanditSDKAutoAdjustingBarcodePicker(
                this, sScaditSdkAppKey, ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK);
        // lets add both views

        picker.getOverlayView().addListener(this);

        mBarcodePicker = picker;

        // register listener in order to be notified about relevant evnets
        // e.g if we find a barcode
        mBarcodePicker.getOverlayView().addListener(this);

        // show a search bar in the scan UI
        mBarcodePicker.getOverlayView().showSearchBar(true);

        setContentView(picker);
    }

    @Override
    protected void onPause() {
        // activity is in background we bring to front again
        mBarcodePicker.stopScanning();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mBarcodePicker.startScanning();
        super.onResume();
    }

    public void didScanBarcode(String barcode, String symbology) {
        String cleanBarcode = "";
        String oneablumname = "";
        for (int i = 0; i < barcode.length(); i++) {
            if (barcode.charAt(i) > 30) {
                cleanBarcode += barcode.charAt(i);
            }
        }

        Toast.makeText(this, symbology + ": " + cleanBarcode, Toast.LENGTH_LONG).show();
        mBarcodePicker.startScanning();
        finish();



        String albumname = getAlbumName(cleanBarcode);
        // we need to parse out the JSON to get the album name returned from getAlbumName
        try {
            JSONArray jsonArray = new JSONArray(albumname);
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

    public void didManualSearch(String entry) {
        Toast.makeText(this, "User entered: " + entry, Toast.LENGTH_LONG).show();
    }

    //@Override
    public void didCancel() {
        mBarcodePicker.stopScanning();
        finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    //Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
           // mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
//        mHideHandler.removeCallbacks(mHideRunnable);
 //       mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public String getAlbumName(String cleanBarcode) {

        // this is searching google with barcode
        String searchurl = "https://api.scandit.com/v2/products/";
        String api_key = "sDhapl6kpvCL-a_9wDVAW2jUB9s98y98I8HEb8BJ0Tg";
        String final_searchurl = searchurl + cleanBarcode + "?key=" + api_key;

        Uri uri = Uri.parse(final_searchurl);
        StringBuilder builder = new StringBuilder();
        HttpClient wsclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(final_searchurl);

        try {
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
            } else {
                Log.e(MyScanActivity.class.toString(), "Failed to get Scandit API");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // end of all try/catch
    return builder.toString();
    }


}
