package com.example.hp.phase1;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class ReportIssue extends ActionBarActivity {
    private EditText issue;
    private Button Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);
        String mobilenum = "5187635916";
        issue = (EditText) findViewById(R.id.issue);
        Submit = (Button) findViewById(R.id.report);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSMessage(issue.getText().toString());

            }
        });
    }


             @Override
             public boolean onCreateOptionsMenu(Menu menu) {
                 // Inflate the menu; this adds items to the action bar if it is present.
                 setTitle("Report an Issue");
                 getMenuInflater().inflate(R.menu.menu_report_issue, menu);
                 return true;
             }

             protected void sendSMSMessage(String text) {

                 String phoneNo = "5187635916";
                 if (phoneNo != null && phoneNo != "") {
                     try {
                         SmsManager smsManager = SmsManager.getDefault();
                         smsManager.sendTextMessage(phoneNo, null, text.toString(), null, null);
                         Toast.makeText(getApplicationContext(), "SMS SENT",
                                 Toast.LENGTH_LONG).show();
                     } catch (Exception e) {
                         Toast.makeText(getApplicationContext(),
                                 "SMS failed, please try again.",
                                 Toast.LENGTH_LONG).show();
                         Log.e("Sms failed", "SMSfailed", e);
                     }
                 }
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
