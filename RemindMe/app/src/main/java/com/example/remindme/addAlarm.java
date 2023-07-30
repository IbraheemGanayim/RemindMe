package com.example.remindme;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Calendar;
import java.util.Date;

public class addAlarm extends AppCompatActivity {
    EditText hourEditText, minEditText,noteEditText;
    Button setAlarmButton;
    RelativeLayout view;
    TextView date;
    CalendarView calendar  ;
    String selectedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_alarm);
        hourEditText = (EditText) findViewById(R.id.hour);
        minEditText = (EditText) findViewById(R.id.minute);
        noteEditText =(EditText) findViewById(R.id.Reminder);
        setAlarmButton = (Button) findViewById(R.id.ReminderSet);
        view = (RelativeLayout) findViewById(R.id.add_alarmcontainer);
        date = (TextView) findViewById(R.id.date);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String strDate = sdf.format(c.getTime());
        date.setText(strDate);
        selectedDates=strDate;
        calendar = (CalendarView) findViewById(R.id.c2);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                 selectedDates = sdf.format(new Date(year-1900,month,dayOfMonth));
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String sDate = sdf3.format(calendar.getTime());
                String input = sDate;     //input string
                String lastFourDigits = "";     //substring containing last 4 characters
                if (input.length() > 5)
                {
                    lastFourDigits = input.substring(input.length() - 5);
                }
                else
                {
                    lastFourDigits = input;
                }
                selectedDates=selectedDates+" "+hourEditText.getText()+":"+minEditText.getText();
                Log.i("aaaaaahgfjkajafajfajaafjajfaaaaaaaaaaaaaaaaaaaaaaa",selectedDates);
                date.setText(selectedDates);
            }
        });
        //check color

        Boolean b = sharedPreference.loadCheck();
        if(b){
            view.setBackgroundColor(Color.DKGRAY);
            hourEditText.setTextColor(Color.WHITE);
            hourEditText.setHintTextColor(Color.WHITE);
            minEditText.setTextColor(Color.WHITE);
            minEditText.setHintTextColor(Color.WHITE);
            noteEditText.setTextColor(Color.WHITE);
            noteEditText.setHintTextColor(Color.WHITE);
            setAlarmButton.setTextColor(Color.WHITE);
        }else{
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
        setAlarmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int hour = Integer.parseInt(hourEditText.getText().toString());
                int minute = Integer.parseInt(minEditText.getText().toString());
                String note = (noteEditText.getText().toString());
                if(hourEditText.getText().toString().isEmpty() && minEditText.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Error with the time", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(hour>23 || hour<0 || minute > 59 || minute < 0)
                {Toast.makeText(getApplicationContext(), "Error with the time", Toast.LENGTH_SHORT).show();
                    return;
                }


                //current time
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String strDate = sdf.format(c.getTime());
                date.setText(strDate);

                //add to adapter
                MyAdapter.listner.alarms.add(new Alarm(hour,minute,note,selectedDates));
                MyAdapter.listner.notifyDataSetChanged();


                //Adding to SQL
                AppDataBase abc = AppDataBase.getInstance(getApplicationContext());
                alarmDetails notesAlarm = new alarmDetails(hour,minute, note, selectedDates);
                abc.noteDao().insert(notesAlarm);

                finish();

            }
        });

    }


}