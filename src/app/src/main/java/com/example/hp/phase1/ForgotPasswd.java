package com.example.hp.phase1;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ForgotPasswd extends ActionBarActivity {
    EditText ed;
    final DB db= new DB(this);
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwd);

        ed= (EditText)findViewById(R.id.editText);
        Button button= (Button)findViewById(R.id.button);
        Button button1= (Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String mob= ed.getText().toString();
                String password= db.checkUser(mob);

                if(password=="Number doesn't exist")
                {
                    Toast.makeText(context, "Not a registered phone number!!", Toast.LENGTH_LONG).show();

                }
                else {
                    sendSMSMessage(mob,password);

                }
            }

        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }

        });
    }
    protected void sendSMSMessage(String mob, String passwd) {

            String phoneNo = mob;

            if (phoneNo != null) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    StringBuffer smsBody = new StringBuffer();
                    smsBody.append("Hello "+mob);
                    smsBody.append(". Your password is "+passwd);

                    smsManager.sendTextMessage(phoneNo, null, smsBody.toString(), null, null);
                    Toast.makeText(getApplicationContext(), "Password is sent to your mobile number",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setTitle("Forgot Password");

        getMenuInflater().inflate(R.menu.menu_forgot_passwd, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
