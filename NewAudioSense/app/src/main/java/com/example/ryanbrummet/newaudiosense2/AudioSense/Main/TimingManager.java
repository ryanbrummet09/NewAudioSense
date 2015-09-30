package com.example.ryanbrummet.newaudiosense2.AudioSense.Main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


import java.util.Calendar;

/**
 * Created by ryanbrummet on 9/8/15.
 */
public class TimingManager  extends BroadcastReceiver {


    // we do nothing if the UI is running.  Be default, we always assume that it is for safety (default setting is running)
    public void onReceive(Context context, Intent intent) {
        AlarmAlertWakeLock.acquireCpuWakeLock(context);
        Log.i("AudioSenseTimingManager", "Broadcast Received");

        SharedPreferences preferences = context.getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);

        long currentTime = getCurrentUnixTimeStamp();

        long nextAudioSample = preferences.getLong("nextAudioSample", -1);
        long nextAudioSurveySample = preferences.getLong("nextAudioSurveySample",nextAudioSample);

        if(intent.getAction().equals("AudioSenseStartStudy")) {
            Log.i("AudioSenseTimingManager", "AudioSenseStartStudy");

            /*
            if(preferences.getBoolean("uiRunning",true)) {
                rescheduleAudioSample(context);
            } else {
                rescheduleAudioSample(context);
                runAudioSurveySample(context, getAudioSampleFileName(preferences, true));
            }*/
            rescheduleAudioSample(context);
            runAudioSurveySample(context, getAudioSampleFileName(preferences, true));

        } else if (intent.getAction().equals("AudioSenseAudioSample")) {
            Log.i("AudioSenseTimingManager", "AudioSenseAudioSample");
            if (nextAudioSurveySample <= currentTime) {
                rescheduleAudioSample(context);

            } else {
                if((((double) (nextAudioSurveySample - currentTime)) / 60000) <= 3 * AudioSenseConstants.defaultAudioSampleLength) {
                    rescheduleAudioSample(context);
                } else {

                    if(preferences.getBoolean("uiRunning",true)) {
                        rescheduleAudioSample(context);
                    } else {
                        runAudioSample(context, getAudioSampleFileName(preferences, false));
                    }
                }
            }
        } else if (intent.getAction().equals("AudioSenseAudioSurveySample")) {
            Log.i("AudioSenseTimingManager", "AudioSenseAudioSurveySample");

            if(preferences.getBoolean("uiRunning",true)) {
                rescheduleAudioSurveySample(context, true,false,false);
            } else {
                runAudioSurveySample(context, getAudioSampleFileName(preferences, true));
            }
        } else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.i("AudioSenseTimingManager", "BOOT_COMPLETED");
            rescheduleAudioSample(context);
            rescheduleAudioSurveySample(context, false,false,false);
        } else {
            throw new RuntimeException("This should never happen: " + intent.getAction());
        }
    }

    public static void rescheduleAudioSample(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);

        long currentUnixTimeStamp = getCurrentUnixTimeStamp();
        long currentTime = getCurrentTime();

        int startMin = preferences.getInt("startMin", -1);
        int startHour = preferences.getInt("startHour",-1);
        int startDay = preferences.getInt("startDay",-1);
        int startMonth = preferences.getInt("startMonth", -1);
        int startYear = preferences.getInt("startYear",-1);
        long startTime = getUnixTimeMS(startYear, startMonth, startDay, startHour, startMin);

        int endMin = preferences.getInt("endMin",-1);
        int endHour = preferences.getInt("endHour",-1);
        int endDay = preferences.getInt("endDay",-1);
        int endMonth = preferences.getInt("endMonth", -1);
        int endYear = preferences.getInt("endYear",-1);
        long endTime = getUnixTimeMS(endYear, endMonth, endDay, endHour, endMin);

        long interval = AudioSenseConstants.defaultAudioSampleInterval * 60000;

        Long nextUnixTimeStamp = currentUnixTimeStamp + interval;

        if( nextUnixTimeStamp >= startTime && nextUnixTimeStamp < endTime) {

            if((startHour * 3600000) + (startMin * 60000) <= currentTime &&
                    currentTime + interval <= (endHour *3600000) + (endMin * 60000)) {

                Intent alarmIntent = new Intent(context, TimingManager.class);
                alarmIntent.setAction("AudioSenseAudioSample");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                preferences.edit().putLong("nextAudioSample",nextUnixTimeStamp).commit();

                if(android.os.Build.VERSION.SDK_INT < 19) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, nextUnixTimeStamp, pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextUnixTimeStamp, pendingIntent);
                }

            } else {

                long nextAlarm = currentUnixTimeStamp - currentTime + (24 * 60 * 60 * 1000) + (startHour * 3600000) + (startMin * 60000) + AudioSenseConstants.defaultAudioSampleStartDelay;

                Intent alarmIntent = new Intent(context, TimingManager.class);
                alarmIntent.setAction("AudioSenseAudioSample");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                preferences.edit().putLong("nextAudioSample",nextAlarm).commit();
                //preferences.edit().putBoolean("vibrationOnly",false).commit();

                if(android.os.Build.VERSION.SDK_INT < 19) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, nextAlarm, pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAlarm, pendingIntent);
                }
            }
        }
    }

    public static void rescheduleAudioSurveySample(Context context, boolean snooze,  boolean appInit, boolean surveyFailed) {

        Log.e("TimingManager","reschedule ran");

        SharedPreferences preferences = context.getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);


        long currentUnixTimeStamp = getCurrentUnixTimeStamp();
        long currentTime = getCurrentTime();

        int startMin = preferences.getInt("startMin", -1);
        int startHour = preferences.getInt("startHour",-1);
        int startDay = preferences.getInt("startDay",-1);
        int startMonth = preferences.getInt("startMonth", -1);
        int startYear = preferences.getInt("startYear",-1);
        long startTime = getUnixTimeMS(startYear, startMonth, startDay, startHour, startMin);

        int endMin = preferences.getInt("endMin",-1);
        int endHour = preferences.getInt("endHour",-1);
        int endDay = preferences.getInt("endDay",-1);
        int endMonth = preferences.getInt("endMonth", -1);
        int endYear = preferences.getInt("endYear",-1);
        long endTime = getUnixTimeMS(endYear, endMonth, endDay, endHour, endMin);

        long interval;
        Long nextUnixTimeStamp;

        if(surveyFailed) {
            interval = ((long) AudioSenseConstants.defaultFailureTimeout) * 60000;
            nextUnixTimeStamp = currentUnixTimeStamp + interval;
        } else {
            if (snooze) {
                if (appInit) {
                    interval = ((long) preferences.getInt("snoozeTime", AudioSenseConstants.defaultSnooze)) * 60000;
                    nextUnixTimeStamp = currentUnixTimeStamp + interval;
                } else {
                    if (preferences.getLong("nextAudioSurveySample", currentUnixTimeStamp) <
                            (((long) preferences.getInt("snoozeTime", AudioSenseConstants.defaultSnooze)) * 60000) + getCurrentUnixTimeStamp()) {
                        interval = ((long) preferences.getInt("snoozeTime", AudioSenseConstants.defaultSnooze)) * 60000;
                        nextUnixTimeStamp = currentUnixTimeStamp + interval;
                    } else {
                        interval = 0;
                        nextUnixTimeStamp = preferences.getLong("nextAudioSurveySample", currentUnixTimeStamp);
                    }
                }
            } else {
                interval = ((long) (preferences.getInt("surveyInterval", AudioSenseConstants.defaultSurveyInterval) +
                        ((double) preferences.getInt("randInterval", AudioSenseConstants.defaultRandInterval)) * ((Math.random() * 2) - 1))) * 60000;
                nextUnixTimeStamp = currentUnixTimeStamp + interval;
            }
        }

        if( nextUnixTimeStamp >= startTime && nextUnixTimeStamp < endTime) {

            if ((startHour * 3600000) + (startMin * 60000) <= currentTime &&
                    currentTime + interval <= (endHour * 3600000) + (endMin * 60000)) {

                Intent alarmIntent = new Intent(context, TimingManager.class);
                alarmIntent.setAction("AudioSenseAudioSurveySample");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                preferences.edit().putLong("nextAudioSurveySample",nextUnixTimeStamp).commit();

                if(android.os.Build.VERSION.SDK_INT < 19) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, nextUnixTimeStamp, pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextUnixTimeStamp, pendingIntent);
                }

            } else {

                long nextAlarm = currentUnixTimeStamp - currentTime + (24 * 60 * 60 * 1000) + (startHour * 3600000) + (startMin * 60000);

                Intent alarmIntent = new Intent(context, TimingManager.class);
                alarmIntent.setAction("AudioSenseAudioSurveySample");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                preferences.edit().putLong("nextAudioSurveySample",nextAlarm).commit();
                //preferences.edit().putBoolean("vibrationOnly",false).commit();

                if(android.os.Build.VERSION.SDK_INT < 19) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, nextAlarm, pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAlarm, pendingIntent);
                }
            }
        }
    }



    public static void cancelAudioSurveySample(Context context) {
        Intent alarmIntent = new Intent(context, TimingManager.class);
        alarmIntent.setAction("AudioSenseAudioSurveySample");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static void runAudioSample(Context context, String fileOutputLocation) {

        Log.w("TimingManager", "Running Audio Sample");

        Intent i = new Intent(context, AudioSampleService.class);
        i.putExtra("fileOutPutLocation",fileOutputLocation);
        context.startService(i);
    }

    public static void runAudioSurveySample(Context context, String fileOutputLocation) {

        Log.w("TimingManager", "Running Audio Survey Sample");

        SharedPreferences preferences = context.getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);
        preferences.edit().putInt("sessionID", preferences.getInt("sessionID",0) + 1).commit();

        Intent i = new Intent(context, AudioSurveySampleService.class);
        i.putExtra("fileOutPutLocation",fileOutputLocation);
        i.putExtra("enteringSurvey", true);
        i.putExtra("surveyTimeout",preferences.getInt("surveyTimeout",1));
        context.startService(i);
    }

    public static long getCurrentUnixTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        return (calendar.getTimeInMillis());
    }

    public static long getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        return (calendar.get(Calendar.HOUR_OF_DAY) * 3600000) + (calendar.get(Calendar.MINUTE) * 60000);
    }

    public static long getUnixOffset() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
    }

    public static long getUnixTimeMS(int year, int month, int day, int hour, int min) {
        int yearsPast = year - 1970;
        int correction = 0;
        if(month < 3 & (year % 4) == 0){
            correction = 1;
        }
        int leapDays = (int) Math.floor((year - 1972) / 4) + 1 - correction;
        int[] cumDaysMonth = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
        int totalDaysElapsed = leapDays + cumDaysMonth[month] + day - 1;
        long toReturn = (yearsPast * 31536000) + (totalDaysElapsed * 86400) + (hour * 3600) + (min * 60);
        return (toReturn * 1000) - getUnixOffset();
    }

    private static String getAudioSampleFileName(SharedPreferences preferences, boolean surveyAudioSample) {
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

        if(surveyAudioSample) {
            return "/sdcard/SA_PID" + Integer.toString(patientID) + "_CID" + Integer.toString(conditionID) + "_SID" + Integer.toString(sessionID) +
                    "_DATE" + Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) +
                    "_TIME" + Integer.toString(hour) + "-" + Integer.toString(min) + "-" + Integer.toString(sec) + ".audio";
        } else {
            return "/sdcard/NSA_PID" + Integer.toString(patientID) + "_CID" + Integer.toString(conditionID) +
                    "_DATE" + Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) +
                    "_TIME" + Integer.toString(hour) + "-" + Integer.toString(min) + "-" + Integer.toString(sec) + ".audio";
        }
    }
}
