package com.example.hp.phase1;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private Button Submit;
    private Button SignUp;
    private Button forgotPwd;
    private EditText mob;
    private EditText password;
    final DB db= new DB(this);
    String ValidateMessage;


    public void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Submit = (Button) findViewById(R.id.button);
        SignUp = (Button) findViewById(R.id.button2);
        forgotPwd = (Button) findViewById(R.id.button4);

       mob = (EditText) findViewById(R.id.number);
       password = (EditText) findViewById(R.id.password);

        Submit.setOnClickListener(new View.OnClickListener() {
                   @Override
                  public void onClick(View v) {
                   String mobile = mob.getText().toString();
                   String Pwd = password.getText().toString();

                   String password= db.checkUser(mobile);
                       if(password=="Number doesn't exist")
                       {
                           Toast.makeText(context, "Not a registered phone number!!", Toast.LENGTH_LONG).show();

                       }
                       else {
                           Validate validate = new Validate();
                           ValidateMessage = validate.MainActivity(mobile, password);
                           if (!ValidateMessage.equalsIgnoreCase("ok")) {
                               Toast.makeText(MainActivity.this, ValidateMessage, Toast.LENGTH_LONG).show();
                           } else if (Pwd.equals(password))

                           {
                               SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                               SharedPreferences.Editor editor = pref.edit();
                               editor.putString("userid", mobile);
                               editor.commit();

                               Intent intent = new Intent(context, helpactivity.class);
                               startActivity(intent);
                           } else {
                               Toast.makeText(context, "Enter valid credentials!!", Toast.LENGTH_LONG).show();

                           }
                       }

                  }
          });


                SignUp.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(context, SignUpPage.class);
                        startActivity(intent);


                    }

                });

        forgotPwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, ForgotPasswd.class);
                startActivity(intent);


            }

        });

            }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setTitle("Login Details");


        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    }

