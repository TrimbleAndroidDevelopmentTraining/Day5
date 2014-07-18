package com.trimble.remoteconnectivityapp;

import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.trimble.remoteconnectivityapp.R;

public class WebServicesActivity extends MenuActivity {

    private final String NAMESPACE = "http://www.w3schools.com/webservices/";
    private final String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";
    private final String SOAP_ACTION = "http://www.w3schools.com/webservices/CelsiusToFahrenheit";
    private final String METHOD_NAME = "CelsiusToFahrenheit";
    private String TAG = "TRIMBLE_DEBUG";
    private static String celcius;
    private static String fahren;
    Button mButton;
    TextView mTextView;
    EditText mEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_services);

        mEditText = (EditText) findViewById(R.id.edit_text);
        mTextView = (TextView) findViewById(R.id.text_view_result);
        mButton = (Button) findViewById(R.id.button);

        mButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //Check if Celcius text control is not empty
                if (mEditText.getText().length() != 0 && mEditText.getText().toString() != "") {
                    celcius = mEditText.getText().toString();
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                } else {
                    mTextView.setText("Please enter Celcius");
                }
            }
        });
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            mTextView.setText("Calculating...");
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            getFahrenheit(celcius);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            mTextView.setText(fahren + "° F");
        }
    }

    public void getFahrenheit(String celsius) {
        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("Celsius");
        celsiusPI.setValue(celsius);
        celsiusPI.setType(double.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            fahren = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
