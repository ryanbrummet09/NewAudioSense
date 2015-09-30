package com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.UI;

import android.app.Activity;
import android.graphics.Color;
import android.os.Vibrator;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.util.Log;
import android.widget.TextView.OnEditorActionListener;

import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content.UserInputInfoContent;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.AbstractSingleSelectionSubComponent;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.CustomEditText;

import java.util.ArrayList;

/**
 * Created by ryanbrummet on 8/25/15.
 */
public class UserInputInfoUI extends AbstractSingleSelectionSubComponent{

    private final UserInputInfoContent content;
    private boolean toggle;
    private CustomEditText editText;
    private Vibrator vibrator;

    public UserInputInfoUI(String id, int color, UserInputInfoContent content){
        super(id,color,content);
        this.content = content;
        this.toggle = true;
        editText = null;
    }

    @Override
    public void render(final Activity activity, Display display,ViewGroup rootView) {

        // remove all current views
        rootView.removeAllViews();
        vibrator = (Vibrator) rootView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        rootView.setBackgroundColor(getColor());

        // layout params
        LinearLayout.LayoutParams paramFirstSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                display.getHeight() / 40 );
        LinearLayout.LayoutParams paramInstruction = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                3 * display.getHeight() / 20);
        LinearLayout.LayoutParams paramSecondSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                display.getHeight() / 80);
        LinearLayout.LayoutParams paramNavPanel = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams paramThirdSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                display.getHeight() / 40 );
        LinearLayout.LayoutParams paramEditText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // first space
        TextView firstSpace = new TextView(activity);

        // instructions
        TextView instructions = StaticUIComponents.blueInstructionTextView(activity, content.getInstructionQuestion(), 20);

        // second space
        TextView secondSpace = new TextView(activity);

        // add two direction navPanel and param
        RelativeLayout navPanel = StaticUIComponents.twoDirectionNavBar(activity,
                new PrevScreenSurveyListener(activity,rootView),
                new NextScreenSurveyListener(activity,rootView),
                "Back","Save and next", false, display);

        // third space
        TextView thirdSpace = new TextView(activity);

        // edit text
        if(editText == null) {
            editText = new CustomEditText(activity);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setBackgroundColor(Color.WHITE);
            editText.setGravity(Gravity.TOP);
            editText.setText("Type here...");
            editText.setTextColor(Color.BLACK);
            editText.setSelectAllOnFocus(toggle);
            editText.requestFocus();
            editText.setPrivateImeOptions("nm"); // disable microphone button.  absolutely essential to avoid concurrency problems
            //editText.setOnKeyListener(new RemoveEditTextInstructionKey());  //This line is not compatible with galaxy s6
            //editText.setOnClickListener(new RemoveEditTextInstructionClick());  //This line is not compatible with galaxy s6
            editText.setOnEditorActionListener(new OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    int result = actionId & EditorInfo.IME_MASK_ACTION;
                    switch(result) {
                        case EditorInfo.IME_ACTION_DONE:
                            Toast.makeText(activity, "Press the Save and next button when done", Toast.LENGTH_SHORT).show();
                            break;
                        case EditorInfo.IME_ACTION_NEXT:
                            Toast.makeText(activity, "Press the Save and next button when done", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    Log.e("here","it is");
                    return true;
                }
            });
        } else {
            ((ViewGroup) editText.getParent()).removeView(editText);
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        RelativeLayout relativeLayoutEditText = new RelativeLayout(activity);
        RelativeLayout.LayoutParams paramRelativeLayoutEditText = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 43 * display.getHeight() / 80);
        paramRelativeLayoutEditText.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLayoutEditText.addView(editText, paramRelativeLayoutEditText);

        // add all created views to rootView
        rootView.addView(firstSpace,paramFirstSpace);
        rootView.addView(instructions,paramInstruction);
        rootView.addView(secondSpace,paramSecondSpace);
        rootView.addView(navPanel,paramNavPanel);
        rootView.addView(thirdSpace,paramThirdSpace);
        rootView.addView(relativeLayoutEditText, paramEditText);

    }

    public void resetUI(){
        toggle = true;
        editText = null;
        getContent().removeFromResponseContent();
    }

    public ArrayList<String> getResults() {

        ArrayList<String> results = new ArrayList<String>();
        if (subComponents != null) {
            for (int i = 0; i < subComponents.length; i++) {
                results.addAll(subComponents[i].getResults());
            }
        }
        ArrayList<String> temp = content.getResponseContent();
        for(int i = 0; i < temp.size(); i++) {
            results.add(getComponentID() + ":" + temp.get(i));
        }

        return results;
    }

    private class NextScreenSurveyListener implements View.OnClickListener {

        private Activity activity;
        private View rootView;

        public NextScreenSurveyListener(Activity activity, View rootView) {
            super();
            this.activity = activity;
            this.rootView = rootView;
        }

        @Override
        public void onClick(View view) {
            if(editText.getText().toString().matches("") || editText.getText().toString().matches("Type here...")) {
                Toast.makeText(activity, "Please give a brief explanation", Toast.LENGTH_SHORT).show();
            } else {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInputFromWindow(rootView.getWindowToken(), 0, 0);
                content.setResponse(editText.getText().toString(),1);
                vibrator.vibrate(250);
                gotoNextComponent();
            }
        }

    }

    private class PrevScreenSurveyListener implements View.OnClickListener {

        private Activity activity;
        private View rootView;

        public PrevScreenSurveyListener(Activity activity, View rootView) {
            super();
            this.activity = activity;
            this.rootView = rootView;
        }

        @Override
        public void onClick(View view) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(rootView.getWindowToken(), 0,0);
            resetUI();
            vibrator.vibrate(250);
            gotoPreviousComponent();
        }
    }

    private class RemoveEditTextInstructionKey implements View.OnKeyListener {

        public RemoveEditTextInstructionKey() {
            super();
        }

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if(toggle) {
                ((EditText) view).getText().clear();
                toggle = false;
            }
            Log.e("keyInput",Integer.toString(keyCode));

            return true;
        }
    }

    private class RemoveEditTextInstructionClick implements View.OnClickListener {

        public RemoveEditTextInstructionClick(){
            super();
        }

        @Override
        public void onClick(View view) {
            if(toggle) {
                ((EditText) view).getText().clear();
                toggle = false;
            }
        }
    }

}
