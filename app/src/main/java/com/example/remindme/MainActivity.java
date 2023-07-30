package com.example.remindme;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextClock;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerview;
    static MyAdapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    FrameLayout frame1;
    public ArrayList<Alarm> alarms = new ArrayList<Alarm>();
    TextClock clock;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frame1 = (FrameLayout) findViewById(R.id.container_frame);
        clock = (TextClock) findViewById(R.id.liveClock1);
        recyclerview = (RecyclerView) findViewById(R.id.recycleView);

        //warning if the phone is on silent mode
        myBroadCastReceiver myReceiver = new myBroadCastReceiver();
        IntentFilter f = new IntentFilter();
        f.addAction("android.media.RINGER_MODE_CHANGED");
        registerReceiver(myReceiver, f);

        //---------------------------------SP-------------------------------------------------
        sharedPreference sp = new sharedPreference(this);
        //-------------------------------service foreground---------------------------------

        if (!checkServiceRunning(ForegroundService.class)) {
            Intent serviceIntent = new Intent(this, ForegroundService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }
        //----------------------------open ADD Alarm -----------------------------------------
        FloatingActionButton fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(this);

        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        myAdapter = new MyAdapter(alarms, this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(myAdapter);

        //load saved alarms from SQL and add them to list
        AppDataBase adb = AppDataBase.getInstance((getApplicationContext()));
        List<alarmDetails> list = adb.noteDao().getAllDeatils();
        for (alarmDetails alarm : list) {
            MyAdapter.listener.alarms.add(new Alarm(alarm.hour, alarm.minute, alarm.alarmNote, alarm.create));
            MyAdapter.listener.notifyDataSetChanged();
        }
        change_theme(sharedPreference.loadCheck());
    }

    //decides where to go when clicking a button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.prefbutton:
                getSupportFragmentManager().beginTransaction().addToBackStack("b")
                        .replace(R.id.container_all, new PrefFragment()).commit();
                return true;

            case R.id.item2:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Credit")
                        .setMessage("Made by:" + "\n" + "Ebrahem Enbtawe" + "\n" + " Ibraheem Ganayim ")
                        .setPositiveButton("ok", null)
                        .show();
                return true;

            case R.id.exitMenu:
                System.exit(1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //inflate options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }
    private boolean checkServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName()))
                return true;
        }
        return false;
    }

    public void change_theme(boolean b) {

        if (b) {
            frame1.setBackgroundColor(Color.DKGRAY);
            clock.setTextColor(Color.WHITE);
        } else {
            frame1.setBackgroundColor(Color.WHITE);
            clock.setTextColor(Color.BLACK);
        }
        if (MyAdapter.listener != null) {
            MyAdapter.listener.notifyDataSetChanged();
        }
    }

    // Clicking + button
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, addAlarm.class);
        startActivity(intent);
    }
}