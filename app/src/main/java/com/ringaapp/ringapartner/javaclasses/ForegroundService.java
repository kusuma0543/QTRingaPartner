package com.ringaapp.ringapartner.javaclasses;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.ringaapp.ringapartner.R;
import com.ringaapp.ringapartner.activities.MainActivity;

/**
 * Created by andriod on 7/2/18.
 */

public class ForegroundService extends Service {
    private static final String LOG_TAG = "ForegroundService";
    public static boolean IS_SERVICE_RUNNING = false;
    final Handler handler=new Handler();
    private final int  FIVE_SECONDS=5000;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scheduleSendLocation();
        return START_STICKY;
    }
    public void scheduleSendLocation() {


        handler.postDelayed(new Runnable() {
            public void run() {
                showNotification();

                Toast.makeText(getApplicationContext(), "5", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, FIVE_SECONDS);
            }
        }, FIVE_SECONDS);
    }
    private void showNotification() {

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        long[] v = {500,1000};

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("RingaApp User Request!")
                // .setSound(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.sweety))
                .setSound(uri)
                .setColor(Color.RED)
                .setContentIntent(pendingIntent)
                .setContentText("You got a service request from "+"\nPlease check RingaApp Partner Dashboard"
                );

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();


        builder.setStyle(bigText);
        builder.setVibrate(v);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(0, notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
        Toast.makeText(this, "Service Detroyed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case if services are bound (Bound Services).
        return null;
    }
}