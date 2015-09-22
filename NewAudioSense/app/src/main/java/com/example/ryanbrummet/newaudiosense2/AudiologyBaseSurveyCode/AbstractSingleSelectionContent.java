package com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ryanbrummet on 8/22/15.
 */
public abstract class AbstractSingleSelectionContent {

    private String instructionQuestion;
    private ArrayList<String> responseContent;
    private int responseIndex;

    public AbstractSingleSelectionContent(String instructionQuestion) {
        this.instructionQuestion = instructionQuestion;
        this.responseContent = new ArrayList<String>();
        responseContent.add(0,"-1");
        responseIndex = -1;
    }

    public String getInstructionQuestion() {
        return instructionQuestion;
    }

    public ArrayList<String> getResponseContent() {
        return responseContent;
    }


    public int getResponseIndex(){
        return responseIndex;
    }

    public void setResponse(String content, int responseIndex) {
        Log.w(getClass().getSimpleName(),"Set to " + content);
        responseContent.set(0,content);
        this.responseIndex = responseIndex;
    }

    public void removeFromResponseContent() {
        Log.w(getClass().getSimpleName(),"Removed " + responseContent);
        responseContent.set(0,"NaN");
    }


}
