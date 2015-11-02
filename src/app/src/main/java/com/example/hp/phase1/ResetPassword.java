package com.example.hp.phase1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ResetPassword extends ActionBarActivity {
    private Button Submit;
    private EditText oldpswd;
    private EditText newpswd;
    private EditText newcnfrmpswd;
    final DB db= new DB(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_reset_password);
        Submit = (Button) findViewById(R.id.button);
        oldpswd = (EditText) findViewById(R.id.number);
        newpswd = (EditText) findViewById(R.id.password1);
        newcnfrmpswd = (EditText) findViewById(R.id.password2);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newpswd.getText().toString().equals(newcnfrmpswd.getText().toString())) {
                    SharedPreferences prefs=getSharedPreferences("MyPref",MODE_PRIVATE);
                    String mobilenum=prefs.getString("userid","5187635916");
                    db.updatePassword(newpswd.getText().toString(), mobilenum);
                    Toast.makeText(context, "Password reset done!!!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, helpactivity.class);
                    startActivity(intent);

                }
                else
                    Toast.makeText(context, "Password & Confirm password doesn't match", Toast.LENGTH_LONG).show();


            }
        });
    }

               @Override
               public boolean onCreateOptionsMenu(Menu menu) {
                   // Inflate the menu; this adds items to the action bar if it is present.
                   setTitle("Reset Password");
                   getMenuInflater().inflate(R.menu.menu_reset_password, menu);
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
