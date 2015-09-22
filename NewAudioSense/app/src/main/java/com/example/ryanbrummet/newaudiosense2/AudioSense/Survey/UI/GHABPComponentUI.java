package com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.UI;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content.GHABPComponentContent;
import com.example.ryanbrummet.newaudiosense2.R;

import java.util.ArrayList;

import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSingleSelectionSubComponent;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSurveySubComponent;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.SingleSelectionButtonGroup;

/**
 * Created by ryanbrummet on 8/25/15.
 */
public class GHABPComponentUI extends AbstractSingleSelectionSubComponent {

    private SingleSelectionButtonGroup buttonGroup;
    private Button yesButton;
    private Button noButton;
    private final boolean omitDescription;

    public GHABPComponentUI(String id, GHABPComponentContent content,
                            AbstractSurveySubComponent[] subComponents,
                            int[][] subComponentExecutePaths, boolean omitDescription) {
        super(id, content, subComponents, subComponentExecutePaths);
        this.omitDescription = omitDescription;
    }

    public void render(Activity activity, Display display, ViewGroup rootView) {

        // remove all current views from root view
        rootView.removeAllViews();

        if(omitDescription) {
            LinearLayout.LayoutParams paramFirstSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3 * display.getHeight() / 40);
            LinearLayout.LayoutParams paramInstruction = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3 * display.getHeight() / 10);

            // create first empty space
            TextView firstEmptySpace = new TextView(activity);
            rootView.addView(firstEmptySpace,paramFirstSpace);

            // create instruction/question panel
            TextView instructions = StaticUIComponents.blueInstructionTextView(activity, getContent().getInstructionQuestion(), 20);
            rootView.addView(instructions,paramInstruction);

        } else {
            // create spacing parameters.  Spaces are created top to bottom
            LinearLayout.LayoutParams paramFirstSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, display.getHeight() / 80);
            LinearLayout.LayoutParams paramInstruction = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 11 * display.getHeight() / 80);
            LinearLayout.LayoutParams paramSecondSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, display.getHeight() / 40);
            LinearLayout.LayoutParams paramContext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, display.getHeight() / 5);

            // create first empty space
            TextView firstEmptySpace = new TextView(activity);
            rootView.addView(firstEmptySpace,paramFirstSpace);

            // create instruction/question panel
            TextView instructions = StaticUIComponents.redInstructionTextView(activity, getContent().getInstructionQuestion(), 20);
            rootView.addView(instructions,paramInstruction);

            // create second empty space
            TextView secondEmptySpace = new TextView(activity);
            rootView.addView(secondEmptySpace, paramSecondSpace);

            // create context panel
            TextView context = StaticUIComponents.blueInstructionTextView(activity, ((GHABPComponentContent) getContent()).getContextDescription(), 20);
            rootView.addView(context, paramContext);

        }

        LinearLayout.LayoutParams paramThirdSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 5 * display.getHeight() / 80);
        RelativeLayout.LayoutParams paramYesButton = new RelativeLayout.LayoutParams(7 * display.getWidth() / 10, display.getHeight() / 10);
        paramYesButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams paramFourthSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, display.getHeight() / 20);
        RelativeLayout.LayoutParams paramNoButton = new RelativeLayout.LayoutParams(7 * display.getWidth() / 10, display.getHeight() / 10);
        paramNoButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams paramFifthSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 5 * display.getHeight() / 80);
        LinearLayout.LayoutParams paramNavPanel = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // create third empty space
        TextView thirdEmptySpace = new TextView(activity);

        // create yes Button
        if( buttonGroup == null) {
            yesButton = new Button(activity);
            yesButton.setBackgroundResource(R.drawable.bluebutton);
            yesButton.setTextColor(Color.WHITE);
            yesButton.setTextSize(22);
            yesButton.setText("YES");
            yesButton.setTypeface(null, Typeface.BOLD);
            yesButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            yesButton.setOnClickListener(new ChangeSelection());
            //RelativeLayout yesButtonRelativeLayout = new RelativeLayout(activity);
            //yesButtonRelativeLayout.addView(yesButton, paramYesButton);

            // create no Button
            noButton = new Button(activity);
            noButton.setBackgroundResource(R.drawable.bluebutton);
            noButton.setTextColor(Color.WHITE);
            noButton.setTextSize(22);
            noButton.setText("NO");
            noButton.setTypeface(null, Typeface.BOLD);
            noButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            noButton.setOnClickListener(new ChangeSelection());
            //RelativeLayout noButtonRelativeLayout = new RelativeLayout(activity);
            //noButtonRelativeLayout.addView(noButton, paramNoButton);

            // create SingleSelectionButtonGroup
            ArrayList<Button> buttons = new ArrayList<Button>();
            buttons.add(yesButton);
            buttons.add(noButton);
            buttonGroup = new SingleSelectionButtonGroup(buttons, R.drawable.greenbutton);  // second param must be in drawable
        } else {
            ((ViewGroup) yesButton.getParent()).removeView(yesButton);
            ((ViewGroup) noButton.getParent()).removeView(noButton);
        }

        RelativeLayout yesButtonRelativeLayout = new RelativeLayout(activity);
        yesButtonRelativeLayout.addView(yesButton, paramYesButton);
        RelativeLayout noButtonRelativeLayout = new RelativeLayout(activity);
        noButtonRelativeLayout.addView(noButton, paramNoButton);

        // create fourth empty space
        TextView fourthEmptySpace = new TextView(activity);

        // create fifth empty space
        TextView fifthEmptySpace = new TextView(activity);

        // create two direction nav panel
        RelativeLayout navPanel = StaticUIComponents.twoDirectionNavBar(activity,
                new PrevScreenSurveyListener(),
                new NextScreenSurveyListener(activity),
                "Back","Save and next", false, display);





        rootView.addView(thirdEmptySpace, paramThirdSpace);
        rootView.addView(yesButtonRelativeLayout);
        rootView.addView(fourthEmptySpace, paramFourthSpace);
        rootView.addView(noButtonRelativeLayout);
        rootView.addView(fifthEmptySpace, paramFifthSpace);
        rootView.addView(navPanel,paramNavPanel);

    }


    public void resetUI(){

        buttonGroup = null;
        getContent().removeFromResponseContent();
    }

    private class NextScreenSurveyListener implements View.OnClickListener {

        private final Activity activity;

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
