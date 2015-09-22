package com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.UI;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.view.WindowManager;

import com.example.ryanbrummet.newaudiosense2.R;

/**
 * Created by ryanbrummet on 8/22/15.
 */
public class StaticUIComponents {

/*
    public static LinearLayout audioSense2RootLayout(Activity activity) {
        LinearLayout rootView = new LinearLayout(activity);
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setBackgroundColor(Color.BLACK);
        rootView.setPadding(12, 0, 12, 0);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return rootView;
    }
*/

    /*
    Creates prev and next buttons that are scaled based on the size of the screen.  Additionally
    attaches the passed listeners to the respected buttons.
    */
    public static RelativeLayout twoDirectionNavBar(Activity activity, View.OnClickListener prevButtonListener,
                                                       View.OnClickListener nextButtonListener, String prevText,
                                                       String nextText, boolean submit, Display display) {

        // Make navigation container
        RelativeLayout navigationContainer = new RelativeLayout(activity);

        // navigationContainer Params
        RelativeLayout.LayoutParams nextButtonParams = new RelativeLayout.LayoutParams(15 * display.getWidth() / 24,display.getHeight() / 10);
        nextButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        RelativeLayout.LayoutParams backButtonParams = new RelativeLayout.LayoutParams(5 * display.getWidth() / 24,display.getHeight() / 10);
        backButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        // next button
        Button nextButton = new Button(activity);
        if(submit) {
            nextButton.setBackgroundResource(R.drawable.submitbutton);
        } else {
            nextButton.setBackgroundResource(R.drawable.redbutton);
        }
        nextButton.setTextColor(Color.WHITE);
        nextButton.setTextSize(22);
        nextButton.setText(nextText);
        nextButton.setTypeface(null, Typeface.BOLD);
        nextButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        nextButton.setOnClickListener(nextButtonListener);

        // back button
        Button backButton = new Button(activity);
        backButton.setBackgroundResource(R.drawable.redbutton);
        backButton.setTextColor(Color.WHITE);
        backButton.setTextSize(22);
        backButton.setText(prevText);
        backButton.setTypeface(null, Typeface.BOLD);
        backButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        backButton.setOnClickListener(prevButtonListener);

        // add components to navigationContainer
        navigationContainer.addView(nextButton,nextButtonParams);
        navigationContainer.addView(backButton,backButtonParams);

        return navigationContainer;
    }

    /*
    Creates a single button that is similar to the next button created by the addTwoDirectionNavBar.
    This addition is used to add a nav bar that allows navigation only in one direction.
     */
    public static RelativeLayout oneDirectionNavBar(Activity activity, View.OnClickListener buttonListener,
                                                       String buttonText, Display display) {

        // Make navigation container
        RelativeLayout navigationContainer = new RelativeLayout(activity);

        // navigationContainer Params
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(15 * display.getWidth() / 24,display.getHeight() / 10);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        // next button
        Button button = new Button(activity);
        button.setBackgroundResource(R.drawable.redbutton);
        button.setTextColor(Color.WHITE);
        button.setTextSize(22);
        button.setText(buttonText);
        button.setTypeface(null, Typeface.BOLD);
        button.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        button.setOnClickListener(buttonListener);

        navigationContainer.addView(button,buttonParams);

        return navigationContainer;
    }

    /*
    Creates red instruction/question text field.  The size of the field must be specified when the
    returned TextView is added to a container (ie LinearLayout, RelativeLayout, etc)
    */
    public static TextView redInstructionTextView(Activity activity, String text, int fontSize) {
        TextView instructions = new TextView(activity);
        instructions.setText(text);
        instructions.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        instructions.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.redinstruction));
        instructions.setTextColor(Color.BLACK);
        instructions.setTextSize(fontSize);
        instructions.setTypeface(null, Typeface.BOLD);

        return instructions;
    }

    /*
    Creates blue instruction/question text field.  The size of the field must be specified when the
    returned TextView is added to a container (ie LinearLayout, RelativeLayout, etc)
     */
    public static TextView blueInstructionTextView(Activity activity, String text, int fontSize) {
        TextView instructions = new TextView(activity);
        instructions.setText(text);
        instructions.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        instructions.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.blueinstruction));
        instructions.setTextColor(Color.WHITE);
        instructions.setTextSize(fontSize);
        instructions.setTypeface(null, Typeface.BOLD);

        return instructions;
    }

    /*
    Creates an EditText box with the given starting text and size and returns it in a RelativeLayout.
     */
    public static RelativeLayout getEditTextBox(Activity activity, String text, int width, int height) {
        EditText editText = new EditText(activity);
        editText.setBackgroundColor(Color.WHITE);
        editText.setGravity(Gravity.TOP);

        RelativeLayout relativeLayout = new RelativeLayout(activity);
        RelativeLayout.LayoutParams paramRelativeLayout = new RelativeLayout.LayoutParams(width, height );
        paramRelativeLayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLayout.addView(editText,paramRelativeLayout);

        return relativeLayout;

    }
}
