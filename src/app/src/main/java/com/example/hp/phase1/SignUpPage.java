package com.example.hp.phase1;

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


public class SignUpPage extends ActionBarActivity {

    private Button Next;
    private Button Previous;
    private EditText firstName;
    private EditText lastName;
    private EditText mobileNumber;
    private EditText address1;
    private EditText address2;
    private EditText city;
    private EditText state;
    private EditText postalCode;
    private EditText emailid;
    private EditText password;
    private EditText renterPassword;
    final DB db = new DB(this);
    String validationMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       final Context context = this;
        setContentView(R.layout.activity_sign_up_page);

        Next = (Button) findViewById(R.id.next);
        Previous = (Button) findViewById(R.id.previous);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        mobileNumber = (EditText) findViewById(R.id.number);
        address1 = (EditText) findViewById(R.id.address1);
        address2 = (EditText) findViewById(R.id.address2);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        emailid=(EditText) findViewById(R.id.email);
        postalCode = (EditText) findViewById(R.id.postalCode);
        password = (EditText) findViewById(R.id.password);
        renterPassword = (EditText) findViewById(R.id.renterPassword);

        Next.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String fn = firstName.getText().toString();
                                        String ln = lastName.getText().toString();
                                        String num= mobileNumber.getText().toString();
                                        String email= emailid.getText().toString();
                                        String add1 = address1.getText().toString();
                                        String add2 = address2.getText().toString();
                                        String ct = city.getText().toString();
                                        String st = state.getText().toString();
                                        String code = (postalCode.getText().toString());
                                        String pwd = password.getText().toString();
                                        String repwd = renterPassword.getText().toString();
                                        UserObj user= new UserObj(fn,ln,num,email,add1,add2,ct,st,"USA",code,pwd,repwd);

                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("userid", num);
                                        editor.commit();


                                        Validate validate=new Validate();
                                           validationMessage=validate.SignUpPage(user);
                                            if(!validationMessage.equalsIgnoreCase("ok"))
                                           {
                                    Toast.makeText(com.example.hp.phase1.SignUpPage.this,validationMessage, Toast.LENGTH_LONG).show();
                                            	      }
                                                                                else
                                                                                {
                                                                                    int value= db.addUser(user);
                                                                                    if(value==111)
                                                                                    {
                                                                                        Toast.makeText(context, "Mobile number is already registered!!", Toast.LENGTH_LONG).show();

                                                                                    }
                                                                                    else {
                                                                                        Intent intent = new Intent(context, FacebookTwitterPage.class);

                                                                                        startActivity(intent);
                                                                                    }

                                            	                                        }

            }
        });

        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

});
        }
public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setTitle("Registration(1)");

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    }

