package com.example.ryanbrummet.newaudiosense2.AudioSense.Main;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.view.WindowManager;
import android.widget.Toast;
import android.util.Log;

import com.example.ryanbrummet.newaudiosense2.AudioSense.Settings.AudioSenseSettingsUI;
import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content.AudioSense2;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.RouteComponents;
import com.example.ryanbrummet.newaudiosense2.R;

/**
 * Created by ryanbrummet on 8/14/15.
 */
public class AudioSenseActivity extends Activity {

    private AudioSense2 survey;
    private LinearLayout rootView;
    protected RouteComponents routeComponents;
    private Timer timer;
    private boolean unlockSurveyScreen;
    PowerManager.WakeLock screenOnWakeLock;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.intent = getIntent();

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        screenOnWakeLock =  powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,"ScreenlockTag");
        screenOnWakeLock.acquire();
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.i("AudioSenseActivity", "OnCreate Called");

        final SharedPreferences preferences = getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);
        preferences.edit().putBoolean("uiRunning",true);

        if(preferences.getBoolean("unlockSurveyScreen",false)) {
            preferences.edit().putBoolean("unlockSurveyScreen",false).commit();
            unlockSurveyScreen = true;
        } else {
            unlockSurveyScreen = false;
        }

        long appTimeout = 0;

        if(unlockSurveyScreen) {
            appTimeout = AudioSenseConstants.defaultUserSurveyTimeoutInMin * 60000;
        } else {
            if(preferences.getBoolean("userLock",false)) {
                Toast.makeText(this, "You cannot open the app at this time.  A survey is currently being delivered",Toast.LENGTH_LONG).show();
                finish();
            } else {
                long currentTime = TimingManager.getCurrentUnixTimeStamp();
                long timeRemaining = preferences.getLong("nextAudioSurveySample",currentTime) - currentTime - 1;

                if(timeRemaining <= AudioSenseConstants.defaultUserSurveyTimeoutInMin * 30000 && timeRemaining > 0) {
                    Toast.makeText(this, "You cannot open the app at this time.  A survey is currently being delivered",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    if(timeRemaining > 0) {
                        appTimeout = timeRemaining;
                    } else {
                        appTimeout = AudioSenseConstants.defaultUserSurveyTimeoutInMin * 60000;
                    }

                }

            }
        }

        timer = new Timer();

        // forces the app to close after a period of time (surveys taking longer than AudioSenseConstants.defaultSurveyTimeoutInMin are discarded)
        // this is done for two reasons.  first, to avoid the user leaving the app open and forgetting about it.
        // two, surveys that take a abnormal amount of time indicate subject non compliance.  This will catch this and prevent
        // a person from being credited with taking a survey (and not taking it seriously).  The timeout is made to be fairly
        // long (the initial default is set to 10 minutes in the AudioSenseConstants class)

        /*
        if(startedByService) {
            setContentView(R.layout.homepageappstarted);
        } else {
            setContentView(R.layout.homepageuserstarted);
        }*/

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                (new Handler(Looper.getMainLooper())).post(new Runnable() {

                    @Override
                    public void run() {
                        Log.w("AudioSenseActivity","User Timeout");
                        if(unlockSurveyScreen) {
                            preferences.edit().putInt("dailyGivenSurveys", preferences.getInt("dailyGivenSurveys", 0) + 1).commit();
                            preferences.edit().putInt("totalGivenSurveys", preferences.getInt("totalGivenSurveys", 0) + 1).commit();
                        }
                        finish();
                    }
                });
            }
        }, appTimeout);

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i("AudioSenseActivity", "OnStart Called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("AudioSenseActivity", "OnResumed Called");


        boolean startedByService = intent.getBooleanExtra("AUDIOSENSE_STARTED_BY_SERVICE",false);

        if(startedByService && unlockSurveyScreen) {
            setContentView(R.layout.homepageappstarted);
        } else {
            setContentView(R.layout.homepageuserstarted);
        }
    }

    @Override
    protected void onRestart(){

        Log.i("AudioSenseActivity", "OnRestart Called");
        try {
            screenOnWakeLock.acquire();
        } catch(RuntimeException e) {}
        super.onRestart();
    }

    @Override
    protected void onPause() {

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(rootView.getWindowToken(), 0, 0);
            ViewGroup layout = (ViewGroup) (findViewById(R.id.passwordLayout));
            imm.toggleSoftInputFromWindow(layout.getWindowToken(), 0, 0);
            layout = (ViewGroup) (findViewById(R.id.settingsRoot));
            imm.toggleSoftInputFromWindow(layout.getWindowToken(), 0, 0);
            layout = (ViewGroup) (findViewById(R.id.confirmScreen));
            imm.toggleSoftInputFromWindow(layout.getWindowToken(), 0, 0);

        } catch (RuntimeException e) {}
        Log.i("AudioSenseActivity", "OnPause Called");
        try {
            screenOnWakeLock.release();
        } catch(RuntimeException e) {}
        super.onPause();
    }

    @Override
    protected void onStop() {

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.i("AudioSenseActivity", "OnStop Called");
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        SharedPreferences preferences = getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);
        preferences.edit().putBoolean("uiRunning",false).commit();
        preferences.edit().putBoolean("unlockSurveyScreen",false).commit();
        try {
            intent.removeExtra("AUDIOSENSE_STARTED_BY_SERVICE");
        }catch(NullPointerException e){}
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.i("AudioSenseActivity", "OnDestroy Called");

        timer.cancel();
        timer.purge();
        super.onDestroy();
    }

    public void gotoHomepage() {

        Intent intent = getIntent();
        boolean startedByService = intent.getBooleanExtra("AUDIOSENSE_STARTED_BY_SERVICE",false);

        if(startedByService) {
            setContentView(R.layout.homepageappstarted);
        } else {
            setContentView(R.layout.homepageuserstarted);
        }
    }

    public void onStartStudy(boolean testing) {

        SharedPreferences preferences = getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);

        preferences.edit().putInt("running",((testing) ? 1 : 0) + 1).commit();

        if(testing) {
            timer.cancel();
            timer.purge();

            setContentView(R.layout.homepageappstarted);

        } else {
            int startMin = preferences.getInt("startMin", -1);
            int startHour = preferences.getInt("startHour", -1);
            int startDay = preferences.getInt("startDay", -1);
            int startMonth = preferences.getInt("startMonth", -1);
            int startYear = preferences.getInt("startYear", -1);

            int endMin = preferences.getInt("endMin", -1);
            int endHour = preferences.getInt("endHour", -1);
            int endDay = preferences.getInt("endDay", -1);
            int endMonth = preferences.getInt("endMonth", -1);
            int endYear = preferences.getInt("endYear", -1);

            long startTime = TimingManager.getUnixTimeMS(startYear, startMonth, startDay, startHour, startMin);
            Log.e("startTime",Long.toString(startTime));
            long startTimeSurvey = startTime + (preferences.getInt("surveyInterval",-1) * 60000);
            Log.e("startTimeSurvey",Long.toString(startTimeSurvey));
            long endTime = TimingManager.getUnixTimeMS(endYear, endMonth, endDay, endHour, endMin);
            Log.e("endTime",Long.toString(endTime));

            Log.w("Study Start Time", Long.toString(startTime));
            Log.w("Study End Time", Long.toString(endTime));

            Intent alarmIntentStudy = new Intent(getApplicationContext(), TimingManager.class);
            alarmIntentStudy.setAction("AudioSenseStartStudy");
            PendingIntent pendingIntentStudy = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntentStudy, 0);

            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            preferences.edit().putLong("nextAudioSample", startTime + AudioSenseConstants.defaultAudioSampleStartDelay).commit();

            //temp
            preferences.edit().putLong("nextAudioSurveySample", startTime).commit();

            Intent alarmIntentEnd = new Intent(getApplicationContext(), EndStudyManager.class);
            alarmIntentEnd.setAction("AudioSenseEndStudy");
            PendingIntent pendingIntentEnd = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntentEnd, 0);

            Intent alarmIntentSurveyCounter = new Intent(getApplicationContext(), SurveyCountManager.class);
            alarmIntentSurveyCounter.setAction("Update_Daily_Survey_Count");
            PendingIntent pendingIntentSurveyCounter = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntentSurveyCounter, 0);


            if (android.os.Build.VERSION.SDK_INT < 19) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, pendingIntentStudy);
                alarmManager.set(AlarmManager.RTC_WAKEUP, startTimeSurvey, pendingIntentSurveyCounter);
                alarmManager.set(AlarmManager.RTC_WAKEUP, endTime, pendingIntentEnd);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime, pendingIntentStudy);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTimeSurvey, pendingIntentSurveyCounter);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTime, pendingIntentEnd);
            }
        }
    }

    public void onStartSurvey(View view) {
        SharedPreferences preferences = getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);
        Log.i("AudioSenseActivity", "OnStartSurvey Called");
        rootView = ((LinearLayout) findViewById(R.id.homepageRootLayout));
        survey = new AudioSense2("AudioSense2",(int) Math.ceil((double) preferences.getInt("surveyInterval",3) / 60), this, rootView);
        routeComponents = new RouteComponents(this, rootView, survey, survey.getComponents());
        routeComponents.route(0);
    }

    public void onIgnoreSurvey(View view) {
        timer.cancel();
        timer.purge();

        SharedPreferences preferences = getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);

        preferences.edit().putInt("dailyGivenSurveys",preferences.getInt("dailyGivenSurveys",0) + 1).commit();
        preferences.edit().putInt("totalGivenSurveys",preferences.getInt("totalGivenSurveys",0) + 1).commit();

        Log.i("AudioSenseActivity", "OnIgnoreSurvey");

        finish();
    }

    public void onExitSurvey(View view){
        timer.cancel();
        timer.purge();

        finish();
    }

    public void onSnoozeSurvey(View view) {

        SharedPreferences preferences = getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);

        if(preferences.getInt("running",1) != 2) {

            timer.cancel();
            timer.purge();

            TimingManager.cancelAudioSurveySample(getApplicationContext());
            TimingManager.rescheduleAudioSurveySample(getApplicationContext(), true);
            Toast.makeText(this,"Next alarm will not occur for at least " + Integer.toString(preferences.getInt("snoozeTime",AudioSenseConstants.defaultSnooze)) + " minutes",Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this,"Disabled: There is no current study running",Toast.LENGTH_LONG).show();
        }
    }

    public void onChangeVibration(View view) {

        SharedPreferences preferences = getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);

        if(preferences.getInt("running",1) != 2) {

            if (preferences.getBoolean("vibrationOnly", false)) {
                preferences.edit().putBoolean("vibrationOnly", false).commit();
                Toast.makeText(this, "Normal Alarm Mode", Toast.LENGTH_LONG).show();
            } else {
                preferences.edit().putBoolean("vibrationOnly", true).commit();
                Toast.makeText(this, "Vibration Only Mode", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Vibration toggle disabled for testing",Toast.LENGTH_LONG).show();
        }
    }

    public void onStaffSettings(View view) {

        new AudioSenseSettingsUI(this);
    }

    public void onSubmitSurvey(View view) {

        SharedPreferences preferences = getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);
        if(preferences.getInt("running",1) != 2) {

            timer.cancel();
            timer.purge();


            preferences.edit().putInt("dailyTakenSurveys", preferences.getInt("dailyTakenSurveys", 0) + 1).commit();
            preferences.edit().putInt("totalTakenSurveys", preferences.getInt("totalTakenSurveys", 0) + 1).commit();
            preferences.edit().putInt("dailyGivenSurveys", preferences.getInt("dailyGivenSurveys", 0) + 1).commit();
            preferences.edit().putInt("totalGivenSurveys", preferences.getInt("totalGivenSurveys", 0) + 1).commit();

            ArrayList<String> results = survey.getResults();

            FileOutputStream os = null;
            try {
                os = new FileOutputStream(getSurveySampleFileName());
                for (int i = 0; i < results.size(); i++) {
                    os.write((results.get(i) + "\n").getBytes());
                }
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            TimingManager.cancelAudioSurveySample(getApplicationContext());
            TimingManager.rescheduleAudioSurveySample(getApplicationContext(), false);

        }

        finish();

        Log.e("AudioSenseActivity", "survey finished");
    }

    public void onBackPressed() {
        Toast.makeText(this, "Back Button is Disabled", Toast.LENGTH_SHORT).show();
        return;
    }

    public static void initDailySurveyTrack(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, SurveyCountManager.class);
        alarmIntent.setAction("Update_Daily_Survey_Count");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        int startMin = preferences.getInt("startMin", -1);
        int startHour = preferences.getInt("startHour", -1);

        int endMin = preferences.getInt("endMin",-1);
        int endHour = preferences.getInt("endHour",-1);
        int endDay = preferences.getInt("endDay",-1);
        int endMonth = preferences.getInt("endMonth", -1);
        int endYear = preferences.getInt("endYear",-1);
        long endTime = TimingManager.getUnixTimeMS(endYear, endMonth, endDay, endHour, endMin);

        long firstAlarmNextDay = ((24 * 3600000) - TimingManager.getCurrentTime()) + TimingManager.getCurrentUnixTimeStamp() + (startHour * 3600000) + (startMin * 60000);

        if(firstAlarmNextDay <= endTime) {
            if(android.os.Build.VERSION.SDK_INT < 19) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, firstAlarmNextDay, pendingIntent);
            }else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, firstAlarmNextDay, pendingIntent);
            }
        }

        AlarmAlertWakeLock.releaseCpuLock();
    }

    private String getSurveySampleFileName() {
        SharedPreferences preferences =  getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);

        int patientID = preferences.getInt("patientID", -1);
        int conditionID = preferences.getInt("conditionID", -1);
        int sessionID = preferences.getInt("sessionID",-1);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        return "/sdcard/SURVEY_PID" + Integer.toString(patientID) + "_CID" + Integer.toString(conditionID) + "_SID" + Integer.toString(sessionID) +
                "_DATE" + Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) +
                "_TIME" + Integer.toString(hour) + "-" + Integer.toString(min) + "-" + Integer.toString(sec) + ".survey";
    }

    public void onUserLeaveHint() {
        Toast.makeText(this, "Please use the exit button to exit the app. Pressing the home button may count a survey as abandoned",Toast.LENGTH_SHORT).show();
        intent.removeExtra("AUDIOSENSE_STARTED_BY_SERVICE");
    }

    public void finish() {
        try {
            timer.cancel();
            timer.purge();
        }catch(Exception e){}

        try {
            screenOnWakeLock.release();
        } catch(RuntimeException e) {}
        super.finish();
    }

}