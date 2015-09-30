package com.example.ryanbrummet.newaudiosense2.AudioSense.Main;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.ryanbrummet.newaudiosense2.AudioSense.Settings.AudioSenseSettingsUI;
import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content.AudioSense2;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.LogUsage;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.RouteComponents;
import com.example.ryanbrummet.newaudiosense2.R;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ryanbrummet on 9/24/15.
 */
public class AudioSenseActivity extends Activity{

    private AudioSense2 survey;
    private LinearLayout rootView;
    protected RouteComponents routeComponents;
    private boolean unlockSurveyScreen;
    private SharedPreferences preferences;
    private PowerManager.WakeLock screenOnWakeLock;
    private Intent intent;
    private AudioSenseActivity activity = this;
    private BroadcastReceiver timeoutReceiver;
    private LogUsage logUsage;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.intent = getIntent();

        logUsage = new LogUsage(this);
        //logUsage.startLoggingMemUsage();

        timeoutReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.w("ActivityTimoutManager", "Broadcast received");
                AlarmAlertWakeLock.acquireCpuWakeLock(activity);
                activity.appTimeout();
                AlarmAlertWakeLock.releaseCpuLock();
            }
        };

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        screenOnWakeLock =  powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,"ScreenlockTag");
        screenOnWakeLock.acquire();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.i("AudioSenseActivity", "OnCreate Called");

        preferences = getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);
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
            setContentView(R.layout.homepageappstarted);
        } else {
            if(preferences.getBoolean("userLock",false)) {
                Toast.makeText(this, "You cannot open the app at this time.  A survey is currently being delivered", Toast.LENGTH_LONG).show();
                finish();
            } else {
                long currentTime = TimingManager.getCurrentUnixTimeStamp();
                long timeRemaining = preferences.getLong("nextAudioSurveySample",currentTime) - currentTime - 1;

                if(timeRemaining <= AudioSenseConstants.defaultUserSurveyTimeoutInMin * 30000 && timeRemaining > 0) {
                    Toast.makeText(this, "You can't open the app at this time.  A survey is currently being delivered",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    if(timeRemaining > 0) {
                        appTimeout = timeRemaining;
                    } else {
                        appTimeout = AudioSenseConstants.defaultUserSurveyTimeoutInMin * 60000;
                    }
                }
                setContentView(R.layout.homepageuserstarted);
            }
        }

        registerReceiver(timeoutReceiver, new IntentFilter("APP_TIMEOUT"));

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent timeoutIntent = new Intent(this, ActivityTimeoutManager.class);
        timeoutIntent.setAction("AudioSenseAppTimeout");
        PendingIntent pendingIntentTimeout = PendingIntent.getBroadcast(this, 0, timeoutIntent, 0);
        if (android.os.Build.VERSION.SDK_INT < 19) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, TimingManager.getCurrentUnixTimeStamp() + appTimeout, pendingIntentTimeout);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, TimingManager.getCurrentUnixTimeStamp() + appTimeout, pendingIntentTimeout);
        }
    }

    protected void onStart() {
        Log.i("AudioSenseActivity","onStart Called");
        super.onStart();
    }

    protected void onResume() {
        Log.i("AudioSenseActivity","onResume Called");

        Switch switchWidget;
        switchWidget = (Switch) findViewById(R.id.homepageVibrationApp);
        if(switchWidget == null) {
            switchWidget = (Switch) findViewById(R.id.homepageVibrationUser);
        }
        try {
            if (preferences.getBoolean("vibrationOnly", false)) {
                switchWidget.setChecked(true);
            } else {
                switchWidget.setChecked(false);
            }
        } catch(Exception e){}
        switchWidget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onChangeVibration();
            }
        });

        super.onResume();
    }

    protected void onRestart() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(rootView.getWindowToken(), 0, 0);
        } catch (RuntimeException e) {}
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            ViewGroup layout = (ViewGroup) (findViewById(R.id.passwordLayout));
            imm.toggleSoftInputFromWindow(layout.getWindowToken(), 0, 0);

        } catch (RuntimeException e) {}
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            ViewGroup layout = (ViewGroup) (findViewById(R.id.settingsRoot));
            imm.toggleSoftInputFromWindow(layout.getWindowToken(), 0, 0);

        } catch (RuntimeException e) {}
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            ViewGroup layout = (ViewGroup) (findViewById(R.id.confirmScreen));
            imm.toggleSoftInputFromWindow(layout.getWindowToken(), 0, 0);

        } catch (RuntimeException e) {}

        Log.i("AudioSenseActivity","onRestart Called");
        super.onRestart();
    }

    protected void onPause() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(rootView.getWindowToken(), 0, 0);
        } catch (RuntimeException e) {}
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            ViewGroup layout = (ViewGroup) (findViewById(R.id.passwordLayout));
            imm.toggleSoftInputFromWindow(layout.getWindowToken(), 0, 0);

        } catch (RuntimeException e) {}
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            ViewGroup layout = (ViewGroup) (findViewById(R.id.settingsRoot));
            imm.toggleSoftInputFromWindow(layout.getWindowToken(), 0, 0);

        } catch (RuntimeException e) {}
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            ViewGroup layout = (ViewGroup) (findViewById(R.id.confirmScreen));
            imm.toggleSoftInputFromWindow(layout.getWindowToken(), 0, 0);

        } catch (RuntimeException e) {}

        Log.i("AudioSenseActivity", "OnPause Called");
        try {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch(RuntimeException e) {}
        try {
            screenOnWakeLock.release();
        } catch(RuntimeException e){}
        super.onPause();
    }

    protected void onStop() {
        Log.i("AudioSenseActivity", "OnStop Called");
        super.onStop();
    }

    protected void onDestroy() {
        preferences.edit().putBoolean("uiRunning",false).commit();
        preferences.edit().putBoolean("unlockSurveyScreen",false).commit();
        Log.i("AudioSenseActivity", "OnDestroy Called");

        try {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch(RuntimeException e) {}

        try {
            screenOnWakeLock.release();
        } catch(RuntimeException e) {}

        try {
            unregisterReceiver(timeoutReceiver);
        } catch(RuntimeException e) {}
        unlockSurveyScreen = false;
        //logUsage.stopLoggingMemUsage();

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
        Log.i("AudioSenseActivity", "OnStartSurvey Called");
        rootView = ((LinearLayout) findViewById(R.id.homepageRootLayout));
        survey = new AudioSense2("AudioSense2",preferences.getInt("surveyInterval",90), this, rootView);
        routeComponents = new RouteComponents(this, rootView, survey, survey.getComponents());
        routeComponents.route(0);
    }

    public void onIgnoreSurvey(View view) {

        Log.w("AudioSenseActivity", "onIgnoredSurvey");

        preferences.edit().putInt("dailyGivenSurveys",preferences.getInt("dailyGivenSurveys",0) + 1).commit();
        preferences.edit().putInt("totalGivenSurveys",preferences.getInt("totalGivenSurveys",0) + 1).commit();

        Log.i("AudioSenseActivity", "OnIgnoreSurvey");

        finish();
    }

    public void onExitSurvey(View view){
        finish();
    }

    public void onSnoozeSurvey(View view) {

        Log.w("AudioSenseActivity", "onSnoozeCalled");

        if(preferences.getInt("running",1) != 2) {

            try {
                TimingManager.cancelAudioSurveySample(getApplicationContext());
                TimingManager.rescheduleAudioSurveySample(getApplicationContext(), true, unlockSurveyScreen, false);
                Toast.makeText(this, "Next alarm will not occur for at least " + Integer.toString(preferences.getInt("snoozeTime", AudioSenseConstants.defaultSnooze)) + " minutes", Toast.LENGTH_LONG).show();
                finish();
            } catch (ArrayIndexOutOfBoundsException e){
                Toast.makeText(this,"Disabled: There is no current study running",Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this,"Disabled: There is no current study running",Toast.LENGTH_LONG).show();
        }
    }

    public void onChangeVibration() {

        Log.w("AudioSenseActivity", "onChangeVibration");

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
        if(preferences.getInt("running",1) != 2) {

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
            TimingManager.rescheduleAudioSurveySample(getApplicationContext(), false, unlockSurveyScreen,false);

        }

        Log.w("AudioSenseActivity", "survey finished");

        finish();
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

    public void appTimeout() {

        SharedPreferences preferences = getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);

        Log.w("AudioSenseActivity","User Timeout");
        if(unlockSurveyScreen) {
            preferences.edit().putInt("dailyGivenSurveys", preferences.getInt("dailyGivenSurveys", 0) + 1).commit();
            preferences.edit().putInt("totalGivenSurveys", preferences.getInt("totalGivenSurveys", 0) + 1).commit();
        }

        finish();
    }


}
