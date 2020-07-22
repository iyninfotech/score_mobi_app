package com.score3s.scoremobi;
import android.util.Log;
import com.google.firebase.messaging.RemoteMessage;
import android.content.Intent;
import android.content.SharedPreferences;

public class LatestFirebaseMessagingService extends MyFirebaseMessagingService {

    @Override
    public void onNewToken(String mToken) {
        super.onNewToken(mToken);
        storeRegIdInPref(mToken);
        Log.e("TOKEN",mToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.apply();
    }
}
