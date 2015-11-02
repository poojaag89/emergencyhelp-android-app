package com.example.hp.phase1;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.net.Uri;
import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FriendSignUp extends ActionBarActivity {
    private Button addContactsBtn;
    private Button submitBtn;
    private static final int REQUEST_SELECT_CONTACT = 1;
    private ListView listView;
    private List<String> contacts = new ArrayList<>();
    private String[] dbmobilenm={"","","","",""};
    private Map<String, ContactInfo> contactInfoMapById = new HashMap<>();
    int i=0;
    DB db= new DB(this);
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_sign_up);

        addContactsBtn = (Button) findViewById(R.id.contacts);
        addContactsBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (contactInfoMapById.size() > 3) {
                    Toast.makeText(context, "You can only add 4 contacts", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_SELECT_CONTACT);
                }
            }
        });

        submitBtn = (Button) findViewById(R.id.submit);
        submitBtn.setVisibility(View.INVISIBLE);
        submitBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                SharedPreferences prefs=getSharedPreferences("MyPref",MODE_PRIVATE);
                String mobilenum=prefs.getString("userid","5187635916");
                mobilenumbersObj  mobilenumbersObj= new mobilenumbersObj(dbmobilenm[0],dbmobilenm[1],dbmobilenm[2],dbmobilenm[3]);
                db.addPhnum(mobilenumbersObj,mobilenum);
                Intent intent = new Intent(context,CrimeSurvey.class);
                startActivity(intent);
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {
                case (REQUEST_SELECT_CONTACT):
                    if (resultCode == Activity.RESULT_OK) {
                        Uri contactData = data.getData();
                        Cursor c = managedQuery(contactData, null, null, null, null);
                        if (c.moveToFirst()) {
                            String id =
                                    c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                            String hasPhone =
                                    c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                            if (hasPhone.equalsIgnoreCase("1")) {
                                Cursor phones = getContentResolver().query(
                                        CommonDataKinds.Phone.CONTENT_URI, null,
                                        CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                        null, null);
                                phones.moveToFirst();
                                String phn_no = phones.getString(phones.getColumnIndex("data1"));
                                String name = c.getString(c.getColumnIndex(CommonDataKinds.StructuredPostal.DISPLAY_NAME));
                                if (contactInfoMapById.containsKey(id)) {
                                    Toast.makeText(this, "This Contact is Already added", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                contactInfoMapById.put(id, new ContactInfo(name, phn_no));
                                contacts.add(name + "\n" + phn_no);
                                dbmobilenm[i]=phn_no;
                                i++;


                            }
                        }
                    }
            }
        }

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, contacts);
        //Swipe to dissmiss
        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    adapter.remove(adapter.getItem(position));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());

 /*       // Set up normal ViewGroup example
        final ViewGroup dismissableContainer = (ViewGroup) findViewById(R.id.dismissable_container);
        for (int i = 0; i < items.length; i++) {
            final Button dismissableButton = new Button(this);
            dismissableButton.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dismissableButton.setText("Button " + (i + 1));
            dismissableButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this,
                            "Clicked " + ((Button) view).getText(),
                            Toast.LENGTH_SHORT).show();
                }
            });*/
            /*// Create a generic swipe-to-dismiss touch listener.
            dismissableButton.setOnTouchListener(new SwipeDismissTouchListener(
                    dismissableButton,
                    null,
                    new SwipeDismissTouchListener.DismissCallbacks() {
                        @Override
                        public boolean canDismiss(Object token) {
                            return true;
                        }

                        @Override
                        public void onDismiss(View view, Object token) {
                            dismissableContainer.removeView(dismissableButton);
                        }
                    }));
            dismissableContainer.addView(dismissableButton);*/
        //}



    // Assign adapter to ListView
        listView.setAdapter(adapter);

        if (contactInfoMapById.size() == 2) {
            submitBtn.setVisibility(View.VISIBLE);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setTitle("Add Contacts");


        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
}

