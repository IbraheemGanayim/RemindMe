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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    NotificationManager mNotiMgr;
    Notification.Builder mNotifyBuilder;
    Runnable test;
    MediaPlayer mediaPlayer;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        initForeground();
        mediaPlayer = MediaPlayer.create(this,R.raw.vibrate);

        mediaPlayer.setLooping(false);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        //stopSelf();
        startForeground(1, updateNotification("No Reminders"));

        final Handler handler = new Handler();
        test = new Runnable() {

            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                Calendar rightNow = Calendar.getInstance();
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
                String CurrDate = sdf2.format(rightNow.getTime());
                int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
                int currentMins = rightNow.get(Calendar.MINUTE);
                Log.i("Current date                                                                    :",CurrDate);

                AppDataBase abc = AppDataBase.getInstance(getApplicationContext());
                List<alarmDetails> list = abc.noteDao().getAllDetails();

                for(alarmDetails alarm : list){
                    String DateRemind=alarm.create;
                    DateRemind=  DateRemind.substring(0, DateRemind.length() - 6);
                    Log.i("date                                                                    :",DateRemind);

                    if(alarm.hour == currentHourIn24Format && alarm.minute == currentMins && (DateRemind.equals(CurrDate))){
                        mediaPlayer.start();
                        String det="Date :"+DateRemind+" Time :"+currentHourIn24Format+":"+currentMins+" "+" Reminder Note : "+alarm.alarmNote;
                        updateNotification(det);
                    }else{
                        mNotiMgr.cancelAll();
                    }
                }

                //check if there is alarm to delete by create time
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String strDate = sdf.format(c.getTime());

                for(int i =0; i < MyAdapter.listner.alarms.size(); i++) 
                {
                    //get time creation of alarm
                    String createalarm = MyAdapter.listner.alarms.get(i).getCreate();
                    int dd = Integer.parseInt(createalarm.substring(0,2));
                    int mm = Integer.parseInt(createalarm.substring(3,5));
                    int yyyy = Integer.parseInt(createalarm.substring(6,10));
                    int hh = Integer.parseInt(createalarm.substring(11,13));
                    int mmin = Integer.parseInt(createalarm.substring(14,16));

//                    Log.i("Time",String.valueOf(dd));
  //                  Log.i("Time",String.valueOf(mm));
    //                Log.i("Time",String.valueOf(yyyy));
      //              Log.i("Time",String.valueOf(hh));
        //            Log.i("Time",String.valueOf(mmin));

                    //get time now
                    int dd2 = Integer.parseInt(strDate.substring(0,2));
                    int mm2 = Integer.parseInt(strDate.substring(3,5));
                    int yyyy2 = Integer.parseInt(strDate.substring(6,10));
                    int hh2 = Integer.parseInt(strDate.substring(11,13));
                    int mmin2 = Integer.parseInt(strDate.substring(14,16));
                    Log.i("Time",String.valueOf(dd2));
                    Log.i("Time",String.valueOf(mm2));
                    Log.i("Time",String.valueOf(yyyy2));
                    Log.i("Time",String.valueOf(hh2));
                    Log.i("Time",String.valueOf(mmin2));

                    if((hh == hh2) && (mmin  == mmin2) && (dd==dd2) && (mm == mm2) && (yyyy == yyyy2) )
                    {
                        AppDataBase abc1 = AppDataBase.getInstance(getApplicationContext());
                        abc1.noteDao().delete(MyAdapter.listner.alarms.get(i).getHour(),MyAdapter.listner.alarms.get(i).getMins());
                        MyAdapter.listner.alarms.remove(i);
                        MyAdapter.listner.notifyDataSetChanged();
                    }
                }

                handler.postDelayed(test,1000);
            }
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
    private void initForeground(){
        String CHANNEL_ID = "CHANNEL_SAMPLE";
        if (mNotiMgr==null)
            mNotiMgr= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "My Notification",
                NotificationManager.IMPORTANCE_HIGH);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .createNotificationChannel(channel);

        mNotifyBuilder = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("There is no reminders 2")
                .setSmallIcon(R.drawable.ic_baseline_alarm_add_24)
                .setAutoCancel(true)
        .setPriority(Notification.PRIORITY_MAX);



    }
    public Notification updateNotification(String details){
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

