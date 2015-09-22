package com.example.ryanbrummet.newaudiosense2.AudioSense.Settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ryanbrummet.newaudiosense2.AudioSense.Main.AudioSenseActivity;
import com.example.ryanbrummet.newaudiosense2.AudioSense.Main.AudioSenseConstants;
import com.example.ryanbrummet.newaudiosense2.R;

/**
 * Created by ryanbrummet on 8/24/15.
 */
public class AudioSenseSettingsUI {

    private AudioSenseActivity activity;
    private AudioSenseSettingsContent content;
    private SharedPreferences preferences;

    private EditText patientID;
    private EditText conditionID;
    private EditText sessionID;
    private EditText randInterval;
    private EditText surveyTimeout;
    private EditText snoozeTime;
    private EditText surveyInterval;
    private EditText ringSelection;
    private DatePicker startDate;
    private DatePicker endDate;
    private TimePicker startTime;
    private TimePicker endTime;

    public AudioSenseSettingsUI(final AudioSenseActivity activity) {
        this.activity = activity;
        this.content = new AudioSenseSettingsContent(activity);
        this.preferences = activity.getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);

        activity.setContentView(R.layout.password_ui);
        Button passwordSubmit = (Button) activity.findViewById(R.id.passwordSubmit);
        passwordSubmit.setOnClickListener(new OnPasswordSubmitListener());
        Button passwordBack = (Button) activity.findViewById(R.id.passwordBack);
        passwordBack.setOnClickListener(new OnBackListener());
        Button infoButton = (Button) activity.findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new OnPasswordInfoListener());

        EditText passwordField = (EditText) activity.findViewById(R.id.passwordField);
        passwordField.setFocusable(true);
        passwordField.setPrivateImeOptions("nm"); // disable microphone button.  absolutely essential to avoid concurrency problems
        passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        Toast.makeText(activity, "Press submit to submit password", Toast.LENGTH_SHORT).show();
                        break;
                    case EditorInfo.IME_ACTION_NEXT:
                        Toast.makeText(activity, "Press submit to submit password", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    private void openSettings() {

        activity.setContentView(R.layout.staffsettings);

        Button backButton = (Button) activity.findViewById(R.id.backButton);
        backButton.setOnClickListener(new OnBackListener());
        Button submitButton = (Button) activity.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new OnSubmitListener());

        patientID = (EditText) activity.findViewById(R.id.patientID);
        conditionID = (EditText) activity.findViewById(R.id.conditionID);
        sessionID = (EditText) activity.findViewById(R.id.sessionID);
        randInterval = (EditText) activity.findViewById(R.id.randInterval);
        surveyTimeout = (EditText) activity.findViewById(R.id.timeout);
        snoozeTime = (EditText) activity.findViewById(R.id.snooze);
        surveyInterval = (EditText) activity.findViewById(R.id.surveyInterval);
        ringSelection = (EditText) activity.findViewById(R.id.ringSelection);

        // set default start/end time/date. start and end are the same indicating default test state
        startTime = (TimePicker) activity.findViewById(R.id.startTime);
        startTime.setCurrentHour(12);
        startTime.setCurrentMinute(0);
        endTime = (TimePicker) activity.findViewById(R.id.endTime);
        endTime.setCurrentHour(12);
        endTime.setCurrentMinute(0);
        startDate = (DatePicker) activity.findViewById(R.id.startDate);
        startDate.updateDate(2015,0,1);
        endDate = (DatePicker) activity.findViewById(R.id.endDate);
        endDate.updateDate(2015,0,1);
    }

    private void openConfirmScreen(boolean testing) {

        if(testing) {
            Toast.makeText(activity,"YOU ARE IN TESTING MODE",Toast.LENGTH_LONG).show();
            content.init();
            content.start(true);
            return;
        }

        activity.setContentView(R.layout.staffsetttingsubmitscreen);

            TextView patientIDChoice = (TextView) activity.findViewById(R.id.patientIDChoice);
            patientIDChoice.setText("Patient ID: " + patientID.getText().toString());

            TextView conditionIDChoice = (TextView) activity.findViewById(R.id.conditionIDChoice);
            conditionIDChoice.setText("Condition ID: " + conditionID.getText().toString());

            TextView sessionIDChoice = (TextView) activity.findViewById(R.id.sessionIDChoice);
            sessionIDChoice.setText("Session ID: " + sessionID.getText().toString());

            TextView randIntervalChoice = (TextView) activity.findViewById(R.id.randIntervalChoice);
            randIntervalChoice.setText("Survey random interval: " + randInterval.getText().toString());

            TextView timeoutChoice = (TextView) activity.findViewById(R.id.timeoutChoice);
            timeoutChoice.setText("Survey surveyTimeout: " + surveyTimeout.getText().toString());

            TextView snoozeChoice = (TextView) activity.findViewById(R.id.snoozeChoice);
            snoozeChoice.setText("Survey snoozeTime time: " + snoozeTime.getText().toString());

            TextView surveyIntervalChoice = (TextView) activity.findViewById(R.id.surveyIntervalChoice);
            surveyIntervalChoice.setText("Survey sample interval: " + surveyInterval.getText().toString());

            TextView ringSelectionChoice = (TextView) activity.findViewById(R.id.ringSelectionChoice);
            ringSelectionChoice.setText("Ringer value: " + ringSelection.getText().toString());

            TextView startDateChoice = (TextView) activity.findViewById(R.id.startDateChoice);
            startDateChoice.setText("Start date: " + Integer.toString(startDate.getMonth() + 1) +
                    "-" + Integer.toString(startDate.getDayOfMonth()) + "-" + Integer.toString(startDate.getYear()));

            TextView endDateChoice = (TextView) activity.findViewById(R.id.endDateChoice);
            endDateChoice.setText("End date: " + Integer.toString(endDate.getMonth() + 1) +
                    "-" + Integer.toString(endDate.getDayOfMonth()) + "-" + Integer.toString(endDate.getYear()));

            TextView startTimeChoice = (TextView) activity.findViewById(R.id.startTimeChoice);
            if(startTime.getCurrentMinute() < 10) {
                startTimeChoice.setText("Start time: " + Integer.toString(startTime.getCurrentHour()) +
                        ":0" + Integer.toString(startTime.getCurrentMinute()));
            } else {
                startTimeChoice.setText("Start time: " + Integer.toString(startTime.getCurrentHour()) +
                        ":" + Integer.toString(startTime.getCurrentMinute()));
            }

            TextView endTimeChoice = (TextView) activity.findViewById(R.id.endTimeChoice);
            if(endTime.getCurrentMinute() < 10) {
                endTimeChoice.setText("End time: " + Integer.toString(endTime.getCurrentHour()) +
                        ":0" + Integer.toString(endTime.getCurrentMinute()));
            } else {
                endTimeChoice.setText("End time: " + Integer.toString(endTime.getCurrentHour()) +
                        ":" + Integer.toString(endTime.getCurrentMinute()));
            }


        Button reject = (Button) activity.findViewById(R.id.rejectChoices);
        reject.setOnClickListener(new OnRejectListener());
        Button confirm = (Button) activity.findViewById(R.id.confirmChoices);
        confirm.setOnClickListener(new OnConfirmListener());

    }

    // handles info button click on password screen
    private class OnPasswordInfoListener implements View.OnClickListener{
        public OnPasswordInfoListener() {
            super();
        }

        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Password is the same as last AudioSense app.  Contact Ryan Brummet for more details")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    // handles password submissions
    private class OnPasswordSubmitListener implements View.OnClickListener{
        public OnPasswordSubmitListener() {
            super();
        }

        public void onClick(View view) {
            EditText passwordField = (EditText) activity.findViewById(R.id.passwordField);
            if(passwordField.getText().toString().equals(AudioSenseConstants.password)) {
                if(preferences.getInt("running",0) > 0) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    content.stop();

                                    openSettings();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    activity.gotoHomepage();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("A study interval is currently running. " +
                            "Would you like to stop it and purge the study settings?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                } else {
                    openSettings();
                }
            } else {
                Toast.makeText(activity, "Password Incorrect", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // handles on back button presses (returns to app home screen)
    private class OnBackListener implements  View.OnClickListener{

        public void onClick(View view) {
            activity.gotoHomepage();
        }

    }

    // handles setting submission
    private class OnSubmitListener implements  View.OnClickListener{

        public OnSubmitListener(){
            super();
        }

        public void onClick(View view) {

            TimePicker startTime = (TimePicker) activity.findViewById(R.id.startTime);
            TimePicker endTime = (TimePicker) activity.findViewById(R.id.endTime);
            if(startTime.getCurrentMinute().equals(endTime.getCurrentMinute()) &&
                    startTime.getCurrentHour().equals(endTime.getCurrentHour()) &&
                    endDate.getYear() == startDate.getYear() &&
                    endDate.getMonth() == startDate.getMonth() &&
                    endDate.getDayOfMonth() == startDate.getDayOfMonth()) {

                openConfirmScreen(true);
            } else {
                if(!patientID.getText().toString().equals("") &&
                        !sessionID.getText().toString().equals("") &&
                        !conditionID.getText().toString().equals("") &&
                        !randInterval.getText().toString().equals("") &&
                        !surveyTimeout.getText().toString().equals("") &&
                        !snoozeTime.getText().toString().equals("") &&
                        !surveyInterval.getText().toString().equals("") &&
                        !ringSelection.getText().toString().equals("")) {

                    // make sure ring selection is valid
                    if(Integer.parseInt(ringSelection.getText().toString()) > 10 ||
                            Integer.parseInt(ringSelection.getText().toString()) < 1) {
                        Toast.makeText(activity, "ringSelection must be on the int interval [1,10]", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // check all durations positive (all in mins)
                    int surveyDayPeriod = ((endTime.getCurrentHour() * 60) + endTime.getCurrentMinute()) -
                            ((startTime.getCurrentHour() * 60) + startTime.getCurrentMinute());

                    int fullSurveyPeriod = (int) ((endDate.getCalendarView().getDate() - startDate.getCalendarView().getDate()) / 60000);
                    if(fullSurveyPeriod < 0) {
                        Toast.makeText(activity, "endDate < startDate, This is not allowed", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(surveyDayPeriod <= 0) {
                        Toast.makeText(activity, "endTime <= startTime, This is not allowed", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(Integer.parseInt(surveyInterval.getText().toString()) <= 0) {
                        Toast.makeText(activity, "surveyInterval must be greater than 0", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(Integer.parseInt(snoozeTime.getText().toString()) <= 0) {
                        Toast.makeText(activity, "snoozeTime must be greater than 0", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(Integer.parseInt(randInterval.getText().toString()) <= 0) {
                        Toast.makeText(activity, "randomInterval must be greater than 0", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(Integer.parseInt(surveyTimeout.getText().toString()) <= 0) {
                        Toast.makeText(activity, "surveyTimeout must be greater than 0", Toast.LENGTH_LONG).show();
                        return;
                    }/*
                    if(Integer.parseInt(surveyInterval.getText().toString()) <= AudioSenseConstants.defaultUserSurveyTimeoutInMin) {
                        Toast.makeText(activity, "surveyInterval must be larger than the app timeout which is " +
                                Integer.toString(AudioSenseConstants.defaultUserSurveyTimeoutInMin) + " minutes", Toast.LENGTH_LONG).show();
                        return;
                    }*/

                    // check durations are compatible with each other
                    /*
                    if(fullSurveyPeriod <= surveyDayPeriod) {
                        Toast.makeText(activity, "This must be true: endDate - startDate > endTime - startTime", Toast.LENGTH_LONG).show();
                        return;
                    }*/
                    if(surveyDayPeriod > (24 * 60) - Integer.parseInt(randInterval.getText().toString())) {
                        Toast.makeText(activity, "This must be true: endTime - startTime <= (24 * 60) - randomInterval", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(Integer.parseInt(surveyInterval.getText().toString()) >
                            surveyDayPeriod - (2 * Integer.parseInt(randInterval.getText().toString()))) {
                        Toast.makeText(activity, "This must be true: surveyInterval <= endTime - startTime + 2*randomInterval", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(Integer.parseInt(surveyInterval.getText().toString()) < 2 * Integer.parseInt(snoozeTime.getText().toString())) {
                        Toast.makeText(activity, "This must be true: surveyInterval >= 2*snoozeTime", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(Integer.parseInt(snoozeTime.getText().toString()) <= Integer.parseInt(randInterval.getText().toString())){
                        Toast.makeText(activity, "This must be true: snoozeTime > randomInterval", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(Integer.parseInt(randInterval.getText().toString()) <= Integer.parseInt(surveyTimeout.getText().toString())) {
                        Toast.makeText(activity, "This must be true: randomInterval > surveyTimeout", Toast.LENGTH_LONG).show();
                        return;
                    }
                    /*
                    if(Integer.parseInt(surveyInterval.getText().toString()) <= 2 * AudioSenseConstants.defaultAppSurveyTimeoutInMin) {
                        Toast.makeText(activity, "The survey interval must be greater than " + Integer.toString(2 * AudioSenseConstants.defaultUserSurveyTimeoutInMin) + " minutes", Toast.LENGTH_LONG).show();
                        return;
                    }*/


                    openConfirmScreen(false);

                } else {
                    Toast.makeText(activity, "You have left a field blank", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // handles setting rejection
    private class OnRejectListener implements View.OnClickListener {

        public void onClick(View view ) {
            openSettings();
        }

    }

    // handles setting confirmation
    private class OnConfirmListener implements View.OnClickListener {

        public void onClick(View view ) {

            preferences.edit().putInt("patientID",Integer.parseInt(patientID.getText().toString())).commit();

            preferences.edit().putInt("conditionID",Integer.parseInt(conditionID.getText().toString())).commit();

            // we subtract one since 1 is added every time session is used (the first time it is used then
            // sessionID will be equal to the value input by a user)
            preferences.edit().putInt("sessionID",Integer.parseInt(sessionID.getText().toString()) - 1).commit();

            preferences.edit().putInt("randInterval",Integer.parseInt(randInterval.getText().toString())).commit();

            preferences.edit().putInt("surveyTimeout",Integer.parseInt(surveyTimeout.getText().toString())).commit();

            preferences.edit().putInt("snoozeTime",Integer.parseInt(snoozeTime.getText().toString())).commit();

            preferences.edit().putInt("surveyInterval",Integer.parseInt(surveyInterval.getText().toString())).commit();
            preferences.edit().putInt("appTimeout",Integer.parseInt(surveyInterval.getText().toString()) / 3).commit();

            preferences.edit().putInt("ringSelection",Integer.parseInt(ringSelection.getText().toString())).commit();

            preferences.edit().putInt("startYear",startDate.getYear()).commit();
            preferences.edit().putInt("startMonth",startDate.getMonth()).commit();
            preferences.edit().putInt("startDay",startDate.getDayOfMonth()).commit();

            preferences.edit().putInt("endYear",endDate.getYear()).commit();
            preferences.edit().putInt("endMonth",endDate.getMonth()).commit();
            preferences.edit().putInt("endDay",endDate.getDayOfMonth()).commit();

            preferences.edit().putInt("startHour",startTime.getCurrentHour()).commit();
            preferences.edit().putInt("startMin",startTime.getCurrentMinute()).commit();

            preferences.edit().putInt("endHour",endTime.getCurrentHour()).commit();
            preferences.edit().putInt("endMin",endTime.getCurrentMinute()).commit();

            content.init();
            content.start(false);
            activity.finish();
        }

    }
}
