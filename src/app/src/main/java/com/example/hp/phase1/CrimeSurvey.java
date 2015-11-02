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
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;


public class CrimeSurvey extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {
Button submit;
    SeekBar seekBar1;
    SeekBar seekBar2;
    SeekBar seekBar3;
    SeekBar seekBar4;
    SeekBar seekBar5;
    final DB db = new DB(this);
int valueslist[]={0,0,0,0,0};
    int val1,val2,val3,val4,val5;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_survey);
        submit= (Button)findViewById(R.id.submit);
        seekBar1 = (SeekBar)findViewById(R.id.seekBar);
        seekBar2 = (SeekBar)findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar)findViewById(R.id.seekBar3);
        seekBar4 = (SeekBar)findViewById(R.id.seekBar4);
        seekBar5 = (SeekBar)findViewById(R.id.seekBar5);
        //listener
        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);
        seekBar3.setOnSeekBarChangeListener(this);
        seekBar4.setOnSeekBarChangeListener(this);
        seekBar5.setOnSeekBarChangeListener(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs=getSharedPreferences("MyPref",MODE_PRIVATE);
                String mobilenum = prefs.getString("userid", "5187635916");
            //save to db
                if(val1!=0 & val2!=0 & val3!=0 &val5!=0 &val4!=0)
                {
                    if(!duplicates(valueslist)) {
                        db.addCrimeSurvey(mobilenum,val1,val2,val3,val4,val5);
                        Toast.makeText(context, "Data Saved", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, helpactivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(context, "Assign different Priorities for each crime type", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(context, "Assign Priority for each crime type", Toast.LENGTH_LONG).show();

                }


            }

        });
    }
    boolean duplicates(final int[] valueslist)
    {
        Set<Integer> lump = new HashSet<Integer>();
        for (int i : valueslist)
        {
            if (lump.contains(i)) return true;
            lump.add(i);
        }
        return false;
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        val1=   seekBar1.getProgress();
        if(val1==5)
            val1=35;
        if(val1==4)
            val1=25;
        if(val1==3)
            val1=20;
        if(val1==2)
            val1=10;
        if(val1==1)
            val1=5;
        valueslist[0]= val1;
        val2=   seekBar2.getProgress();
        if(val2==5)
            val2=35;
        if(val2==4)
            val2=25;
        if(val2==3)
            val2=20;
        if(val2==2)
            val2=10;
        if(val2==1)
            val2=5;
        valueslist[1]= val2;
        val3=   seekBar3.getProgress();
        if(val3==5)
            val3=35;
        if(val3==4)
            val3=25;
        if(val3==3)
            val3=20;
        if(val3==2)
            val3=10;
        if(val3==1)
            val3=5;
        valueslist[2]= val3;
        val4=   seekBar4.getProgress();
        if(val4==5)
            val4=35;
        if(val4==4)
            val4=25;
        if(val4==3)
            val4=20;
        if(val4==2)
            val4=10;
        if(val4==1)
            val4=5;
        valueslist[3]= val4;
        val5=   seekBar5.getProgress();
        if(val5==5)
            val5=35;
        if(val5==4)
            val5=25;
        if(val5==3)
            val5=20;
        if(val5==2)
            val5=10;
        if(val5==1)
            val5=5;
        valueslist[4]= val5;
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setTitle("Crime Survey");

        getMenuInflater().inflate(R.menu.menu_crime_survey, menu);
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
