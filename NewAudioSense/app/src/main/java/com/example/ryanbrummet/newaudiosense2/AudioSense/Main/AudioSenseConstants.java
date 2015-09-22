package com.example.ryanbrummet.newaudiosense2.AudioSense.Main;

/**
 * Created by ryanbrummet on 9/1/15.
 */
public class AudioSenseConstants {
    public static final String sharedPrefName = "AudioSense2Preferences";
    public static boolean vibrationOnly = false;
    public static boolean uiRunning = false;

    // this is for the maximum amount of time that a user can spend taking a survey
    public static final int defaultUserSurveyTimeoutInMin = 10;
    public static final int defaultAppSurveyTimeoutInMin = 15;  // should be ten unless running tests

    public static final int defaultPatID = -1;
    public static final int defaultSessionID = -1;
    public static final int defaultConditionID = -1;

    // all are in minutes
    public static final int defaultRandInterval = 1;
    public static final int defaultSurveyTimeout = 1;  // this is for the maximum amount of time a user can take to answer an alarm
    public static final int defaultSnooze = 1;
    public static final int defaultSurveyInterval = 8;

    // min is 1 and max is 10
    public static final int defaultRingSelection = 5;

    // timing stuff in military time
    public static final int defaultStartYear = 2015;
    public static final int defaultStartMonth = 8;
    public static final int defaultStartDay = 13;
    public static final int defaultStartHour =12;
    public static final int defaultStartMin = 39;

    public static final int defaultEndYear = 2015;
    public static final int defaultEndMonth = 8;
    public static final int defaultEndDay = 13;
    public static final int defaultEndHour = 20;
    public static final int defaultEndMin = 55;

    public static final int defaultRunning = 0;  //zero is stopped, 1 is running normal, 2 is running test

    // Audio recoding constants in mins
    public static final int defaultAudioSampleInterval = 2; // 60
    public static final int defaultAudioSampleLength = 1;

    // in millisec
    public static final int defaultAudioSampleStartDelay = 5000;

    // names of persistent variables
    //there are also other variables stored using SharedPreferences but they are not persistent
    public static final String[] persistentNames = {"patientID","sessionID","conditionID",
            "randInterval","surveyTimeout","snoozeTime","surveyInterval","ringSelection",
            "startYear","startMonth","startDay","startHour","startMin","endYear","endMonth",
            "endDay","endHour","endMin","running",
            "startYear","startMonth","startDay","startHour","startMin",
            "endYear","endMonth","endDay","endHour","endMin"};

    // values of persistent variables
    public static final int[] persistentValues = {defaultPatID,defaultSessionID,defaultConditionID,
            defaultRandInterval,defaultSurveyTimeout,defaultSnooze,defaultSurveyInterval,defaultRingSelection,
            defaultStartYear,defaultStartMonth,defaultStartDay,defaultStartHour,defaultStartMin,
            defaultEndYear,defaultEndMonth,defaultEndDay,defaultEndHour,defaultEndMin,
            defaultRunning,
            defaultStartYear, defaultStartMonth, defaultStartDay, defaultStartHour, defaultStartMin,
            defaultEndYear, defaultEndMonth, defaultEndDay, defaultEndHour, defaultEndMin};

    // settings password
    public static final String password = "hear";

}
