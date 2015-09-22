package com.example.ryanbrummet.newaudiosense2.AudioSense.Settings;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.ryanbrummet.newaudiosense2.AudioSense.Main.AudioSenseActivity;
import com.example.ryanbrummet.newaudiosense2.AudioSense.Main.AudioSenseConstants;
import com.example.ryanbrummet.newaudiosense2.AudioSense.Main.EndStudyManager;

/**
 * Created by ryanbrummet on 8/24/15.
 */
public class AudioSenseSettingsContent {

    private AudioSenseActivity activity;
    private SharedPreferences preferences;

    public AudioSenseSettingsContent(AudioSenseActivity activity) {
        this.activity = activity;
        this.preferences = activity.getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);
    }

    public void init() {

        Log.i("AudioSenseSettingsContent","Initializing SharedPreferences");
        String[] persistentNames = AudioSenseConstants.persistentNames;
        int[] persistentValues = AudioSenseConstants.persistentValues;

        for(int i = 0; i < persistentNames.length; i++) {
            if(!preferences.contains(persistentNames[i])){
                preferences.edit().putInt(persistentNames[i], persistentValues[i]).commit();
            }
        }

        preferences.edit().putBoolean("vibrationOnly",AudioSenseConstants.vibrationOnly).commit();
        preferences.edit().putBoolean("uiRunning", AudioSenseConstants.uiRunning).commit();
        preferences.edit().putInt("dailyTakenSurveys",0).commit();
        preferences.edit().putInt("dailyGivenSurveys",0).commit();
        preferences.edit().putInt("totalTakenSurveys",0).commit();
        preferences.edit().putInt("totalGivenSurveys", 0).commit();
    }

    public void stop() {

        EndStudyManager.endStudy(activity,true);
    }

    public void start(boolean testing) {
        Log.i("AudioSenseSettingsContent","Starting study, testing = " + Boolean.toString(testing));
        String[] persistentNames = AudioSenseConstants.persistentNames;
        for(int i = 0; i < persistentNames.length; i++) {
            Log.w("Settings", persistentNames[i] + ": " + Integer.toString(preferences.getInt(persistentNames[i],-1)));
        }
        activity.onStartStudy(testing);
    }
}
