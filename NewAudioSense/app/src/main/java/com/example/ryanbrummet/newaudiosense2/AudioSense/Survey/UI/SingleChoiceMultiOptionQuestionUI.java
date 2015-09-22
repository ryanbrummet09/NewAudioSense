package com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.UI;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSurveySubComponent;
import com.example.ryanbrummet.newaudiosense2.R;

import java.util.ArrayList;

import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSingleSelectionSubComponent;
import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content.SingleChoiceMultiOptionQuestionContent;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.SingleSelectionButtonGroup;

/**
 * Created by ryanbrummet on 8/25/15.
 */
public class SingleChoiceMultiOptionQuestionUI extends AbstractSingleSelectionSubComponent {

    private SingleSelectionButtonGroup buttonGroup;
    private final boolean omitDescription;

    public SingleChoiceMultiOptionQuestionUI(String id, SingleChoiceMultiOptionQuestionContent content,
                                             AbstractSurveySubComponent[] subComponents,
                                             int[][] subComponentExecutePaths, boolean omitDescription){
        super(id, content, subComponents,subComponentExecutePaths);
        this.omitDescription = omitDescription;
    }

    public SingleChoiceMultiOptionQuestionUI(String id, SingleChoiceMultiOptionQuestionContent content, boolean omitDescription) {
        super(id, content);
        this.omitDescription = omitDescription;
    }

    public void render(Activity activity, Display display, ViewGroup rootView) {
        rootView.removeAllViews();

        if(omitDescription) {

            // add empty space to top of view
            LinearLayout.LayoutParams paramFirstEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3 * display.getHeight() / 160);
            TextView firstEmptySpace = new TextView(activity);
            rootView.addView(firstEmptySpace,paramFirstEmptySpace);

            // add question
            LinearLayout.LayoutParams paramQuestion = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, display.getHeight() / 10);
            TextView question = StaticUIComponents.blueInstructionTextView(activity, getContent().getInstructionQuestion(), 20);
            rootView.addView(question, paramQuestion);

            LinearLayout.LayoutParams paramSecondEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3 * display.getHeight() / 160);
            TextView secondEmptySpace = new TextView(activity);
            rootView.addView(secondEmptySpace,paramSecondEmptySpace);

            if (buttonGroup == null) {
                ArrayList<Button> buttons = new ArrayList<Button>();
                for (int i = 0; i < ((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().size(); i++) {
                    Button optionButton = new Button(activity);
                    optionButton.setBackgroundResource(R.drawable.bluebutton);
                    optionButton.setTextColor(Color.WHITE);
                    optionButton.setTextSize(15);
                    optionButton.setText(((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().get(i));
                    optionButton.setTypeface(null, Typeface.BOLD);
                    optionButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    optionButton.setOnClickListener(new ChangeSelection());
                    RelativeLayout.LayoutParams paramOptionButton = new RelativeLayout.LayoutParams(9 * display.getWidth() / 10,
                            (9 * (5 * display.getHeight() / (((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().size() * 10))) / 10);
                    paramOptionButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    RelativeLayout buttonRelativeLayout = new RelativeLayout(activity);
                    buttonRelativeLayout.addView(optionButton, paramOptionButton);

                    TextView emptySpace = new TextView(activity);
                    LinearLayout.LayoutParams paramEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            (5 * display.getHeight() / (((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().size() * 10)) / 10);

                    rootView.addView(emptySpace, paramEmptySpace);
                    rootView.addView(buttonRelativeLayout);

                    buttons.add(optionButton);
                }
                buttonGroup = new SingleSelectionButtonGroup(buttons, R.drawable.greenbutton);
            } else {
                for (int i = 0; i < buttonGroup.getNumberOfButtonsInGroup(); i++) {
                    RelativeLayout.LayoutParams paramOptionButton = new RelativeLayout.LayoutParams(9 * display.getWidth() / 10,
                            (9 * (5 * display.getHeight() / (((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().size() * 10))) / 10);
                    paramOptionButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    RelativeLayout buttonRelativeLayout = new RelativeLayout(activity);
                    ((ViewGroup) buttonGroup.getButtonInGroup(i).getParent()).removeView(buttonGroup.getButtonInGroup(i));
                    buttonRelativeLayout.addView(buttonGroup.getButtonInGroup(i), paramOptionButton);

                    TextView emptySpace = new TextView(activity);
                    LinearLayout.LayoutParams paramEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            (5 * display.getHeight() / (((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().size() * 10)) / 10);

                    rootView.addView(emptySpace, paramEmptySpace);
                    rootView.addView(buttonRelativeLayout);
                }
            }

            // add final empty space
            TextView finalEmptySpace = new TextView(activity);
            LinearLayout.LayoutParams paramFinalEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, display.getHeight() / 10);
            rootView.addView(finalEmptySpace, paramFinalEmptySpace);

        } else {

            // add context description and param
            TextView description = StaticUIComponents.redInstructionTextView(activity, ((SingleChoiceMultiOptionQuestionContent) getContent()).getContextDescription(), 20);
            LinearLayout.LayoutParams paramDescription = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, display.getHeight() / 20);
            rootView.addView(description, paramDescription);

            // add space between description and question
            TextView topEmptySpace = new TextView(activity);
            LinearLayout.LayoutParams paramTopEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    display.getHeight() / 160);
            rootView.addView(topEmptySpace, paramTopEmptySpace);

            // add question and param
            TextView question = StaticUIComponents.blueInstructionTextView(activity, getContent().getInstructionQuestion(), 20);
            LinearLayout.LayoutParams paramQuestion = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 7 * display.getHeight() / 40);
            rootView.addView(question, paramQuestion);


            // add options and params for each.  All options must fill .5 of screen
            if (buttonGroup == null) {
                ArrayList<Button> buttons = new ArrayList<Button>();
                for (int i = 0; i < ((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().size(); i++) {
                    Button optionButton = new Button(activity);
                    optionButton.setBackgroundResource(R.drawable.bluebutton);
                    optionButton.setTextColor(Color.WHITE);

                    // this is only for the audiology study and can be removed
                    if(((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().size() == 2) {
                        optionButton.setTextSize(30);
                    } else {
                        optionButton.setTextSize(15);
                    }

                    optionButton.setText(((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().get(i));
                    optionButton.setTypeface(null, Typeface.BOLD);
                    optionButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    optionButton.setOnClickListener(new ChangeSelection());
                    RelativeLayout.LayoutParams paramOptionButton = new RelativeLayout.LayoutParams(2 * display.getWidth() / 3,
                            (9 * (5 * display.getHeight() / (((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().size() * 10))) / 10);
                    paramOptionButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    RelativeLayout buttonRelativeLayout = new RelativeLayout(activity);
                    buttonRelativeLayout.addView(optionButton, paramOptionButton);

                    TextView emptySpace = new TextView(activity);
                    LinearLayout.LayoutParams paramEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            (5 * display.getHeight() / (((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().size() * 10)) / 10);

                    rootView.addView(emptySpace, paramEmptySpace);
                    rootView.addView(buttonRelativeLayout);

                    buttons.add(optionButton);
                }
                buttonGroup = new SingleSelectionButtonGroup(buttons, R.drawable.greenbutton);
            } else {
                for (int i = 0; i < buttonGroup.getNumberOfButtonsInGroup(); i++) {
                    RelativeLayout.LayoutParams paramOptionButton = new RelativeLayout.LayoutParams(2 * display.getWidth() / 3,
                            (9 * (5 * display.getHeight() / (((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().size() * 10))) / 10);
                    paramOptionButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    RelativeLayout buttonRelativeLayout = new RelativeLayout(activity);
                    ((ViewGroup) buttonGroup.getButtonInGroup(i).getParent()).removeView(buttonGroup.getButtonInGroup(i));
                    buttonRelativeLayout.addView(buttonGroup.getButtonInGroup(i), paramOptionButton);

                    TextView emptySpace = new TextView(activity);
                    LinearLayout.LayoutParams paramEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            (5 * display.getHeight() / (((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().size() * 10)) / 10);

                    rootView.addView(emptySpace, paramEmptySpace);
                    rootView.addView(buttonRelativeLayout);
                }
            }

            // add empty space
            TextView finalEmptySpace = new TextView(activity);
            LinearLayout.LayoutParams paramFinalEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 25 * display.getHeight() / 1000);
            rootView.addView(finalEmptySpace, paramFinalEmptySpace);

        }

        // add two direction navPanel and param
        RelativeLayout navPanel = StaticUIComponents.twoDirectionNavBar(activity,
                new PrevScreenSurveyListener(),
                new NextScreenSurveyListener(activity),
                "Back","Save and next", false, display);
        LinearLayout.LayoutParams paramNavPanel = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        rootView.addView(navPanel,paramNavPanel);

    }


    public void resetUI() {
        buttonGroup = null;
        getContent().removeFromResponseContent();
    }

    private class NextScreenSurveyListener implements View.OnClickListener {

        private Activity activity;

        public NextScreenSurveyListener(Activity activity) {
            super();
            this.activity = activity;
        }

        @Override
        public void onClick(View view) {
            try {
                getContent().setResponse((String) buttonGroup.getSelectedButton().getText(),
                        buttonGroup.getIndexOfSelectedButton());
                //buttonGroup.resetBackground();
                gotoNextComponent();
            } catch (NullPointerException e) {
                Toast.makeText(activity, "Please select an option", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class PrevScreenSurveyListener implements View.OnClickListener {

        public PrevScreenSurveyListener() {
            super();
        }

        @Override
        public void onClick(View view) {
            //buttonGroup.resetBackground();
            resetUI();
            gotoPreviousComponent();
        }
    }

    private class ChangeSelection implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            buttonGroup.selectButton((Button) view);
        }
    }
}
