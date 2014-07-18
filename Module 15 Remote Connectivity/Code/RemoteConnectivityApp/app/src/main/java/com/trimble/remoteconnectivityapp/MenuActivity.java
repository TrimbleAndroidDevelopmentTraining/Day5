package com.trimble.remoteconnectivityapp;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.trimble.remoteconnectivityapp.NetworkActivity;
import com.trimble.remoteconnectivityapp.R;
import com.trimble.remoteconnectivityapp.RESTfulServicesActivity;
import com.trimble.remoteconnectivityapp.WebServicesActivity;

public abstract class MenuActivity extends Activity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        menu.findItem(R.id.network_menu_item).setIntent(new Intent(this, NetworkActivity.class));
        menu.findItem(R.id.web_services_menu_item).setIntent(new Intent(this, WebServicesActivity.class));
        menu.findItem(R.id.rest_services_menu_item).setIntent(new Intent(this, RESTfulServicesActivity.class));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(item.getIntent());
        super.onOptionsItemSelected(item);
        return true;
    }
}
