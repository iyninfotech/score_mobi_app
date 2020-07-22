package com.score3s.scoremobi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.NotificationCompat;
import androidx.core.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import android.content.Context;
import android.content.Intent;
//import android.support.v4.content.LocalBroadcastManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ballu on 21/11/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    String strBillNo="";
    String dtBillDate="";
    String decBillAmount="0.00";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.e(TAG, "From: " + remoteMessage.getFrom());
//
        if (remoteMessage == null)
            return;

        String strTitle=remoteMessage.getNotification().getTitle();
        String message=remoteMessage.getNotification().getBody();
        Log.d(TAG,"onMessageReceived: Message Received: \n" +
        "Title: "  + "\n" +
                "Message: "+ message);

        //Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
//
//            try {
//                JSONObject json = new JSONObject(remoteMessage.getData().toString());
//                handleDataMessage(json);
//            } catch (Exception e) {
//                Log.e(TAG, "Exception: " + e.getMessage());
//            }
//        }


        //sendNotification(strTitle,message);
    }

    private  void sendNotification(String title,String messageBody) {
        Intent intentnew = new Intent(getApplicationContext(),
                MainActivity.class);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("strMessageBody", messageBody);
        editor.commit();

        int requestID = (int) System.currentTimeMillis();
//
//        Bundle mBundle = new Bundle();
//        mBundle.putString("strMessageBody", messageBody);
//
//        intentnew.putExtras(mBundle);
//
        PendingIntent pIntent = PendingIntent.getActivity(
                getApplicationContext(), requestID, intentnew, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri
                (RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationbuilder=
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pIntent)
                        .setLargeIcon(BitmapFactory.decodeResource
                                (getResources(), R.mipmap.ic_launcher));;

        NotificationManager notificationManager=(NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(requestID, notificationbuilder.build());
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
           sendNotification("Sales Invoice Generated", message);
        } else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            strBillNo= data.getString("strBillNo");
            dtBillDate = data.getString("dtBillDate");
            decBillAmount= data.getString("decBillAmount");

            Log.e(TAG, "Bill No.: " + strBillNo);
            Log.e(TAG, "Bill Date: " + dtBillDate);
            Log.e(TAG, "Bill Amount: " + decBillAmount);

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
