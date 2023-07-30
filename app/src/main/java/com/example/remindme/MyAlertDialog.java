package com.example.remindme;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;


public class MyAlertDialog extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Message")
                .show();
    }
}
