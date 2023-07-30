package com.example.remindme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

public class myBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        if (am.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("WARNING")
                    .setMessage("phone is on silent mode")
                    .setPositiveButton("ok", null)
                    .show();
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        }
    }
}
