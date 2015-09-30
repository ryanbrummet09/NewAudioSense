package com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Vibrator;
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

import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content.MultiChoiceMultiOptionQuestionContent;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSurveySubComponent;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.MultiSelectionButtonGroup;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.RouteComponents;
import com.example.ryanbrummet.newaudiosense2.R;

import java.util.ArrayList;

/**
 * Created by ryanbrummet on 9/22/15.
 *
 * This class is a specific implementation for the AudioSense2 study.  Because of the difficult nature of
 * determining the execution path of a Component (given that it has children) that allows multi selection
 * a general implementation was not created.  DO NOT USE THIS CLASS UNDER THE ASSUMPTION THAT IT IS
 * GENERAL.  It has very specific behaviour related to aforementioned study.
 */
public class MultiChoiceMultiOptionQuestionUI extends AbstractSurveySubComponent {

    private MultiSelectionButtonGroup buttonGroup;
    private final boolean omitDescription;
    private final MultiChoiceMultiOptionQuestionContent content;
    public final AbstractSurveySubComponent subComponent;
    public ArrayList<AbstractSurveySubComponent> selectedSubComponents;
    private final int[][] subComponentExecutePaths;
    private int subComponentIndex; // zero indicates the component is the contextOccuranceUI.  Any other
    // value greater than zero subtract one to get the index in
    // singleChoiceMultiOptionUI
    private Vibrator vibrator;


    // int[][] subComponentExecutePaths: first dim is number of buttons and second dim is the size of subComponents
    public MultiChoiceMultiOptionQuestionUI(String id, int color, MultiChoiceMultiOptionQuestionContent content,
                                               AbstractSurveySubComponent subComponent,
                                               int[][] subComponentExecutePaths, boolean omitDescription) {
        super(id, color);
        this.content = content;
        this.subComponent = subComponent;
        this.subComponentExecutePaths = subComponentExecutePaths;
        selectedSubComponents = new ArrayList<AbstractSurveySubComponent>();
        subComponentIndex = -1;
        this.omitDescription = omitDescription;

    }

    public void route(Activity activity, Display display, ViewGroup rootView,
                      RouteComponents parentRenderComponents, int thisComponentIndex) {

        Log.i("AbstractSingleSelectionSubComponent", "Rendering AbstractSingleSelectionSubComponent " +
                Integer.toString(thisComponentIndex));

        super.route(activity, display, rootView, parentRenderComponents, thisComponentIndex);

        if (subComponent == null) {
            this.render(activity, display, rootView);
        } else {
            if (subComponentIndex < 0) {
                this.render(activity, display, rootView);
            } else {
                /*
                int[] subComponentsToRender = subComponentExecutePaths[content.getResponseIndex()];
                for (int i = 0; i < subComponentsToRender.length; i++) {
                    if (subComponentsToRender[i] == 1) {
                        selectedSubComponents.add(subComponents[i]);
                    }
                }*/
                RouteComponents childRenderComponents = new RouteComponents(activity, rootView, this,
                        selectedSubComponents);
                childRenderComponents.route(subComponentIndex);
            }
        }
    }

    public void render(Activity activity, Display display, ViewGroup rootView) {
        rootView.removeAllViews();
        rootView.setBackgroundColor(getColor());
        vibrator = (Vibrator) rootView.getContext().getSystemService(Context.VIBRATOR_SERVICE);

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
                for (int i = 0; i < (getContent()).getOptions().size(); i++) {
                    Button optionButton = new Button(activity);
                    optionButton.setBackgroundResource(R.drawable.bluebutton);
                    optionButton.setTextColor(Color.WHITE);
                    optionButton.setTextSize(20);
                    optionButton.setText(( getContent()).getOptions().get(i));
                    optionButton.setTypeface(null, Typeface.BOLD);
                    optionButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    optionButton.setOnClickListener(new ChangeSelection());
                    RelativeLayout.LayoutParams paramOptionButton = new RelativeLayout.LayoutParams(9 * display.getWidth() / 10,
                            (9 * (5 * display.getHeight() / ((getContent()).getOptions().size() * 10))) / 10);
                    paramOptionButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    RelativeLayout buttonRelativeLayout = new RelativeLayout(activity);
                    buttonRelativeLayout.addView(optionButton, paramOptionButton);

                    TextView emptySpace = new TextView(activity);
                    LinearLayout.LayoutParams paramEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            (5 * display.getHeight() / ((getContent()).getOptions().size() * 10)) / 10);

                    rootView.addView(emptySpace, paramEmptySpace);
                    rootView.addView(buttonRelativeLayout);

                    buttons.add(optionButton);
                }
                buttonGroup = new MultiSelectionButtonGroup(buttons, R.drawable.greenbutton);
            } else {
                for (int i = 0; i < buttonGroup.getNumberOfButtonsInGroup(); i++) {
                    RelativeLayout.LayoutParams paramOptionButton = new RelativeLayout.LayoutParams(9 * display.getWidth() / 10,
                            (9 * (5 * display.getHeight() / ((getContent()).getOptions().size() * 10))) / 10);
                    paramOptionButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    RelativeLayout buttonRelativeLayout = new RelativeLayout(activity);
                    ((ViewGroup) buttonGroup.getButtonInGroup(i).getParent()).removeView(buttonGroup.getButtonInGroup(i));
                    buttonRelativeLayout.addView(buttonGroup.getButtonInGroup(i), paramOptionButton);

                    TextView emptySpace = new TextView(activity);
                    LinearLayout.LayoutParams paramEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            (5 * display.getHeight() / ((getContent()).getOptions().size() * 10)) / 10);

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
            TextView description = StaticUIComponents.redInstructionTextView(activity, (getContent()).getContextDescription(), 20);
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
                for (int i = 0; i < (getContent()).getOptions().size(); i++) {
                    Button optionButton = new Button(activity);
                    optionButton.setBackgroundResource(R.drawable.bluebutton);
                    optionButton.setTextColor(Color.WHITE);

                    // this is only for the audiology study and can be removed
                    if((getContent()).getOptions().size() == 2) {
                        optionButton.setTextSize(30);
                    } else {
                        optionButton.setTextSize(20);
                    }

                    optionButton.setText((getContent()).getOptions().get(i));
                    optionButton.setTypeface(null, Typeface.BOLD);
                    optionButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    optionButton.setOnClickListener(new ChangeSelection());
                    RelativeLayout.LayoutParams paramOptionButton = new RelativeLayout.LayoutParams(9 * display.getWidth() / 10,
                            (9 * (5 * display.getHeight() / ((getContent()).getOptions().size() * 10))) / 10);
                    paramOptionButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    RelativeLayout buttonRelativeLayout = new RelativeLayout(activity);
                    buttonRelativeLayout.addView(optionButton, paramOptionButton);

                    TextView emptySpace = new TextView(activity);
                    LinearLayout.LayoutParams paramEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            (5 * display.getHeight() / ((getContent()).getOptions().size() * 10)) / 10);

                    rootView.addView(emptySpace, paramEmptySpace);
                    rootView.addView(buttonRelativeLayout);

                    buttons.add(optionButton);
                }
                buttonGroup = new MultiSelectionButtonGroup(buttons, R.drawable.greenbutton);
            } else {
                for (int i = 0; i < buttonGroup.getNumberOfButtonsInGroup(); i++) {
                    RelativeLayout.LayoutParams paramOptionButton = new RelativeLayout.LayoutParams(9 * display.getWidth() / 10,
                            (9 * (5 * display.getHeight() / ((getContent()).getOptions().size() * 10))) / 10);
                    paramOptionButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    RelativeLayout buttonRelativeLayout = new RelativeLayout(activity);
                    ((ViewGroup) buttonGroup.getButtonInGroup(i).getParent()).removeView(buttonGroup.getButtonInGroup(i));
                    buttonRelativeLayout.addView(buttonGroup.getButtonInGroup(i), paramOptionButton);

                    TextView emptySpace = new TextView(activity);
                    LinearLayout.LayoutParams paramEmptySpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            (5 * display.getHeight() / ((getContent()).getOptions().size() * 10)) / 10);

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

    public ArrayList<String> getResults() {

        ArrayList<String> results = new ArrayList<String>();
        if (subComponent != null) {
            results.addAll(subComponent.getResults());
        }
        ArrayList<String> temp = content.getResponseContent();
        String thisContent = "";
        for(int i = 0; i < temp.size(); i++) {
            if(i == 0) {
                thisContent = thisContent + getComponentID() + ":" + temp.get(i);
            } else {
                thisContent = thisContent + "," + temp.get(i);
            }
        }
        results.add(thisContent);
        return results;
    }

    public void resetUI() {
        buttonGroup = null;
        getContent().clearResponseContent();
    }

    public void gotoPreviousComponent(){
        Log.i(getClass().getSimpleName(),this.getComponentID() + " Moving to previous AbstractSingleSelectionSubComponent");
        if(subComponent == null) {
            getRouteComponents().route(getThisComponentIndex() - 1);
        } else {
            if (subComponentIndex >= 0) {
                subComponentIndex = -1;
                selectedSubComponents = new ArrayList<AbstractSurveySubComponent>();
                getRouteComponents().route(getThisComponentIndex());
            } else {
                getRouteComponents().route(getThisComponentIndex() - 1);
            }
        }
    }

    // this method is by no means general and is made only to work with the current AudioSense study
    public void gotoNextComponent(){
        Log.i(getClass().getSimpleName(), this.getComponentID() + " Moving to next AbstractSingleSelectionSubComponent");
        if(subComponent == null) {
            getRouteComponents().route(getThisComponentIndex() + 1);
        } else {
            if (subComponentIndex < 0) {
                ArrayList<String> responses = content.getResponseContent();
                for(int i = 0; i < responses.size(); i++) {
                    if(responses.get(i).equals("5")){
                        selectedSubComponents.add(subComponent);
                        break;
                    }
                }

                subComponentIndex++;
                getRouteComponents().route(getThisComponentIndex());
            } else {
                subComponentIndex = selectedSubComponents.size() - 1;
                getRouteComponents().route(getThisComponentIndex() + 1);
            }
        }
    }

    private class NextScreenSurveyListener implements View.OnClickListener {

        private Activity activity;

        public NextScreenSurveyListener(Activity activity) {
            super();
            this.activity = activity;
        }

        @Override
        public void onClick(View view) {
            if(buttonGroup.getSelectedButtons().size() == 0) {
                Toast.makeText(activity, "Please select at least one option", Toast.LENGTH_SHORT).show();
            } else {
                vibrator.vibrate(250);
                gotoNextComponent();
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
            vibrator.vibrate(250);
            gotoPreviousComponent();
        }
    }

    private class ChangeSelection implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            buttonGroup.selectButton((Button) view);
            content.toggleResponse(Integer.toString(buttonGroup.getIndexOfButton((Button) view)));
        }
    }

    public MultiChoiceMultiOptionQuestionContent getContent() {
        return content;
    }

}


