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

import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content.SingleChoiceMultiOptionQuestionContent;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSingleSelectionSubComponent;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSurveySubComponent;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.SingleSelectionButtonGroup;
import com.example.ryanbrummet.newaudiosense2.R;

import java.util.ArrayList;

/**
 * Created by ryanbrummet on 8/31/15.
 */
public class SingleChoiceTwoColumnMultiOptionQuestionUI extends AbstractSingleSelectionSubComponent {

    private SingleSelectionButtonGroup buttonGroup;
    private final boolean omitDescription;

    // last sub component in subComponent will stretch across both columns and have a different color button
    public SingleChoiceTwoColumnMultiOptionQuestionUI(String id, SingleChoiceMultiOptionQuestionContent content,
                                             AbstractSurveySubComponent[] subComponents,
                                             int[][] subComponentExecutePaths, boolean omitDescription){
        super(id, content, subComponents,subComponentExecutePaths);
        this.omitDescription = omitDescription;

        /*
        if(subComponents.length != 11) {
            throw new RuntimeException("This is a specific implementation class used specifically for AudioSense2.  For this implementation the number " +
                    "of subComponents must be 11.");
        }*/
    }

    public SingleChoiceTwoColumnMultiOptionQuestionUI(String id, SingleChoiceMultiOptionQuestionContent content, boolean omitDescription) {
        super(id, content);
        this.omitDescription = omitDescription;

        throw new RuntimeException("This is a specific implementation class used specifically for AudioSense2.  For this implementation sub components " +
                "must be given.  This constructor exists solely to satisfy requirements of the parent abstract class");
    }

    public void render(Activity activity, Display display, ViewGroup rootView) {
        rootView.removeAllViews();

        // add first empty space
        LinearLayout.LayoutParams paramFirstEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3 * display.getHeight() / 160);
        TextView firstEmptySpace = new TextView(activity);
        rootView.addView(firstEmptySpace,paramFirstEmptySpace);

        // add question
        LinearLayout.LayoutParams paramQuestion = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, display.getHeight() / 10);
        TextView question = StaticUIComponents.blueInstructionTextView(activity, getContent().getInstructionQuestion(), 20);
        rootView.addView(question,paramQuestion);

        // add second empty space
        LinearLayout.LayoutParams paramSecondEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3 * display.getHeight() / 160);
        TextView secondEmptySpace = new TextView(activity);
        rootView.addView(secondEmptySpace, paramSecondEmptySpace);

        if (buttonGroup == null) {
            ArrayList<Button> buttons = new ArrayList<Button>();
            for (int i = 0; i < ((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().size() - 1; i=i+2) {
                Button leftOptionButton = new Button(activity);
                leftOptionButton.setBackgroundResource(R.drawable.bluebutton);
                leftOptionButton.setTextColor(Color.WHITE);
                leftOptionButton.setTextSize(15);
                leftOptionButton.setText(((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().get(i));
                leftOptionButton.setTypeface(null, Typeface.BOLD);
                leftOptionButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                leftOptionButton.setOnClickListener(new ChangeSelection());

                Button rightOptionButton = new Button(activity);
                rightOptionButton.setBackgroundResource(R.drawable.bluebutton);
                rightOptionButton.setTextColor(Color.WHITE);
                rightOptionButton.setTextSize(15);
                rightOptionButton.setText(((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().get(i + 1));
                rightOptionButton.setTypeface(null, Typeface.BOLD);
                rightOptionButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                rightOptionButton.setOnClickListener(new ChangeSelection());

                TextView leftWidthSpace = new TextView(activity);
                TextView rightWidthSpace = new TextView(activity);

                RelativeLayout.LayoutParams paramLeftWidthSpace = new RelativeLayout.LayoutParams(3 * display.getWidth() / 100, 7 * display.getHeight() / 80);
                paramLeftWidthSpace.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                RelativeLayout.LayoutParams paramLeftOptionButton = new RelativeLayout.LayoutParams(46 * display.getWidth() / 100, 7 * display.getHeight() / 80);
                paramLeftOptionButton.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                RelativeLayout.LayoutParams paramRightWidthSpace = new RelativeLayout.LayoutParams(3 * display.getWidth() / 100, 7 * display.getHeight() / 80);
                paramRightWidthSpace.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                RelativeLayout.LayoutParams paramRightOptionButon = new RelativeLayout.LayoutParams(46 * display.getWidth() / 100, 7 * display.getHeight() / 80);
                paramRightOptionButon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                RelativeLayout buttonContainer = new RelativeLayout(activity);
                buttonContainer.addView(leftWidthSpace, paramLeftWidthSpace);
                buttonContainer.addView(leftOptionButton, paramLeftOptionButton);
                buttonContainer.addView(rightWidthSpace, paramRightWidthSpace);
                buttonContainer.addView(rightOptionButton, paramRightOptionButon);

                LinearLayout.LayoutParams paramButtonContainer = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                rootView.addView(buttonContainer, paramButtonContainer);

                LinearLayout.LayoutParams paramBottomButtonSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, display.getHeight() / 80);
                TextView bottomButtonSpace = new TextView(activity);
                rootView.addView(bottomButtonSpace, paramBottomButtonSpace);

                buttons.add(leftOptionButton);
                buttons.add(rightOptionButton);
            }

            Button lastButton = new Button(activity);
            lastButton.setBackgroundResource(R.drawable.darkbluebutton);
            lastButton.setTextColor(Color.WHITE);
            lastButton.setTextSize(15);
            lastButton.setText(((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().get(((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().size() - 1));
            lastButton.setTypeface(null, Typeface.BOLD);
            lastButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            lastButton.setOnClickListener(new ChangeSelection());

            RelativeLayout buttonContainer = new RelativeLayout(activity);
            RelativeLayout.LayoutParams paramLastButton = new RelativeLayout.LayoutParams(96 * display.getWidth() / 100, 7 * display.getHeight() / 80);
            paramLastButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
            buttonContainer.addView(lastButton, paramLastButton);
            LinearLayout.LayoutParams paramButtonContainer = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            rootView.addView(buttonContainer,paramButtonContainer);

            LinearLayout.LayoutParams paramFinalSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1 * display.getHeight() / 40);
            TextView finalSpace = new TextView(activity);
            rootView.addView(finalSpace,paramFinalSpace);

            // add two direction navPanel and param
            RelativeLayout navPanel = StaticUIComponents.twoDirectionNavBar(activity,
                    new PrevScreenSurveyListener(),
                    new NextScreenSurveyListener(activity),
                    "Back","Save and next", false, display);
            LinearLayout.LayoutParams paramNavPanel = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            rootView.addView(navPanel,paramNavPanel);

            buttons.add(lastButton);
            buttonGroup = new SingleSelectionButtonGroup(buttons, R.drawable.greenbutton);

        } else {
            for (int i = 0; i < ((SingleChoiceMultiOptionQuestionContent) getContent()).getOptions().size() - 1; i=i+2) {
                TextView leftWidthSpace = new TextView(activity);
                TextView rightWidthSpace = new TextView(activity);

                ((ViewGroup) buttonGroup.getButtonInGroup(i).getParent()).removeView(buttonGroup.getButtonInGroup(i));
                ((ViewGroup) buttonGroup.getButtonInGroup(i + 1).getParent()).removeView(buttonGroup.getButtonInGroup(i + 1));

                RelativeLayout.LayoutParams paramLeftWidthSpace = new RelativeLayout.LayoutParams(3 * display.getWidth() / 100, 7 * display.getHeight() / 80);
                paramLeftWidthSpace.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                RelativeLayout.LayoutParams paramLeftOptionButton = new RelativeLayout.LayoutParams(46 * display.getWidth() / 100, 7 * display.getHeight() / 80);
                paramLeftOptionButton.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                RelativeLayout.LayoutParams paramRightWidthSpace = new RelativeLayout.LayoutParams(3 * display.getWidth() / 100, 7 * display.getHeight() / 80);
                paramRightWidthSpace.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                RelativeLayout.LayoutParams paramRightOptionButon = new RelativeLayout.LayoutParams(46 * display.getWidth() / 100, 7 * display.getHeight() / 80);
                paramRightOptionButon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                RelativeLayout buttonContainer = new RelativeLayout(activity);
                buttonContainer.addView(leftWidthSpace, paramLeftWidthSpace);
                buttonContainer.addView(buttonGroup.getButtonInGroup(i), paramLeftOptionButton);
                buttonContainer.addView(rightWidthSpace, paramRightWidthSpace);
                buttonContainer.addView(buttonGroup.getButtonInGroup(i + 1), paramRightOptionButon);

                LinearLayout.LayoutParams paramButtonContainer = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                rootView.addView(buttonContainer, paramButtonContainer);

                LinearLayout.LayoutParams paramBottomButtonSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, display.getHeight() / 80);
                TextView bottomButtonSpace = new TextView(activity);
                rootView.addView(bottomButtonSpace, paramBottomButtonSpace);
            }

            ((ViewGroup) buttonGroup.getButtonInGroup(buttonGroup.getNumberOfButtonsInGroup() - 1).getParent()).removeView(buttonGroup.getButtonInGroup(buttonGroup.getNumberOfButtonsInGroup() - 1));

            RelativeLayout buttonContainer = new RelativeLayout(activity);
            RelativeLayout.LayoutParams paramLastButton = new RelativeLayout.LayoutParams(96 * display.getWidth() / 100, 7 * display.getHeight() / 80);
            paramLastButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
            buttonContainer.addView(buttonGroup.getButtonInGroup(buttonGroup.getNumberOfButtonsInGroup() - 1), paramLastButton);
            LinearLayout.LayoutParams paramButtonContainer = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            rootView.addView(buttonContainer,paramButtonContainer);

            LinearLayout.LayoutParams paramFinalSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1 * display.getHeight() / 40);
            TextView finalSpace = new TextView(activity);
            rootView.addView(finalSpace,paramFinalSpace);

            // add two direction navPanel and param
            RelativeLayout navPanel = StaticUIComponents.twoDirectionNavBar(activity,
                    new PrevScreenSurveyListener(),
                    new NextScreenSurveyListener(activity),
                    "Back","Save and next", false, display);
            LinearLayout.LayoutParams paramNavPanel = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            rootView.addView(navPanel,paramNavPanel);

        }
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
