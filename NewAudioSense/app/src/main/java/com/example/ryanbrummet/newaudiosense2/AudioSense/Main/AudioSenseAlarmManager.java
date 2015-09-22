package com.example.ryanbrummet.newaudiosense2.AudioSense.Main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by ryanbrummet on 9/7/15.
 */
public class AudioSenseAlarmManager extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Log.w("AudioSenseAlarmManager", "Broadcast Received");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            ((AudioSurveySampleService) context).stopAlarm(true, true);
        }
    }
}
