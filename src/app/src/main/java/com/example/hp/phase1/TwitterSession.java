package com.example.hp.phase1;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import twitter4j.auth.AccessToken;

/**
 * Created by HP on 3/16/15.
 */
public class TwitterSession {

    private SharedPreferences sharedPref;
    private Editor editor;

    private static final String TWEET_AUTH_KEY = "3008507562-wLPPZSYfGPql62V9Jz2pEvnLaswt868kMxmrIeM";
    private static final String TWEET_AUTH_SECRET_KEY = "u9N48MwzV7em33Hl1uRYN5CcVJ3K8v211sueBQ7W5AyhI";
    private static final String TWEET_USER_NAME = "pasashri@gmail.com";
    private static final String SHARED = "Twitter_Preferences";

    public TwitterSession(Context context) {
        sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);

        editor = sharedPref.edit();
    }

    public void storeAccessToken(AccessToken accessToken, String username) {
        editor.putString(TWEET_AUTH_KEY, accessToken.getToken());
        editor.putString(TWEET_AUTH_SECRET_KEY, accessToken.getTokenSecret());
        editor.putString(TWEET_USER_NAME, username);

        editor.commit();
    }

    public void resetAccessToken() {
        editor.putString(TWEET_AUTH_KEY, null);
        editor.putString(TWEET_AUTH_SECRET_KEY, null);
        editor.putString(TWEET_USER_NAME, null);

        editor.commit();
    }

    public String getUsername() {
        return sharedPref.getString(TWEET_USER_NAME, "");
    }

    public AccessToken getAccessToken() {
        String token = "3008507562-wLPPZSYfGPql62V9Jz2pEvnLaswt868kMxmrIeM";//sharedPref.getString(TWEET_AUTH_KEY, null);
        String tokenSecret = "u9N48MwzV7em33Hl1uRYN5CcVJ3K8v211sueBQ7W5AyhI";//sharedPref.getString(TWEET_AUTH_SECRET_KEY, null);

        if (token != null && tokenSecret != null)
            return new AccessToken(token, tokenSecret);
        else
            return null;
    }

}
