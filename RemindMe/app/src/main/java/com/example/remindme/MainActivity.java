package com.example.remindme;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextClock;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private EditText textView4;//
    FrameLayout frame1;

    TextClock clock;
    public ArrayList<Alarm> alarms = new ArrayList<Alarm>();
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myBroadCastReceiver myReceiver = new myBroadCastReceiver();
        IntentFilter f = new IntentFilter();
        f.addAction("android.media.RINGER_MODE_CHANGED");
        registerReceiver(myReceiver,f);


        frame1 = (FrameLayout)findViewById(R.id.container_frame);
        clock = (TextClock)findViewById(R.id.liveClock1);

        //---------------------------------SP-------------------------------------------------
        sharedPreference sp = new sharedPreference(this);

        //--------------------------------service foreground---------------------------------
        if(!checkServiceRunning(ForegroundService.class)){
            Intent serviceIntent = new Intent(this,ForegroundService.class);
            ContextCompat.startForegroundService(this,serviceIntent);
        }
        ///////////////////////////////////////////////////////////////////////////////////////



        //----------------------------open ADD Alarm -----------------------------------------
        FloatingActionButton fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(this);


        recyclerView = (RecyclerView)findViewById(R.id.recycleView);
        myAdapter = new MyAdapter(alarms,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

        //load saved alarms from SQL and add them to list
        AppDataBase abc = AppDataBase.getInstance(getApplicationContext());
        List<alarmDetails> list = abc.noteDao().getAllDetails();
        for(alarmDetails alarm : list){
            MyAdapter.listner.alarms.add(new Alarm(alarm.hour,alarm.minute,alarm.alarmNote,alarm.create));
            MyAdapter.listner.notifyDataSetChanged();
        }


        //check the SP to see the theme of the app (white or black)
        change_theme(sharedPreference.loadCheck());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case  R.id.prefbutton:
                getSupportFragmentManager().beginTransaction().addToBackStack("b")
                        .replace(R.id.container_all,new PrefFragment()).commit();

                return true;
            case R.id.item2:

                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("Credit")
                            .setMessage("Made by Mohamed Abo Hamad & Mahmood Odeh")
                            .setPositiveButton("Ok",null)
                            .show();

                return  true;
            case R.id.exitMenu:
                System.exit(1);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option,menu);
        return  true;
    }

    private boolean checkServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClass.getName().equals(service.service.getClassName()))
                return true;
        }
        return false;
    }

    public void change_theme(boolean b){

        if(b){
            frame1.setBackgroundColor(Color.DKGRAY);
            clock.setTextColor(Color.WHITE);
        }else{
            frame1.setBackgroundColor(Color.WHITE);
            clock.setTextColor(Color.BLACK);
        }
        if(MyAdapter.listner != null){
            MyAdapter.listner.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,addAlarm.class);

        startActivity(intent);
    }
}
