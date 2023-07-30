package com.example.remindme;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Button;

public class myBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager am = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);
        if(am.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("WARNING")
                    .setMessage("Phone is on silent mode")
                    .setPositiveButton("Ok",null)
                    .show();
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);



        }


    }
}
