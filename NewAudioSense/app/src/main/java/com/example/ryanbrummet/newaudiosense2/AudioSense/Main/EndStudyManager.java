package com.example.ryanbrummet.newaudiosense2.AudioSense.Main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.FileOutputStream;

/**
 * Created by ryanbrummet on 9/8/15.
 */
public class EndStudyManager extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        AlarmAlertWakeLock.acquireCpuWakeLock(context);
        Log.i("EndStudyManager", "Broadcast Received");
        endStudy(context,false);
        AlarmAlertWakeLock.releaseCpuLock();

    }

    public static void endStudy(Context context, boolean stopServices) {
        Intent alarmIntentStart = new Intent(context, TimingManager.class);
        alarmIntentStart.setAction("AudioSenseStartStudy");
        PendingIntent pendingIntentStart = PendingIntent.getBroadcast(context, 0, alarmIntentStart, 0);

        Intent alarmIntentEnd = new Intent(context, EndStudyManager.class);
        alarmIntentEnd.setAction("AudioSenseEndStudy");
        PendingIntent pendingIntentEnd = PendingIntent.getBroadcast(context, 0, alarmIntentEnd,0);

        Intent alarmIntentAudioService = new Intent(context, TimingManager.class);
        alarmIntentAudioService.setAction("AudioSenseAudioSample");
        PendingIntent pendingIntentAudioService = PendingIntent.getBroadcast(context, 0, alarmIntentAudioService, 0);

        Intent alarmIntentAudioSurveyService = new Intent(context, TimingManager.class);
        alarmIntentAudioSurveyService.setAction("AudioSenseAudioSurveySample");
        PendingIntent pendingIntentAudioSurveyService = PendingIntent.getBroadcast(context, 0, alarmIntentAudioSurveyService, 0);

        Intent alarmIntentSurveyCounter = new Intent(context, SurveyCountManager.class);
        alarmIntentSurveyCounter.setAction("Update_Daily_Survey_Count");
        PendingIntent pendingIntentSurveyCounter = PendingIntent.getBroadcast(context, 0, alarmIntentSurveyCounter, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntentStart);
        alarmManager.cancel(pendingIntentEnd);
        alarmManager.cancel(pendingIntentAudioService);
        alarmManager.cancel(pendingIntentAudioSurveyService);
        alarmManager.cancel(pendingIntentSurveyCounter);

        if(stopServices) {
            context.stopService(new Intent(context, AudioSampleService.class));
            context.stopService(new Intent(context, AudioSurveySampleService.class));
        }

        SharedPreferences preferences = context.getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);

        int patientID = preferences.getInt("patientID", -1);
        int conditionID = preferences.getInt("conditionID", -1);
        int takenSurveys = preferences.getInt("totalTakenSurveys",-1);
        int givenSurveys = preferences.getInt("totalGivenSurveys",-1);

        Log.e("takenSurveys",Integer.toString(takenSurveys));
        Log.e("givenSurveys",Integer.toString(givenSurveys));

        FileOutputStream os = null;
        try {
            os = new FileOutputStream("/sdcard/SURVEYCount_PID" + Integer.toString(patientID) + "_CID" + Integer.toString(conditionID) + ".count");
            os.write(("taken: " + Integer.toString(takenSurveys) + "\n").getBytes());
            os.write(("given: " + Integer.toString(givenSurveys) + "\n").getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("AudioSenseSettingsContent","Stopping study");
        preferences.edit().clear().commit();
    }
}
