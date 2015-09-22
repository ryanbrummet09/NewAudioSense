package com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content;


import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSingleSelectionContent;

import java.util.Calendar;

/**
 * Created by ryanbrummet on 8/22/15.
 */
public class SurveyInstructionScreenContent extends AbstractSingleSelectionContent {

    public SurveyInstructionScreenContent(String surveyInstruction) {
        super(surveyInstruction);
    }

    public void recordSurveyStartInfo() {
        Calendar calendar = Calendar.getInstance();
        getResponseContent().set(0,Long.toString(calendar.getTimeInMillis()));
    }
}
