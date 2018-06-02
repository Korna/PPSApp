package com.coma.go.Web;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.coma.go.R;
import com.coma.go.Utils.Logger;
import com.coma.go.View.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



/**
 * Created by Koma on 12.03.2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
           // sendNewNotification("Title:" + remoteMessage.getNotification().getTitle(),
           //         "Body:" + remoteMessage.getNotification().getBody());
        sendNewNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody());

    }

    private void sendDeprecatedNoty(RemoteMessage remoteMessage){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        notificationBuilder.setContentTitle(remoteMessage.getNotification().getTitle());
        notificationBuilder.setContentText(remoteMessage.getNotification().getBody());

        notificationBuilder.setColor(Color.parseColor("#4B8A08"));
        notificationBuilder.setAutoCancel(true);
        //notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        notificationBuilder.setLights(Color.MAGENTA, 500, 1000);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }


    private void sendNewNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* RequestTopic code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "default";

        Logger.d(TAG, "new type of notification");

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setColor(Color.parseColor("#4B8A08"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    title,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        Log.i(TAG, "Message sent:" + s);
    }

    @Override
    public void onSendError(String msgId, Exception e) {
        Logger.e(TAG, "onSendError: " + msgId);
        Logger.e(TAG, "Exception: " + e);

    }


    private void sendNotification(String messageTitle, String messageBody) {
        Intent intent = null;
        //intent = new Intent(this, MainActivity.class);
        intent.putExtra("title",  messageTitle);
        intent.putExtra("body",  messageBody);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0
                //request code here
                , intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_krolik);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                // .setSmallIcon(R.mipmap.ic_stat_s1)
                // .setLargeIcon(largeIcon)
                .setColor(Color.parseColor("#4B8A08"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.circles0))
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.MAGENTA, 500, 1000)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0
                // ID of notification
                , notificationBuilder.build());
    }
}