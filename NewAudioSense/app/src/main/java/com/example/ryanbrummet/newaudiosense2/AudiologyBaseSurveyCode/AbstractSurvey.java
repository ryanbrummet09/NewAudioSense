package com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode;

import java.util.ArrayList;
import android.util.Log;

/**
 * Created by ryanbrummet on 8/21/15.
 */
public abstract class AbstractSurvey extends AbstractSurveyRootComponent {

    private final int surveyInterval; // in hours
    private ArrayList<AbstractSurveySubComponent> components;

    public AbstractSurvey(String id, int surveyInterval) {
        super(id);

        this.surveyInterval = surveyInterval;
        this.components = new ArrayList<AbstractSurveySubComponent>();
    }

    /*
    Returns the surveyInterval
     */
    public int getSurveyInterval() {
        return surveyInterval;
    }

    /*
    Returns the components of this Audiology survey
     */
    public ArrayList<AbstractSurveySubComponent> getComponents() {
        return components;
    }

    /*
    Adds AbstractSurveySubComponent to components
     */
    public void addToComponents(AbstractSurveySubComponent component) {
        components.add(component);
    }

    public ArrayList<String> getResults() {
        ArrayList<String> results = new ArrayList<String>();
        results.add(getComponentID());
        for (int i = 0; i < components.size(); i++) {
            results.addAll(components.get(i).getResults());
        }
        for(int i = 0; i < results.size(); i++){
            Log.w("Survey Results",results.get(i));
        }

        return results;
    }


}

