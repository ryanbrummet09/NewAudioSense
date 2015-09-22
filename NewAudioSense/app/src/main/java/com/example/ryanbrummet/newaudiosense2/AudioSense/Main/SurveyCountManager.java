package com.example.ryanbrummet.newaudiosense2.AudioSense.Main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by ryanbrummet on 9/11/15.
 */
public class SurveyCountManager extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        AlarmAlertWakeLock.acquireCpuWakeLock(context);

        Log.i("SurveyCountManager","Broadcast Received");

        SharedPreferences preferences = context.getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);
        preferences.edit().putInt("dailyTakenSurveys",0).commit();
        preferences.edit().putInt("dailyGivenSurveys",0).commit();
        preferences.edit().putBoolean("vibrationOnly",false).commit();
        AudioSenseActivity.initDailySurveyTrack(context);
    }
}
