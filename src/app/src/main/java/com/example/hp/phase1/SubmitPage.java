package com.example.hp.phase1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.widget.Toast;

import java.util.Random;


public class SubmitPage extends ActionBarActivity {

    private Button Submit;
    private Button Previous;
    private EditText ConfirmationCode;
int num;
    int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_submit_page);

        Submit = (Button) findViewById(R.id.submit);
        Previous = (Button) findViewById(R.id.previous);
        ConfirmationCode= (EditText) findViewById(R.id.code);
////////////////////////sms//////
        SharedPreferences prefs=getSharedPreferences("MyPref",MODE_PRIVATE);
        String mobilenum=prefs.getString("userid","5187635916");
        String message = "Help!!!";
        if(mobilenum!= null) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                StringBuffer smsBody = new StringBuffer();
               smsBody.append("Please use this 6 digit random code:");

                Random generator = new Random();
                 num = generator.nextInt(899999) + 100000;
                smsBody.append(num);
                smsManager.sendTextMessage(mobilenum, null, smsBody.toString(), null, null);
                Toast.makeText(getApplicationContext(), "Confirmation Code Sent",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS faild, please try again.",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }



        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, FacebookTwitterPage.class);
                startActivity(intent);
            }

        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ConfirmationCode.getText().toString() != null && !ConfirmationCode.getText().toString().isEmpty()) {
                    code = Integer.parseInt(ConfirmationCode.getText().toString());
                }
                if (num == code) {
                    Intent intent = new Intent(context, FriendSignUp.class);
                    startActivity(intent);

                } else {
                     Toast.makeText(context,"Confirmation code doesn't match", Toast.LENGTH_LONG).show();

                }
            }

        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setTitle("Registration(3)");


        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }





    }

