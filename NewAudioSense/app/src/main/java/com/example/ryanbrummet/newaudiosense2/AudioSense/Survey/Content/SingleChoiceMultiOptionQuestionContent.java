package com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content;

import java.util.ArrayList;

import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSingleSelectionContent;

/**
 * Created by ryanbrummet on 8/25/15.
 */
public class SingleChoiceMultiOptionQuestionContent extends AbstractSingleSelectionContent {

    private String contextDescription;
    private final ArrayList<String> options;

    public SingleChoiceMultiOptionQuestionContent(String question, String contextDescription,
                                                  ArrayList<String> options) {
        super(question);
        this.contextDescription = contextDescription;
        this.options = options;

        if (options.size() > 6) {
            throw new TooManyOptionsException("Because the rootView of the app is a LinearLayout" +
                    " the number of options for this component type is limited to at most 6.  As an" +
                    " aside, the rootView is made to be a LinearLayout to make sure that all information" +
                    " for a component with no subcomponents appears on the phones screen in full.");
        }
    }

    public SingleChoiceMultiOptionQuestionContent(String question, String contextDescription,
                                                  ArrayList<String> options, boolean byPassSizeLimit) {
        super(question);
        this.contextDescription = contextDescription;
        this.options = options;

        if (options.size() > 6 && !byPassSizeLimit) {
            throw new TooManyOptionsException("Because the rootView of the app is a LinearLayout" +
                    " the number of options for this component type is limited to at most 6.  As an" +
                    " aside, the rootView is made to be a LinearLayout to make sure that all information" +
                    " for a component with no subcomponents appears on the phones screen in full.");
        }
    }

    public String getContextDescription() {
        return contextDescription;
    }

    public ArrayList<String> getOptions() {
        return options;
    }
}
