package com.example.ryanbrummet.newaudiosense2.AudioSense.Main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by ryanbrummet on 9/29/15.
 */
public class SurveyFailureManager extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        AlarmAlertWakeLock.acquireCpuWakeLock(context.getApplicationContext());
        Log.i("SurveyFailureManager","Broadcast Received");
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);
        if(preferences.getLong("nextAudioSurveySample",-1) <= TimingManager.getCurrentUnixTimeStamp()) {
            Log.e("SurveyFailureManager", "OS killed app, Restarting");
            TimingManager.rescheduleAudioSurveySample(context.getApplicationContext(), false,false, true);
        }


        AlarmAlertWakeLock.releaseCpuLock();
    }
}
