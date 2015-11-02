package com.example.hp.phase1;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by HP on 3/16/15.
 */
public class TwitterActivity extends ActionBarActivity implements OnClickListener{

    private TwitterApp mTwitter;
    Button mBtnTwitter;
    private static final String CONSUMER_KEY = "ZXaE8Lf89SshHoRFnZiKQLiYR";
    private static final String CONSUMER_SECRET = "IpLJBb9bNUHOg0hQ7ChO9Qeunzd6ilKA5zFGq1F4OHNRUpUraE";

    // For Sample_Demo_Application

    // private static final String CONSUMER_KEY = "1ozjDiHOqJq4UkowfvZIA";
    // private static final String CONSUMER_SECRET =
    // "FPToH6HrOZa5TttS0dSGjgPsNIHYCgCJA5uki8Bi00";

    private enum FROM {
        TWITTER_POST, TWITTER_LOGIN
    };

    private enum MESSAGE {
        SUCCESS, DUPLICATE, FAILED, CANCELLED
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mTwitter = new TwitterApp(this, CONSUMER_KEY, CONSUMER_SECRET);
        mBtnTwitter = (Button) findViewById(R.id.tweet);
      //  mBtnTwitter.setOnClickListener(this);
        mBtnTwitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mTwitter.setListener(mTwLoginDialogListener);
//        mTwitter.resetAccessToken();
                if (mTwitter.hasAccessToken() == true) {
                    try {
//                mTwitter.updateStatus(String.valueOf(Html
//                        .fromHtml(TwitterApp.MESSAGE)));

                        EditText TweetContent = (EditText)findViewById(R.id.tweetContent);
                        mTwitter.updateStatus(TweetContent.getText().toString());
                        // File f = new File("/mnt/sdcard/android.jpg");
                        // mTwitter.uploadPic(f, String.valueOf(Html
                        // .fromHtml(TwitterApp.MESSAGE)));

                        postAsToast(FROM.TWITTER_POST, MESSAGE.SUCCESS);
//                mTwitter.resetAccessToken();
                    } catch (Exception e) {
                        if (e.getMessage().toString().contains("duplicate")) {
                            postAsToast(FROM.TWITTER_POST, MESSAGE.DUPLICATE);
                        }
                        e.printStackTrace();
                    }
//            mTwitter.resetAccessToken();
                } else {
                    mTwitter.authorize();
                }
            }        });
        }


    public void onClick(View v) {
        mTwitter.setListener(mTwLoginDialogListener);
//        mTwitter.resetAccessToken();
        if (mTwitter.hasAccessToken() == true) {
            try {
//                mTwitter.updateStatus(String.valueOf(Html
//                        .fromHtml(TwitterApp.MESSAGE)));

              EditText TweetContent = (EditText)findViewById(R.id.tweetContent);
                mTwitter.updateStatus(TweetContent.getText().toString());
                // File f = new File("/mnt/sdcard/android.jpg");
                // mTwitter.uploadPic(f, String.valueOf(Html
                // .fromHtml(TwitterApp.MESSAGE)));

                postAsToast(FROM.TWITTER_POST, MESSAGE.SUCCESS);
//                mTwitter.resetAccessToken();
            } catch (Exception e) {
                if (e.getMessage().toString().contains("duplicate")) {
                    postAsToast(FROM.TWITTER_POST, MESSAGE.DUPLICATE);
                }
                e.printStackTrace();
            }
//            mTwitter.resetAccessToken();
        } else {
            mTwitter.authorize();
        }
    }
    ///

    private void postAsToast(FROM twitterPost, MESSAGE success) {
        switch (twitterPost) {
            case TWITTER_LOGIN:
                switch (success) {
                    case SUCCESS:
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG)
                                .show();
                        break;
                    case FAILED:
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
                    default:
                        break;
                }
                break;
            case TWITTER_POST:
                switch (success) {
                    case SUCCESS:
                        Toast.makeText(this, "Posted Successfully", Toast.LENGTH_LONG)
                                .show();
                        break;
                    case FAILED:
                        Toast.makeText(this, "Posting Failed", Toast.LENGTH_LONG)
                                .show();
                        break;
                    case DUPLICATE:
                        Toast.makeText(this,
                                "Posting Failed because of duplicate message...",
                                Toast.LENGTH_LONG).show();
                    default:
                        break;
                }
                break;
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setTitle("Twitter");

        getMenuInflater().inflate(R.menu.menu_twitter, menu);

        return true;
    }
    private TwitterApp.TwDialogListener mTwLoginDialogListener = new TwitterApp.TwDialogListener() {

        public void onError(String value) {
            postAsToast(FROM.TWITTER_LOGIN, MESSAGE.FAILED);
            Log.e("TWITTER", value);
            mTwitter.resetAccessToken();
        }

        public void onComplete(String value) {
            try {
                mTwitter.updateStatus(TwitterApp.MESSAGE);
                postAsToast(FROM.TWITTER_POST, MESSAGE.SUCCESS);
            } catch (Exception e) {
                if (e.getMessage().toString().contains("duplicate")) {
                    postAsToast(FROM.TWITTER_POST, MESSAGE.DUPLICATE);
                }
                e.printStackTrace();
            }
            mTwitter.resetAccessToken();
        }
    };
}
