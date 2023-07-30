package com.example.remindme;

import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
import android.os.Bundle;


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
