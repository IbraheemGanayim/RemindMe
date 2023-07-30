package com.example.remindme;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public ArrayList<Alarm> alarms;
    private static Context context;
    public static MyAdapter listener;
    private String myText;

    public MyAdapter(ArrayList<Alarm> alarms, Context context) {
        this.context = context;
        this.alarms = alarms;
        listener = this;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        Alarm item = alarms.get(position);
        holder.bindData(item, this);
    }
    @Override
    public int getItemCount() {
        return alarms.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView hour;
        TextView min;
        TextView note;
        TextView date;
        FrameLayout view1;
        RelativeLayout view2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.textView);
            min = itemView.findViewById(R.id.textView2);
            note = itemView.findViewById(R.id.notesInItemView);
            date = itemView.findViewById(R.id.date);

            this.view1 = (FrameLayout) itemView;
            this.view2 = (RelativeLayout) itemView.findViewById(R.id.msg_container);
        }

        public void bindData(final Alarm alarm, MyAdapter myAdapter) {
            Boolean b = sharedPreference.loadCheck();
            if (b) {
                view2.setBackgroundColor(Color.DKGRAY);
                hour.setTextColor(Color.WHITE);
                min.setTextColor(Color.WHITE);
                note.setTextColor(Color.WHITE);
            } else {
                view2.setBackgroundColor(Color.WHITE);
                hour.setTextColor(Color.BLACK);
                min.setTextColor(Color.BLACK);
                note.setTextColor(Color.BLACK);
            }

            hour.setText(alarm.getHour() + "");
            min.setText(": " + alarm.getMins() + "");
            note.setText(alarm.getNotes());
            String NewDate = alarm.create;
            NewDate = NewDate.substring(0, NewDate.length() - 5);
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
                            app.noteDao().delete(alarm.getHour(), alarm.getMins());
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
                    android.app.AlertDialog.Builder myDialog = new android.app.AlertDialog.Builder(MyAdapter.context);
                    myDialog.setTitle("Edit notes");
                    final EditText input = new EditText(MyAdapter.context);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    myDialog.setView(input);
                    myDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myText = input.getText().toString();
                            AppDataBase app = AppDataBase.getInstance(myAdapter.context);
                            List<alarmDetails> list = app.noteDao().find(alarm.getHour(), alarm.getMins());
                            if (!list.isEmpty()) {
                                app.noteDao().insert(new alarmDetails(alarm.getHour(), alarm.getMins(), myText, alarm.getCreate()));
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
