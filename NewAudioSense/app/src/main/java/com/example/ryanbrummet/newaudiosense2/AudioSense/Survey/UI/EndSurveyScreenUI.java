package com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.UI;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryanbrummet.newaudiosense2.AudioSense.Main.AudioSenseConstants;
import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content.EndSurveyScreenContent;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSingleSelectionSubComponent;

/**
 * Created by ryanbrummet on 9/11/15.
 */
public class EndSurveyScreenUI extends AbstractSingleSelectionSubComponent {

    public EndSurveyScreenUI(String id, EndSurveyScreenContent content) {
        super(id,content);
    }

    public void render(Activity activity, Display display, ViewGroup rootView) {
        // remove all current views from root view
        rootView.removeAllViews();

        SharedPreferences preferences = activity.getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);
        int surveysTakenToday = preferences.getInt("dailyTakenSurveys",0) + 1;
        int surveysGivenToday = preferences.getInt("dailyGivenSurveys",0) + 1;
        int surveysTakenTotal = preferences.getInt("totalTakenSurveys",0) + 1;
        int surveysGivenTotal = preferences.getInt("totalGivenSurveys",0) + 1;

        LinearLayout.LayoutParams paramTitle = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,display.getHeight() / 10);
        paramTitle.gravity = Gravity.CENTER_HORIZONTAL;
        LinearLayout.LayoutParams paramText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,display.getHeight() / 20);
        LinearLayout.LayoutParams paramTextSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,display.getHeight() / 40);
        LinearLayout.LayoutParams paramEndSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,7 * display.getHeight() / 40);
        LinearLayout.LayoutParams paramNavPanel = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView titleSpace = new TextView(activity);
        rootView.addView(titleSpace,paramTextSpace);

        TextView title = StaticUIComponents.blueInstructionTextView(activity, getContent().getInstructionQuestion(), 36);
        rootView.addView(title,paramTitle);

        TextView firstSpace = new TextView(activity);
        rootView.addView(firstSpace,paramTextSpace);

        TextView completedToday = new TextView(activity);
        completedToday.setText("Surveys Completed Today: " + Integer.toString(surveysTakenToday));
        rootView.addView(completedToday,paramText);
        completedToday.setTextSize(20);

        TextView secondSpace = new TextView(activity);
        rootView.addView(secondSpace,paramTextSpace);

        TextView givenToday = new TextView(activity);
        givenToday.setText("Surveys Given Today: " + Integer.toString(surveysGivenToday));
        rootView.addView(givenToday, paramText);
        givenToday.setTextSize(20);

        TextView thirdSpace = new TextView(activity);
        rootView.addView(thirdSpace,paramTextSpace);

        TextView todayPercent = new TextView(activity);
        todayPercent.setText("Today's Completion: " + Integer.toString((int) Math.floor(
                (((double) surveysTakenToday) / ((double) surveysGivenToday)) * 100)) + "%");
        rootView.addView(todayPercent,paramText);
        todayPercent.setTextSize(20);

        TextView fourthSpace = new TextView(activity);
        rootView.addView(fourthSpace,paramTextSpace);

        TextView completedTotal = new TextView(activity);
        completedTotal .setText("Total Surveys Taken: " + Integer.toString(surveysTakenTotal));
        rootView.addView(completedTotal ,paramText);
        completedTotal.setTextSize(20);

        TextView fifthSpace = new TextView(activity);
        rootView.addView(fifthSpace,paramTextSpace);

        TextView givenTotal = new TextView(activity);
        givenTotal.setText("Total Surveys Given: " + Integer.toString(surveysGivenTotal));
        rootView.addView(givenTotal, paramText);
        givenTotal.setTextSize(20);

        TextView sixthSpace = new TextView(activity);
        rootView.addView(sixthSpace,paramTextSpace);

        TextView totalPercent = new TextView(activity);
        totalPercent.setText("Total Completion: " + Integer.toString((int) Math.floor(
                (((double) surveysTakenTotal) / ((double) surveysGivenTotal)) * 100)) + "%");
        rootView.addView(totalPercent,paramText);
        totalPercent.setTextSize(20);

        TextView endSpace = new TextView(activity);
        rootView.addView(endSpace,paramEndSpace);

        // create two direction nav panel
        RelativeLayout navPanel = StaticUIComponents.twoDirectionNavBar(activity,
                new PrevScreenSurveyListener(),
                new NextScreenSurveyListener(activity),
                "Back","Submit", true, display);
        rootView.addView(navPanel,paramNavPanel);



    }

    public void resetUI(){}

    private class NextScreenSurveyListener implements View.OnClickListener {

        private final Activity activity;

        public NextScreenSurveyListener(Activity activity) {
            super();
            this.activity = activity;
        }

        @Override
        public void onClick(View view) {
            try {
                getContent().setResponse("",0);
                gotoNextComponent();
            } catch (NullPointerException e) {
                Toast.makeText(activity, "Please select an option", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class PrevScreenSurveyListener implements View.OnClickListener {

        public PrevScreenSurveyListener() {
        }

        @Override
        public void onClick(View view) {
            gotoPreviousComponent();
        }
    }
}
