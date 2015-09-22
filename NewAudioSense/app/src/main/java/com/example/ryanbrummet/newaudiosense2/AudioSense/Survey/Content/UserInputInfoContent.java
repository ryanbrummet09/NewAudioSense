package com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content;

import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSingleSelectionContent;

/**
 * Created by ryanbrummet on 8/25/15.
 */
public class UserInputInfoContent extends AbstractSingleSelectionContent{

    public UserInputInfoContent(String instruction) {
        super(instruction);
    }

    // need to include start and end time information as well.  This will allow us to measure the user
    // burden to an extent.
}
