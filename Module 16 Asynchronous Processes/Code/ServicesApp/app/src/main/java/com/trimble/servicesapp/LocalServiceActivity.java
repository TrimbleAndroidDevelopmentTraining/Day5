package com.trimble.servicesapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URL;


public class LocalServiceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_service);

        // LocalReceiver
        IntentFilter statusIntentFilter = new IntentFilter(BROADCAST_ACTION);
        statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        ResponseReceiver responseReceiver = new ResponseReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(responseReceiver, statusIntentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.local, menu);
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

    private void startChronometer() {
        Chronometer chrono = (Chronometer)findViewById(R.id.chronometer);
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.start();
    }

    private void stopChronometer() {
        ((Chronometer)findViewById(R.id.chronometer)).stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopChronometer();
    }

    public void intentService_button_onClick(View view) {
        String msg = "HelloService";
        Intent msgIntent = new Intent(this, SimpleIntentService.class);
        msgIntent.putExtra(SimpleIntentService.PARAM_IN_MSG, msg);

        startChronometer();

        startService(msgIntent);
    }

    public void startedService_button_onClick(View view) {
        String msg = "HelloService";
        Intent msgIntent = new Intent(this, SimpleStartedBoundService.class);
        msgIntent.putExtra(SimpleStartedBoundService.PARAM_IN_MSG, msg);

        startChronometer();

        startService(msgIntent);
    }

    // LocalReceiver
    public static final String BROADCAST_ACTION = "com.trimble.servicesapp.BROADCAST";
    public static final String PARAM_OUT_MSG = "com.trimble.servicesapp.STATUS";

    private class ResponseReceiver extends BroadcastReceiver
    {
        // Prevents instantiation
        private ResponseReceiver() { }

        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(PARAM_OUT_MSG);
            stopChronometer();
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    // AsyncTask
    public void async_download_button_onClick(View view) {
        startChronometer();
        new DownloadImageTask().execute("http://blogs.courant.com/redsox/coco.jpg");
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        // ProgressDialog
        ProgressDialog dialog = new ProgressDialog(LocalServiceActivity.this);

        protected void onPreExecute() {
            dialog.setMessage("Image Loading...");
            dialog.show();
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap bmp = null;
            try {
                URL url = new URL(urls[0]);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                Log.e("EXCEPTION_TAG", e.getMessage());
            }
            return bmp;
        }

        protected void onPostExecute(Bitmap result) {
            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }

            ((ImageView)findViewById(R.id.image_view)).setImageBitmap(result);
            stopChronometer();
        }
    }
}
