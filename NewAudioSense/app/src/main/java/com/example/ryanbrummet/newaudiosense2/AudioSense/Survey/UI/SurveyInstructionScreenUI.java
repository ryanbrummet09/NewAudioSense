package com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.UI;

import android.app.Activity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSingleSelectionSubComponent;
import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content.SurveyInstructionScreenContent;

/**
 * Created by ryanbrummet on 8/25/15.
 */
public class SurveyInstructionScreenUI extends AbstractSingleSelectionSubComponent {

    public SurveyInstructionScreenUI(String id, SurveyInstructionScreenContent content) {
        super(id,content);
    }

    public void render(Activity activity, Display display, ViewGroup rootView){
        rootView.removeAllViews();

        // create spacing params
        LinearLayout.LayoutParams paramTopSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,display.getHeight() / 10);
        LinearLayout.LayoutParams paramInstructions = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,11 * display.getHeight() / 20);
        LinearLayout.LayoutParams paramMidSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,display.getHeight() / 10);
        //LinearLayout.LayoutParams paramNavPanel = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,display.getHeight() / 10);
        LinearLayout.LayoutParams paramNavPanel = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // create empty space to be placed at top of rootView
        TextView topEmptySpace = new TextView(activity);

        // create red instruction TextView
        TextView instructions = StaticUIComponents.redInstructionTextView(activity, getContent().getInstructionQuestion(), 26);

        // create empty space to be placed between instructions and the start button
        TextView midEmptySpace = new TextView(activity);

        // create single direction navigation panel
        RelativeLayout navPanel = StaticUIComponents.oneDirectionNavBar(activity, new StartSurveyListener(),
                "Start", display);

        // Add all created Views to rootView
        rootView.addView(topEmptySpace,paramTopSpace);
        rootView.addView(instructions,paramInstructions);
        rootView.addView(midEmptySpace,paramMidSpace);
        rootView.addView(navPanel,paramNavPanel);
    }

    public void resetUI(){}

    private class StartSurveyListener implements View.OnClickListener {

        public StartSurveyListener() {
        }

        @Override
        public void onClick(View view) {
            ((SurveyInstructionScreenContent) getContent()).recordSurveyStartInfo();
            Log.i("SurveyInstructionScreenUI", "Survey Started");
            gotoNextComponent();
        }
    }
}
