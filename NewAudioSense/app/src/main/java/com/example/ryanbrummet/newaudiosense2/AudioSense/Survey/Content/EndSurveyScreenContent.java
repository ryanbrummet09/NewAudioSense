package com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content;

import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSingleSelectionContent;

import java.util.Calendar;

/**
 * Created by ryanbrummet on 9/11/15.
 */
public class EndSurveyScreenContent extends AbstractSingleSelectionContent {

    long endTime;

    public EndSurveyScreenContent(String title) {
        super(title);
    }

    public void setResponse(String dummyString, int dummyInt) {
        Calendar calendar = Calendar.getInstance();
        getResponseContent().set(0,Long.toString(calendar.getTimeInMillis()));
    }
}
