package com.example.hp.phase1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;


public class CrimeRateActivity extends ActionBarActivity implements LocationListener {

    EditText etResponse;
    EditText location;
    TextView tvIsConnected;
    TextView CrimeDesc;
    private LocationManager locationManager;
    private String provider;
    final Context context = this;
    boolean isGPSEnabled = false;
    String zip_code;
    String zipcode_crime;
    Location l;
    Button crimebtn;
    String pop;
    int crimeCount;
    int finalCrimeCount;
    final DB db = new DB(this);
    CrimeSurveyObj crimeSurveyObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_rate);
        tvIsConnected= (TextView)findViewById(R.id.textView3);
         etResponse = (EditText)findViewById(R.id.rate);
        location = (EditText)findViewById(R.id.Location);
        l=getLocation();
        crimebtn = (Button) findViewById(R.id.done);
        crimebtn.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            tvIsConnected.setVisibility(View.VISIBLE);
                                            zipcode_crime=location.getText().toString();
                                                new HttpAsyncTask().execute("https://data.kcmo.org/resource/2pwu-dwda.json?zip_code_1="+zipcode_crime);
                                            new HttpAsyncTask1().execute("http://api.census.gov/data/2013/acs5?get=B00001_001E&for=zip+code+tabulation+area:"+zipcode_crime);
                                        }
                                    }


        );

        // check if you are connected or not
        if(isConnected()){

        }
        else{

        }
        // call AsynTask to perform network operation on separate thread

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("rows.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton:
                if (checked) {
                    zip_code=  getZipCodeFromLocation(l);
                    zipcode_crime = zip_code;
                    location.setText(zip_code);
                    break;
                }

        }
    }
    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.



        @Override
        protected void onPostExecute(String result) {
           EditText ed= (EditText) findViewById(R.id.editText2);
            ed.setVisibility(View.VISIBLE);
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            int i=0,k=0,l=0,m=0,n=0;
            CrimeDesc = (TextView)findViewById(R.id.textView4);

            int count=0;
            try
            {
                JSONArray j = new JSONArray(result);
                if( result!= null) {

                    count=j.length();
                }
                 crimeCount=count;
                int length= j.length();
                for(int in=0;in<length;in++) {
                    JSONObject o = j.getJSONObject(in);
                    String desc = o.getString("description");
                    if(desc.toLowerCase().contains("murder"))
                    {
                        i++;
                    }
                    if(desc.toLowerCase().contains("theft")||desc.contains("stealing")||desc.contains("burglary"))
                    {
                        k++;
                    }
                    if(desc.toLowerCase().contains("armed robbery"))
                    {
                        l++;
                    }
                    if(desc.toLowerCase().contains("assault"))
                    {
                        m++;
                    }
                    if(desc.toLowerCase().contains("rape")||desc.contains("sodomy"))
                    {
                        n++;
                    }
                }

                SharedPreferences prefs=getSharedPreferences("MyPref",MODE_PRIVATE);
                String mobilenum = prefs.getString("userid", "5187635916");
                crimeSurveyObj=db.getCrimeObj(mobilenum);
                if(crimeSurveyObj==null)
                 finalCrimeCount = crimeCount * 100;
                else
                {
                    finalCrimeCount = (i * crimeSurveyObj.val1)+(n * crimeSurveyObj.val2) +
                            (k * crimeSurveyObj.val3)+(m * crimeSurveyObj.val4)+(l * crimeSurveyObj.val5)+((crimeCount-(l+m+n+k+i))* 5);

                }
                int murderPercentage= (i * 100) /count;
                int theftPercentage = (k * 100)/count;
                int assaultPercentage = (m * 100)/count;
                int robberyPercentage = (l * 100)/count;
                int RapePercentage = ( n * 100)/count;
                int other = (100 - (murderPercentage + theftPercentage + assaultPercentage + RapePercentage+robberyPercentage));

                CrimeDesc.setText(" Murder: " + murderPercentage + "%" + "\n Theft: " + theftPercentage + "%\n Assault: "
                        + assaultPercentage + "%" + "\n Armed Robbery: " + robberyPercentage + "%" + "\n Rape:" + RapePercentage + "%" + "\n Misc Violence: " + other + "%");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    private class HttpAsyncTask1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            JSONArray j;

            try
            {
                j = new JSONArray(result);
                if( result!= null)
                {
                    JSONArray j1=  j.getJSONArray(1);
                     pop=   j1.getString(0);
                }

                calculateCrime();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void calculateCrime()
    {
        double population;
        double crimeRate;
        try {
          population= Double.parseDouble(pop);
          crimeRate = Math.round(((finalCrimeCount)/population)* 1000)/1000 ;
          String Crime= "Crime Rate is : "+ crimeRate;
            etResponse.setText(Crime);
            CrimeDesc.setVisibility(View.VISIBLE);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private String getZipCodeFromLocation(Location location) {
        Address addr = getAddressFromLocation(location);
        return addr.getPostalCode() == null ? "" : addr.getPostalCode();
    }

    private Address getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this);
        Address address = new Address(Locale.getDefault());
        try {
            List<Address> addr = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addr.size() > 0) {
                address = addr.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }
    public Location getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = LocationManager.NETWORK_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);

        boolean enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

// check if enabled and if not send user to the GSP settings
// Better solution would be to display a dialog and suggesting to
// go to the settings
        if (!enabled) {
            System.out.println("Provider " + provider + " not been selected.");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            return location;
        } else {
            return location;
        }

    }
    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setTitle("Crime Rate");
        getMenuInflater().inflate(R.menu.menu_crime_rate, menu);
        return true;
    }
}