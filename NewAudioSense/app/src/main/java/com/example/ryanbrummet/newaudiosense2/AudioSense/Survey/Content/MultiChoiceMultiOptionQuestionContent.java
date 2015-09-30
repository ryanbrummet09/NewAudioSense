package com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ryanbrummet on 9/22/15.
 */
public class MultiChoiceMultiOptionQuestionContent {

    private String instructionQuestion;
    private ArrayList<String> responseContent;
    private String contextDescription;
    private ArrayList<String> options;

    public MultiChoiceMultiOptionQuestionContent(String instructionQuestion, String contextDescription, ArrayList<String> options) {
        this.instructionQuestion = instructionQuestion;
        this.contextDescription = contextDescription;
        this.options = options;
        this.responseContent = new ArrayList<String>();
        responseContent.add(0,"NaN");
    }

    public String getInstructionQuestion() {
        return instructionQuestion;
    }

    public ArrayList<String> getResponseContent() {
        return responseContent;
    }

    private void addResponse(String content) {
        Log.w(getClass().getSimpleName(), "Set to " + content);
        responseContent.add(content);
    }

    private void removeResponse(String content) {
        Log.w(getClass().getSimpleName(), "Remove " + content);
        responseContent.remove(content);
    }

    public void toggleResponse(String content) {
        if(responseContent.contains(content)) {
            removeResponse(content);
            if(responseContent.size() == 0) {
                responseContent.add(0,"NaN");
            }
        } else {
            if(responseContent.contains("NaN")){
                responseContent.remove("NaN");
            }
            addResponse(content);
        }
    }

    public void clearResponseContent() {
        Log.w(getClass().getSimpleName(),"Removed " + responseContent);
        responseContent = new ArrayList<String>();
        responseContent.add(0,"NaN");
    }

    public String getContextDescription() {
        return contextDescription;
    }

    public ArrayList<String> getOptions() {
        return options;
    }
}
