package com.example.remindme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class addAlarm extends AppCompatActivity {

    EditText hourEditText, minEditText, noteEditText;
    Button setAlarmButton;
    TextView date;
    CalendarView calendar;
    String selectedDates;
    ConstraintLayout view;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);


//-----------------Find View by id -------------------
        hourEditText = (EditText) findViewById(R.id.hour);
        minEditText = (EditText) findViewById(R.id.minute);
        noteEditText = (EditText) findViewById(R.id.Reminder);
        setAlarmButton = (Button) findViewById(R.id.ReminderSet);
        date = (TextView) findViewById(R.id.date);
        view = (ConstraintLayout) findViewById(R.id.add_alarmcontainer);


        // print the date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String strDate = sdf.format(c.getTime());
        date.setText(strDate);


        selectedDates = strDate;
        calendar = (CalendarView) findViewById(R.id.calendar1);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                selectedDates = sdf.format(new Date(year - 1900, month, dayOfMonth));
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String sDate = sdf3.format(calendar.getTime());
                String input = sDate;     //input string
                String lastFourDigits = "";     //substring containing last 4 characters
                if (input.length() > 5) {
                    lastFourDigits = input.substring(input.length() - 6);
                } else {
                    lastFourDigits = input;
                }
                selectedDates = selectedDates + " " + hourEditText.getText() + ":" + minEditText.getText();
                Log.i("aaaaaahgfjkajafajfajaafjajfaaaaaaaaaaaaaaaaaaaaaaa", selectedDates);
                date.setText(selectedDates);

            }
        });


        //check color
        Boolean b = sharedPreference.loadCheck();
        if (b) {
            view.setBackgroundColor(Color.DKGRAY);
            hourEditText.setTextColor(Color.WHITE);
            hourEditText.setHintTextColor(Color.WHITE);
            minEditText.setTextColor(Color.WHITE);
            minEditText.setHintTextColor(Color.WHITE);
            noteEditText.setTextColor(Color.WHITE);
            noteEditText.setHintTextColor(Color.WHITE);
            setAlarmButton.setTextColor(Color.WHITE);
        } else {
            view.setBackgroundColor(Color.WHITE);
            hourEditText.setTextColor(Color.BLACK);
            hourEditText.setHintTextColor(Color.BLACK);
            minEditText.setTextColor(Color.BLACK);
            minEditText.setHintTextColor(Color.BLACK);
            noteEditText.setTextColor(Color.BLACK);
            noteEditText.setHintTextColor(Color.BLACK);
            setAlarmButton.setTextColor(Color.BLACK);
        }


        //add alarm button
        setAlarmButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter a reminder!", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (hourEditText.getText().toString().isEmpty() && minEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter a time!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int hour = Integer.parseInt(hourEditText.getText().toString());
                int minute = Integer.parseInt(minEditText.getText().toString());
                String note = (noteEditText.getText().toString());
                if (hour > 23 || hour < 0 || minute > 59 || minute < 0) {
                    Toast.makeText(getApplicationContext(), "Error with the time", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hourEditText.getText().toString().length() < 2 || minEditText.getText().toString().length() < 2) {
                    Toast.makeText(getApplicationContext(), "please enter the hour or minute with 2 numbers", Toast.LENGTH_SHORT).show();
                    return;
                }

                //current time
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String strDate = sdf.format(c.getTime());
                date.setText(strDate);


                MyAdapter.listener.alarms.add(new Alarm(hour, minute, note, selectedDates));
                MyAdapter.listener.notifyDataSetChanged();


                //Adding to SQL
                AppDataBase abc = AppDataBase.getInstance(getApplicationContext());
                alarmDetails notesAlarm = new alarmDetails(hour, minute, note, selectedDates);
                abc.noteDao().insert(notesAlarm);

                finish();
            }
        }));

    }
}