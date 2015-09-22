package com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content;


import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSingleSelectionContent;

/**
 * Created by ryanbrummet on 8/25/15.
 */
public class GHABPComponentContent extends AbstractSingleSelectionContent {

    private final String contextDescription;

    public GHABPComponentContent(String instruction, String contextDescription) {
        super(instruction);
        this.contextDescription = contextDescription;
    }

    public String getContextDescription() {
        return contextDescription;
    }
}