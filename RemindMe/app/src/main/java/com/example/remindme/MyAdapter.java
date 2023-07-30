package com.example.remindme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Viewholder>{
    public ArrayList<Alarm> alarms;
    private static Context context;
    public static MyAdapter listner;
    private String myText;

    public MyAdapter(ArrayList<Alarm> alarms, Context context )
    {
        this.alarms = alarms;
        this.context = context;
        listner = this;
    }

    @NonNull
    @Override
    public MyAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vew,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Alarm item = alarms.get(position);
        holder.bindData(item,this);
    }


    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        private TextView hour;
        private TextView min;
        private TextView note;
        private TextView date;
        FrameLayout view1;
        RelativeLayout view2;


        public Viewholder(View view)
        {
            super(view);
            hour = view.findViewById(R.id.textView);
            min = view.findViewById(R.id.textView2);
            note = view.findViewById(R.id.notesInItemView);
            date = view.findViewById(R.id.date);

            this.view1 = (FrameLayout)view;
            this.view2 = (RelativeLayout)view.findViewById(R.id.msg_container);

        }
        public void bindData(final Alarm alarm, final MyAdapter myAdapter)
        {

            Boolean b = sharedPreference.loadCheck();
            if(b){
                view2.setBackgroundColor(Color.DKGRAY);
                hour.setTextColor(Color.WHITE);
                min.setTextColor(Color.WHITE);
                note.setTextColor(Color.WHITE);
            }else{
                view2.setBackgroundColor(Color.WHITE);
                hour.setTextColor(Color.BLACK);
                min.setTextColor(Color.BLACK);
                note.setTextColor(Color.BLACK);
            }

            hour.setText(alarm.getHour() + "");
            min.setText(": " +alarm.getMins() + "");
            note.setText(alarm.getNotes());
           String NewDate= alarm.create;
            NewDate=  NewDate.substring(0, NewDate.length() - 6);
            date.setText(NewDate);
            view1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder myDialog = new AlertDialog.Builder(MyAdapter.context);
                    myDialog.setTitle(alarm.getCreate());
                    myDialog.setMessage("Are you sure to delete this alarm?");
                    myDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            AppDataBase app = AppDataBase.getInstance(myAdapter.context);
                            app.noteDao().delete(alarm.getHour(),alarm.getMins());
                            alarms.remove(getPosition());
                            notifyDataSetChanged();

                        }

                    });
                    myDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    myDialog.show();


                    return false;
                }
            });
            view1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                        AlertDialog.Builder myDialog = new AlertDialog.Builder(MyAdapter.context);
                        myDialog.setTitle("Edit notes");
                        final EditText input = new EditText(MyAdapter.context);
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        myDialog.setView(input);
                        myDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myText =input.getText().toString();
                                AppDataBase app = AppDataBase.getInstance(myAdapter.context);
                              List<alarmDetails> list =  app.noteDao().find(alarm.getHour(),alarm.getMins());
                              if(!list.isEmpty())
                              {
                                  app.noteDao().insert(new alarmDetails(alarm.getHour(),alarm.getMins(),myText,alarm.getCreate()));
                                  alarm.setNotes(myText);
                                  notifyDataSetChanged();
                              }

                            }

                        });
                        myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        myDialog.show();




                }


            });



        }

    }
}
