package com.ringaapp.ringapartner.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ringaapp.ringapartner.R;
import com.ringaapp.ringapartner.activities.CategoryMain;

import java.util.Calendar;

/**
 * Created by andriod on 28/1/18.
 */

public class FireBaseServiceClass extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {



        Intent intent =new Intent(this,CategoryMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notifica = new NotificationCompat.Builder(this);
        notifica.setContentTitle("Ringaapp Partner");
        notifica.setColor(getColor(R.color.colorAccent));
        notifica.setContentText(remoteMessage.getNotification().getBody());
        notifica.setAutoCancel(true);
        notifica.setColor(getColor(R.color.colorAccent));
        notifica.setOnlyAlertOnce(true);
        Uri alarmSound =RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 6 && timeOfDay < 18){
            notifica.setSound(uri);
        }else if(timeOfDay >= 0 && timeOfDay < 6){
            notifica.setSound(alarmSound);
        }else if(timeOfDay >= 18 && timeOfDay < 24){
            notifica.setSound(alarmSound);
        }


       // notifica.setSound(uri);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notifica.setSmallIcon(R.mipmap.ic_launcher);
        } else {
            notifica.setSmallIcon(R.mipmap.ic_launcher);
        }
        notifica.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notifica.build());
    }


}