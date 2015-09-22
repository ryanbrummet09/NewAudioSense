package com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode;

import java.util.ArrayList;

/**
 * Created by ryanbrummet on 8/23/15.
 */
public abstract class AbstractSurveyRootComponent {

    private final String componentID;

    public AbstractSurveyRootComponent(String componentID) {
        this.componentID = componentID;
    }

    public String getComponentID() {
        return componentID;
    }

    abstract public ArrayList<String> getResults();

    abstract public void gotoNextComponent();

    abstract public void gotoPreviousComponent();
}
