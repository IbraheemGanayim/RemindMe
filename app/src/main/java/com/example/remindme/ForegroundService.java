package com.example.remindme;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    NotificationManager mNotiMgr;
    Notification.Builder mNotifyBuilder;
    Runnable test;
    MediaPlayer mediaPlayer;
    NotificationManagerCompat notificationManagerCompat;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        initForeground();
        mediaPlayer = MediaPlayer.create(this, R.raw.vibrate);
        mediaPlayer.setLooping(false);
    }

    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground(1, updateNotification("No Reminders"));

        final Handler handler = new Handler();
        test = () -> {
            Calendar rightNow = Calendar.getInstance();
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
            String CurrDate = sdf2.format(rightNow.getTime());
            int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
            int currentMins = rightNow.get(Calendar.MINUTE);
            Log.i("Current date                                                                    :", CurrDate);

            AppDataBase abc = AppDataBase.getInstance(getApplicationContext());
            List<alarmDetails> list = abc.noteDao().getAllDeatils();
            int j = -1;
            for (alarmDetails alarm : list) {
                j = j + 1;
                String DateRemind = alarm.create;
                DateRemind = DateRemind.substring(0, DateRemind.length() - 6);
                Log.i("date                                                                    :", DateRemind);

                if (alarm.hour == currentHourIn24Format && alarm.minute == currentMins && (DateRemind.equals(CurrDate))) {
                    mediaPlayer.start();
                    String det = "Date :" + DateRemind + " Time :" + currentHourIn24Format + ":" + currentMins + " " + " Reminder Note : " + alarm.alarmNote;
                    updateNotification(det);

                    AppDataBase abc1 = AppDataBase.getInstance(getApplicationContext());
                    abc1.noteDao().delete(alarm.hour, alarm.minute);
                    MyAdapter.listener.alarms.remove(j);
                    MyAdapter.listener.notifyDataSetChanged();

                } else {
                    mNotiMgr.cancelAll();
                }
            }

            handler.postDelayed(test, 1000);
        };
        handler.postDelayed(test, 0);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initForeground() {
        String CHANNEL_ID = "CHANNEL_SAMPLE";
        if (mNotiMgr == null)
            mNotiMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "My Notification",
                NotificationManager.IMPORTANCE_DEFAULT);
        mNotiMgr.createNotificationChannel(channel);


        mNotifyBuilder = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("There is no reminders 2")
                .setSmallIcon(R.drawable.ic_baseline_alarm_add_24)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX);
    }


    public Notification updateNotification(String details) {

        mNotifyBuilder.setContentTitle(details)
                .setSmallIcon(R.drawable.ic_baseline_alarm_add_24)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX);

        Notification notification = mNotifyBuilder.build();
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        mNotiMgr.notify(0, notification);
        return notification;
    }


}

