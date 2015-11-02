package com.example.hp.phase1;

import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class helpactivity extends ActionBarActivity implements LocationListener {
    private TextView latituteField;
    private TextView longitudeField;
    private static final int MENU_REPORT_CRIME = Menu.FIRST;
    private static final int MENU_LOCATION = Menu.FIRST+1;
    private static final int MENU_RESET = Menu.FIRST+2;
    private static final int MENU_REPORT = Menu.FIRST+3;
    private static final int MENU_ABOUT = Menu.FIRST+4;
    private static final int MENU_LOGOUT = Menu.FIRST+5;




    private LocationManager locationManager;
    private String provider;
    final Context context = this;
    boolean isGPSEnabled = false;
    Button greenBtn;
    MediaPlayer sosSoundPlayer;
    boolean isSoundPlaying = true;
    // flag for network status
    boolean isNetworkEnabled = false;
    String[] numberlist = {"", "", "", "", "", ""};
    DB db = new DB(this);

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpactivity);
        greenBtn = (Button) findViewById(R.id.green);

         final Location location1 = getLocation();
        greenBtn.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            sendSMSMessage(location1);
                                        }
                                    }


        );
        Button sosAlarmButton = (Button) findViewById(R.id.white);

        sosAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // to start the sos sound
                if(sosSoundPlayer == null) {
                    sosSoundPlayer = MediaPlayer.create(helpactivity.this, R.raw.sos);
                    sosSoundPlayer.start();
                    isSoundPlaying = false;
                }
                // to stop the sos sound
                if(isSoundPlaying && sosSoundPlayer != null){
                    sosSoundPlayer.release();
                    sosSoundPlayer = null;
                }
                isSoundPlaying = true;
            }
        });

    }

    public Location getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        // locationManager.getLastKnownLocation();
        provider = LocationManager.GPS_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);


        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

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
            // onLocationChanged(location);
        } else {


            return location;
        }

    }

    protected void sendSMSMessage(Location currentLocation) {
        int i;
        Log.i("Send SMS", "");
        SharedPreferences prefs=getSharedPreferences("MyPref", MODE_PRIVATE);
        String mobilenum=prefs.getString("userid","5187635916");
        mobilenumbersObj mobilenumbersObj = db.getMobilenum(mobilenum);
        numberlist[0] = mobilenumbersObj.mobilenum1;
        numberlist[1] = mobilenumbersObj.mobilenum2;
        numberlist[2] = mobilenumbersObj.mobilenum3;
        numberlist[3] = mobilenumbersObj.mobilenum4;
        for (i = 0; i <= 3; i++) {


            String phoneNo = numberlist[i];
            String message = "Help!!!";
            if (phoneNo != null && phoneNo != "") {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    StringBuffer smsBody = new StringBuffer();
                    smsBody.append("Help!!!!!!!!!!!  \n");
                    smsBody.append("Please help me I am in danger \n");
                    smsBody.append("http://maps.google.com?q=");
                    smsBody.append(currentLocation.getLatitude());
                    smsBody.append(",");
                    smsBody.append(currentLocation.getLongitude());
                 Address addr=   getZipCodeFromLocation(currentLocation);
                 String address= "\n" +  addr.getCountryName() +" , "+ addr.getAdminArea() +" , "+ addr.getFeatureName()+" , "+ addr.getPostalCode()+"\n";
                    smsBody.append(address);
                    smsManager.sendTextMessage(phoneNo, null, smsBody.toString(), null, null);
                    Toast.makeText(getApplicationContext(), "SMS SENT",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }

    private Address getZipCodeFromLocation(Location location) {
        Address addr = getAddressFromLocation(location);
        return addr;
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
//        latituteField.setText(String.valueOf(lat));
  //      longitudeField.setText(String.valueOf(lng));
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
        setTitle("Emergency Help");
        menu.add(Menu.NONE, MENU_REPORT_CRIME, Menu.NONE, "Report Crime");
        menu.add(Menu.NONE, MENU_LOCATION, Menu.NONE, "Get Location Crime Rate");
        menu.add(Menu.NONE, MENU_RESET, Menu.NONE, "Reset Password");
        menu.add(Menu.NONE, MENU_REPORT, Menu.NONE, "Report an Issue");
        menu.add(Menu.NONE, MENU_ABOUT, Menu.NONE, "About");
        menu.add(Menu.NONE, MENU_LOGOUT, Menu.NONE, "Logout");




        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId())
        {
            case MENU_REPORT_CRIME:
                openReportCrime();
                return true;

            case MENU_LOCATION:
                openLocation();
                return true;

            case MENU_RESET:
                openReset();
                return true;

            case MENU_REPORT:
                openReport();
                return true;

            case MENU_ABOUT:
                openAbout();
                return true;

            case MENU_LOGOUT:
                openLogout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openReportCrime(){
        Intent intent = new Intent(context, UploadImage.class);
        startActivity(intent);
    }

    private void openLocation() {
        Intent intent = new Intent(context, CrimeRateActivity.class);
        startActivity(intent);
    }

    private void openReset() {
        Intent intent = new Intent(context, ResetPassword.class);
        startActivity(intent);
    }

    private void openReport() {
        Intent intent = new Intent(context, ReportIssue.class);
        startActivity(intent);
    }

    private void openAbout(){
        Intent intent = new Intent(context, AboutApplication.class);
        startActivity(intent);
    }

    private void openLogout(){
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }


}




