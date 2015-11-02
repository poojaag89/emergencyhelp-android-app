package com.example.hp.phase1;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class FacebookTwitterPage extends ActionBarActivity {

    private Button Next;
    private Button Skip;
    private Button Previous;
    private EditText facebookId;
    private EditText twitterId;
    final DB db = new DB(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_facebook_twitter_page);

        Next = (Button) findViewById(R.id.next);
        Skip = (Button) findViewById(R.id.skip);
        Previous = (Button) findViewById(R.id.button3);
        facebookId = (EditText) findViewById(R.id.fbtext);
        twitterId = (EditText) findViewById(R.id.twtext);

        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SignUpPage.class);
                startActivity(intent);
            }

        });

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SubmitPage.class);
                startActivity(intent);
            }

        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fb = facebookId.getText().toString();
                String twt = twitterId.getText().toString();
                SharedPreferences prefs=getSharedPreferences("MyPref",MODE_PRIVATE);
                String mobilenum = prefs.getString("userid", "5187635916");
                db.addSocNetwrk(mobilenum,twt,fb);
                Intent intent = new Intent(context, SubmitPage.class);
                startActivity(intent);
            }

        });



    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setTitle("Registration(2)");


        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }



}
