package com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content;

import android.util.Log;
import android.view.ViewGroup;

import com.example.ryanbrummet.newaudiosense2.AudioSense.Main.AudioSenseActivity;
import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.UI.EndSurveyScreenUI;
import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.UI.GHABPComponentUI;
import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.UI.SingleChoiceMultiOptionQuestionUI;
import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.UI.SingleChoiceTwoColumnMultiOptionQuestionUI;
import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.UI.SurveyInstructionScreenUI;
import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.UI.UserInputInfoUI;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSingleSelectionSubComponent;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSurvey;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSurveySubComponent;

import java.util.Calendar;
import java.util.ArrayList;

/**
 * Created by ryanbrummet on 8/21/15.
 *
 * c1: "TV/Volume set for others"
 * c2: "One-on-one conversation / Quiet"
 * c3: "Conversation / busy street or shop"
 * c4: "Conversation / Group"
 *
 * q1: "In this situation, what proportion of the time did you wear your hearing aid?"
 * q2: "In this situation, how much did your hearing aid (HA) help you?"
 * q3: "In this situation, with your hearing aid, how much difficulty did you have?"
 * q4: "In this situation, with your hearing aid, how much did any difficulty in this situation worry, annoy or upset you?"
 * q5: "In this situation, how satisfied were you with your hearing aid?"
 * q6: "You were not 100% satisfied with your hearing aid in this situation.  Please indicate why."
 * q7: "Is this situation happening now?"
 */
public class AudioSense2 extends AbstractSurvey {

    private final AudioSenseActivity activity;
    private final ViewGroup rootView;

    public AudioSense2(String id, int surveyInterval, AudioSenseActivity activity, ViewGroup rootView) {

        super(id, surveyInterval);
        this.activity = activity;
        this.rootView = rootView;

        Log.i("AudioSense2", "New survey created");

        // add instruction screen
        String time;
        int hour = (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) % 12;
        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - getSurveyInterval() / 12 == 0) {
            if(Calendar.getInstance().get(Calendar.MINUTE) < 10) {
                time = Integer.toString(hour) + ":0" + Integer.toString(Calendar.getInstance().get(Calendar.MINUTE)) + "am";
            } else {
                time = Integer.toString(hour) + ":" + Integer.toString(Calendar.getInstance().get(Calendar.MINUTE)) + "am";
            }
        } else {
            if(Calendar.getInstance().get(Calendar.MINUTE) < 10) {
                time = Integer.toString(hour) + ":0" + Integer.toString(Calendar.getInstance().get(Calendar.MINUTE)) + "pm";
            } else {
                time = Integer.toString(hour) + ":" + Integer.toString(Calendar.getInstance().get(Calendar.MINUTE)) + "pm";
            }
        }
        SurveyInstructionScreenContent surveyInstructionContent = new SurveyInstructionScreenContent(

                "It is now " + time + ". Please consider the past " + getSurveyInterval() + " hours and then answer the following questions");
        addToComponents(new SurveyInstructionScreenUI("startScreen", surveyInstructionContent));

        //Context Questions
        ArrayList<String> questions = new ArrayList<String>();
        ArrayList<ArrayList<String>> questionOptions = new ArrayList<ArrayList<String>>();
        ArrayList<String> questionIDs = new ArrayList<String>();

        // Question 1
        ArrayList<String> q1Options = new ArrayList<String>(5);
        q1Options.add("Never/Not at all");
        q1Options.add("About 1/4 of the time");
        q1Options.add("About 1/2 of the time");
        q1Options.add("About 3/4 of the time");
        q1Options.add("All the time");
        questions.add("In this situation, what proportion of the time did you wear your hearing aid?");
        questionOptions.add(q1Options);
        questionIDs.add("q1");

        // Question 2
        ArrayList<String> q2Options = new ArrayList<String>(5);
        q2Options.add("HA no use at all");
        q2Options.add("HA is some help");
        q2Options.add("HA is quite helpful");
        q2Options.add("HA is a great help");
        q2Options.add("Hearing is perfect with HA");
        questions.add("In this situation, how much did your hearing aid (HA) help you?");
        questionOptions.add(q2Options);
        questionIDs.add("q2");

        //Question 3
        ArrayList<String> q3Options = new ArrayList<String>(5);
        q3Options.add("No difficulty");
        q3Options.add("Only slight difficulty");
        q3Options.add("Moderate difficulty");
        q3Options.add("Great difficulty");
        q3Options.add("Cannot manage at all");;
        questions.add("In this situation, with your hearing aid, how much difficulty did you have?");
        questionOptions.add(q3Options);
        questionIDs.add("q3");

        // Question 4
        ArrayList<String> q4Options = new ArrayList<String>(5);
        q4Options.add("Not at all");
        q4Options.add("Only a little");
        q4Options.add("A moderate amount");
        q4Options.add("Quite a lot");
        q4Options.add("Very much indeed");
        questions.add("In this situation, with your hearing aid, how much did any difficulty in this situation worry, annoy or upset you?");
        questionOptions.add(q4Options);
        questionIDs.add("q4");

        // Question 5
        ArrayList<String> q5Options = new ArrayList<String>(5);
        q5Options.add("Not satisfied at all");
        q5Options.add("A little satisfied");
        q5Options.add("Reasonably satisfied");
        q5Options.add("Very satisfied");
        q5Options.add("Delighted with hearing aid");
        questions.add("In this situation, how satisfied were you with your hearing aid?");
        questionOptions.add(q5Options);
        questionIDs.add("q5");

        // Question 6
        ArrayList<String> q6Options = new ArrayList<String>(6);
        q6Options.add("Difficulty understanding speech");
        q6Options.add("Discomfort from loud sounds");
        q6Options.add("Poor sound quality");
        q6Options.add("Internal static");
        q6Options.add("Whistling (feedback)");
        q6Options.add("Other");
        questions.add("You were not 100% satisfied with your hearing aid in this situation.  Please indicate why.");
        questionOptions.add(q6Options);
        questionIDs.add("q6");

        // Question 7
        ArrayList<String> q7Options = new ArrayList<String>(2);
        q7Options.add("Yes");
        q7Options.add("No");
        questions.add("Is this situation happening now?");
        questionOptions.add(q7Options);
        questionIDs.add("q7");

        //Context Occurrence question
        String instruction = "Did this situation happen in the past " + surveyInterval + " hours?";

        // context ids
        ArrayList<String> contextIDs = new ArrayList<String>();
        contextIDs.add("c1");
        contextIDs.add("c2");
        contextIDs.add("c3");
        contextIDs.add("c4");
        contextIDs.add("c5");

        // short hand descriptions of contexts
        ArrayList<String> contextShortDescriptions = new ArrayList<String>();
        contextShortDescriptions.add("TV/Volume set for others");
        contextShortDescriptions.add("One-on-one conversation/quiet");
        contextShortDescriptions.add("Conversation/busy street or shop");
        contextShortDescriptions.add("Conversation/Group");

        // long descriptions of contexts
        ArrayList<String> contextLongDescriptions = new ArrayList<String>();
        contextLongDescriptions.add("Listening to the TV with other family or friends when the volume is adjusted to suit other people");
        contextLongDescriptions.add("Having a conversation with one other person when there is no background noise");
        contextLongDescriptions.add("Carrying on conversation in a busy street or shop");
        contextLongDescriptions.add("Having a conversation with several people in a group");

        for(int i = 0; i < contextShortDescriptions.size(); i++) {
            AbstractSurveySubComponent[] subComponents = new AbstractSurveySubComponent[questionIDs.size() - 1];
            GHABPComponentContent tempGHABP = new GHABPComponentContent(instruction,contextLongDescriptions.get(i));
            for (int j = 0; j < questionIDs.size() - 1; j++) {
                SingleChoiceMultiOptionQuestionContent tempContent;
                if(j == 5) {
                    tempContent = new SingleChoiceMultiOptionQuestionContent(questions.get(j+1),
                            contextShortDescriptions.get(i),questionOptions.get(j+1));
                } else {
                    tempContent = new SingleChoiceMultiOptionQuestionContent(questions.get(j),
                            contextShortDescriptions.get(i), questionOptions.get(j));
                }
                if (j == 4) {
                    SingleChoiceMultiOptionQuestionContent tempContent5 = new SingleChoiceMultiOptionQuestionContent(questions.get(j + 1),
                            contextShortDescriptions.get(i),questionOptions.get(j + 1));

                    UserInputInfoContent tempContent5UserInputSubContent = new UserInputInfoContent(
                            "Other: Please briefly explain why you were not 100% satisfied in the situation " + contextShortDescriptions.get(i));
                    AbstractSurveySubComponent[] userInputInfoUIArray = new UserInputInfoUI[1];
                    userInputInfoUIArray[0] = new UserInputInfoUI(questionIDs.get(j + 1) + "U",tempContent5UserInputSubContent);

                    int[][] tempContentFiveExecutePath = new int[6][1];
                    for(int a = 0; a < 6; a++){
                        if(a != 5){
                            tempContentFiveExecutePath[a][0] = 0;
                        }else {
                            tempContentFiveExecutePath[a][0] = 1;
                        }
                    }

                    SingleChoiceMultiOptionQuestionUI tempUI5 = new SingleChoiceMultiOptionQuestionUI(questionIDs.get(j + 1),
                            tempContent5,userInputInfoUIArray,tempContentFiveExecutePath, false);
                    SingleChoiceMultiOptionQuestionUI[] UI5Array = new SingleChoiceMultiOptionQuestionUI[1];
                    UI5Array[0] = tempUI5;

                    int[][] tempContentFourExecutePath = new int[5][1];
                    for(int a = 0; a < 5; a++){
                        if(a != 4){
                            tempContentFourExecutePath[a][0] = 1;
                        }else {
                            tempContentFourExecutePath[a][0] = 0;
                        }
                    }

                    subComponents[j] = new SingleChoiceMultiOptionQuestionUI(questionIDs.get(j),tempContent,
                            UI5Array, tempContentFourExecutePath, false);


                } else {
                    if(j == 5) {
                        subComponents[j] = new SingleChoiceMultiOptionQuestionUI(questionIDs.get(j + 1),tempContent, false);
                    } else {
                        subComponents[j] = new SingleChoiceMultiOptionQuestionUI(questionIDs.get(j),tempContent, false);
                    }

                }

            }
            int[][] tempGHABPExecutePath = new int[2][questionIDs.size() -1];
            for(int j = 0; j < 2; j++){
                for(int a = 0; a < questionIDs.size() - 1; a++) {
                    if(j == 0) {
                        tempGHABPExecutePath[j][a] = 1;
                    } else {
                        tempGHABPExecutePath[j][a] = 0;
                    }
                }
            }
            //Log.e("here",Integer.toString(subComponents.length));
            addToComponents(new GHABPComponentUI(contextIDs.get(i), tempGHABP, subComponents, tempGHABPExecutePath, false));
        }

        // code for context 5
        UserInputInfoContent c5LocOtherContent = new UserInputInfoContent(
                "Others: Please briefly describe the location");
        UserInputInfoUI c5LocOtherUI = new UserInputInfoUI("c5LocOther",c5LocOtherContent);
        AbstractSurveySubComponent[] c5LocOtherUIArray = new AbstractSurveySubComponent[1];
        c5LocOtherUIArray[0] = c5LocOtherUI;

        // add to array left to right and then down (like you would read)
        ArrayList<String> tempLoc = new ArrayList<String>(11);
        tempLoc.add("Restaurant");
        tempLoc.add("While shopping");
        tempLoc.add("Place of worship");
        tempLoc.add("Car");
        tempLoc.add("Recreation / exercise");
        tempLoc.add("Movie / concert");
        tempLoc.add("Workplace");
        tempLoc.add("Sport events");
        tempLoc.add("School / classroom");
        tempLoc.add("Outdoors");
        tempLoc.add("Others");
        SingleChoiceMultiOptionQuestionContent c5LocContent = new SingleChoiceMultiOptionQuestionContent(
                "Please indicate the Location","",tempLoc, true);
        int[][] c5LocExecutePath = new int[11][1];
        for(int a = 0; a < 11; a++) {
            if(a == 10) {
                c5LocExecutePath[a][0] = 1;
            } else {
                c5LocExecutePath[a][0] = 0;
            }
        }

        SingleChoiceTwoColumnMultiOptionQuestionUI c5LocUI = new SingleChoiceTwoColumnMultiOptionQuestionUI(
                "c5Loc",c5LocContent,c5LocOtherUIArray,c5LocExecutePath,true);

        ArrayList<String> tempAct = new ArrayList<String>(5);
        tempAct.add("One-on-one conversation");
        tempAct.add("Group conversation");
        tempAct.add("Phone");
        tempAct.add("TV / Radio");
        tempAct.add("Non-speech sound listening");
        SingleChoiceMultiOptionQuestionContent c5ActContent = new SingleChoiceMultiOptionQuestionContent(
                "Please indicate the Activity","",tempAct);
        SingleChoiceMultiOptionQuestionUI c5ActUI = new SingleChoiceMultiOptionQuestionUI("c5Act",c5ActContent,true);

        AbstractSurveySubComponent[] subComponents = new AbstractSurveySubComponent[questionIDs.size() - 1];
        GHABPComponentContent tempGHABP = new GHABPComponentContent(instruction,"User Context");
        for (int j = 0; j < questionIDs.size() - 1; j++) {
            SingleChoiceMultiOptionQuestionContent tempContent;
            if(j == 5) {
                tempContent = new SingleChoiceMultiOptionQuestionContent(questions.get(j+1),
                        "User Context",questionOptions.get(j+1));
            } else {
                tempContent = new SingleChoiceMultiOptionQuestionContent(questions.get(j),
                        "User Context", questionOptions.get(j));
            }
            if (j == 4) {
                SingleChoiceMultiOptionQuestionContent tempContent5 = new SingleChoiceMultiOptionQuestionContent(questions.get(j + 1),
                        "User Context",questionOptions.get(j + 1));

                UserInputInfoContent tempContent5UserInputSubContent = new UserInputInfoContent(
                        "Other: Please briefly explain why you were not 100% satisfied in the situation " + "User Context");
                AbstractSurveySubComponent[] userInputInfoUIArray = new UserInputInfoUI[1];
                userInputInfoUIArray[0] = new UserInputInfoUI(questionIDs.get(j + 1) + "U",tempContent5UserInputSubContent);

                int[][] tempContentFiveExecutePath = new int[6][1];
                for(int a = 0; a < 6; a++){
                    if(a != 5){
                        tempContentFiveExecutePath[a][0] = 0;
                    }else {
                        tempContentFiveExecutePath[a][0] = 1;
                    }
                }

                SingleChoiceMultiOptionQuestionUI tempUI5 = new SingleChoiceMultiOptionQuestionUI(questionIDs.get(j + 1),
                        tempContent5,userInputInfoUIArray,tempContentFiveExecutePath, false);
                SingleChoiceMultiOptionQuestionUI[] UI5Array = new SingleChoiceMultiOptionQuestionUI[1];
                UI5Array[0] = tempUI5;

                int[][] tempContentFourExecutePath = new int[5][1];
                for(int a = 0; a < 5; a++){
                    if(a != 4){
                        tempContentFourExecutePath[a][0] = 1;
                    }else {
                        tempContentFourExecutePath[a][0] = 0;
                    }
                }

                subComponents[j] = new SingleChoiceMultiOptionQuestionUI(questionIDs.get(j),tempContent,
                        UI5Array, tempContentFourExecutePath, false);


            } else {
                if(j == 5) {
                    subComponents[j] = new SingleChoiceMultiOptionQuestionUI(questionIDs.get(j + 1),tempContent, false);
                } else {
                    subComponents[j] = new SingleChoiceMultiOptionQuestionUI(questionIDs.get(j),tempContent, false);
                }

            }

        }

        AbstractSingleSelectionSubComponent[] c5SubComponents = new AbstractSingleSelectionSubComponent[2];
        c5SubComponents[0] = c5ActUI;
        c5SubComponents[1] = c5LocUI;

        int aLen = c5SubComponents.length;
        int bLen = subComponents.length;
        AbstractSurveySubComponent[] components = new AbstractSurveySubComponent[aLen+bLen];
        System.arraycopy(c5SubComponents, 0, components, 0, aLen);
        System.arraycopy(subComponents, 0, components, aLen, bLen);

        int[][] tempGHABPExecutePath = new int[2][questionIDs.size() + 1];
        for(int j = 0; j < 2; j++){
            for(int a = 0; a < questionIDs.size() + 1; a++) {
                if(j == 0) {
                    tempGHABPExecutePath[j][a] = 1;
                } else {
                    tempGHABPExecutePath[j][a] = 0;
                }
            }
        }

        GHABPComponentContent c5Content = new GHABPComponentContent(
                "Would you like to nominate an additional listening situation that happened in the past " +
                        surveyInterval + " hours in which it was important for you to hear as well as possible?", "");
        GHABPComponentUI c5UI = new GHABPComponentUI("c5", c5Content, components, tempGHABPExecutePath, true);

        addToComponents(c5UI);

        EndSurveyScreenContent endScreenContent = new EndSurveyScreenContent("Survey Completed");
        EndSurveyScreenUI endScreen = new EndSurveyScreenUI("endScreen",endScreenContent);
        addToComponents(endScreen);
    }

    public void gotoPreviousComponent() {}

    public void gotoNextComponent() {
        Log.i("AudioSense2", "User has completed survey. Now on submit screen");
        activity.onSubmitSurvey(rootView);
    }
}
