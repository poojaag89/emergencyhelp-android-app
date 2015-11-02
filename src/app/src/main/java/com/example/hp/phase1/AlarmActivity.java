package com.example.hp.phase1;

import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AlarmActivity extends ActionBarActivity {
    MediaPlayer sosSoundPlayer;
    boolean isSoundPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Button sosAlarmButton = (Button) findViewById(R.id.sosButtonStart);

        sosAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // to start the sos sound
                if(sosSoundPlayer == null) {
                    sosSoundPlayer = MediaPlayer.create(AlarmActivity.this, R.raw.sos);
                    sosSoundPlayer.start();
                    isSoundPlaying = false;
                }
                // to stop the sos sound
                if(isSoundPlaying && sosSoundPlayer != null){
                    sosSoundPlayer.release();
                    sosSoundPlayer = null;
                }
                isSoundPlaying = true;
            }
        });
    }
}
