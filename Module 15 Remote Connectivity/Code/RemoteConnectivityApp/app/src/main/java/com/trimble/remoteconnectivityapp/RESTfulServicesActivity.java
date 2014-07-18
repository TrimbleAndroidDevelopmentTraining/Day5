package com.trimble.remoteconnectivityapp;

import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RESTfulServicesActivity extends MenuActivity {

    private final String URL = "http://www.cheesejedi.com/rest_services/get_big_cheese.php?puzzle=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restful_services);
    }

    public void button_click(View view) {
        ((Button)findViewById(R.id.button)).setClickable(false);

        new LongRunningGetIO().execute();
    }

    private class LongRunningGetIO extends AsyncTask <Void, Void, String> {

        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n>0) {
                byte[] b = new byte[4096];
                n =  in.read(b);
                if (n>0) out.append(new String(b, 0, n));
            }
            return out.toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(URL);

            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
            return text;
        }

        protected void onPostExecute(String results) {
            if (results!=null) {
                ((TextView)findViewById(R.id.text_view_result)).setText(results);
            }
            ((Button)findViewById(R.id.button)).setClickable(true);
        }
    }

}
