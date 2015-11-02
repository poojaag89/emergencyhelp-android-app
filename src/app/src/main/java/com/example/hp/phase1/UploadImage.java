package com.example.hp.phase1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UploadImage extends ActionBarActivity {
    EditText location;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private Bitmap imageBitmap;
    public static String final_path;
    final Context context = this;
    private ImageButton report911;
    private ImageButton twitter;
    private Spinner Item;
    private EditText otherCategory;
    private EditText description;
    Photo ph = null;
    private final static String SERVICE_URI = "http://kc-sce-cs551-2.kc.umkc.edu/aspnet_client/EPG7/WCFService3/WCFService3/Service1.svc/";

    byte[] photoAsByteArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload_image);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Button cameraBtn = (Button) findViewById(R.id.cameraBtn);
        imageView = (ImageView) findViewById(R.id.imageView1);
        location = (EditText)findViewById(R.id.Location);

        cameraBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        report911 = (ImageButton) findViewById(R.id.imageButton);
        twitter = (ImageButton) findViewById(R.id.imageButton2);

        twitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getActualImagePath();
                Intent intent = new Intent(context, TwitterActivity.class);
                startActivity(intent);


            }

        });
        Item = (Spinner) findViewById(R.id.Spinner);
        otherCategory = (EditText) findViewById(R.id.Others);
        description = (EditText) findViewById(R.id.description);
        report911.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SendToServer(ph);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageView.setImageDrawable(null);
            imageView.destroyDrawingCache();
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            //getting photo as byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            photoAsByteArray = stream.toByteArray();

            //give a name of the image here as date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmm"); // should try for second and milliseconds
            String currentDateAndTime = sdf.format(new Date());

            ph = new Photo();

            String encodedImage = Base64.encodeToString(photoAsByteArray, Base64.DEFAULT);
            ph.photoasBase64 = encodedImage;

            ph.photoName = currentDateAndTime + ".jpeg";

        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }


    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void getActualImagePath() {
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(getImageUri(context, imageBitmap), proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        final_path = cursor.getString(column_index);

    }


    private void SendToServer(Photo ph2) throws UnsupportedEncodingException {
        // TODO Auto-generated method stub
        HttpPost httpPost = new HttpPost(SERVICE_URI + "imageUpload");//add here


        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        HttpClient httpClient = new DefaultHttpClient(getHttpParameterObj(17000, 17000));
        // Building the JSON object.

        com.google.gson.Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(ph2);

        StringEntity entity = new StringEntity(json, HTTP.UTF_8);
        Log.d("WebInvoke", "data : " + json.toString());


        httpPost.setEntity(entity);
        // Making the call.
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if (response == null) {
                throw new ClientProtocolException("null response");
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("Io Exception", "Io Exception", e);
        }


        // Getting data from the response to see if it was a success.
        String result=null;
        InputStream inputStream = null;
        try {
            inputStream = response.getEntity().getContent();
            CrimeRateActivity convertResp = new CrimeRateActivity();
            result = convertResp.convertInputStreamToString(inputStream);

        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        sendSMSMessage(ph.photoName);


        Log.d("WebInvoke", "ImageUpload :" + result);
    }

    private HttpParams getHttpParameterObj(int timeOutConnection, int timeOutSocket) {
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeOutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        HttpConnectionParams.setSoTimeout(httpParameters, timeOutSocket);
        return httpParameters;
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton:
                if (checked) {
                    location.setText("64110");
                    break;
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
    protected void sendSMSMessage(String imageName) {
        /*imageName=imageName.replace("\"","");
        imageName=imageName.replace("\"","");*/
        int i;
        Item = (Spinner) findViewById(R.id.Spinner);
        otherCategory = (EditText) findViewById(R.id.OtherCategory);
        description = (EditText) findViewById(R.id.description);
        Log.i("Send SMS", "Sending crime report sms");
        SharedPreferences prefs=getSharedPreferences("MyPref", MODE_PRIVATE);
      // String phoneNo = prefs.getString("userid", "5187635916");
        String phoneNo = "5182588325";
      //  String message = "Help!!!";
        if (phoneNo != null && phoneNo != "") {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                StringBuffer smsBody = new StringBuffer();
                StringBuffer smsBody1 = new StringBuffer();
                smsBody1.append("Crime: "+Item.getSelectedItem().toString()+"\n");
                if(otherCategory!=null){
                   smsBody1.append("Crime:"+otherCategory.getText().toString()+"\n");
                }
                if(description!=null){
                      smsBody1.append("Description:"+description.getText().toString()+"\n");
                }
                smsBody1.append("Please find the picture in the below link \n");
                String url= SERVICE_URI+"GetImage/"+imageName;
                smsBody.append(url);
                Log.d("Message", smsBody.toString());
                smsManager.sendTextMessage(phoneNo, null, smsBody1.toString(), null, null);

                smsManager.sendTextMessage(phoneNo, null, smsBody.toString(), null, null);

                Toast.makeText(getApplicationContext(), "Details SENT",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS failed, please try again.",
                        Toast.LENGTH_LONG).show();
                Log.e("Sms failed","SMSfailed",e);
            }
        }

    }
}

