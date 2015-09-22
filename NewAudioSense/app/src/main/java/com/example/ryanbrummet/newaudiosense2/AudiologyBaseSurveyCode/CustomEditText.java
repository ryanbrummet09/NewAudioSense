package com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode;

import android.app.Activity;
import android.widget.EditText;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.Toast;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ryanbrummet on 8/27/15.
 * Credit for code goes to @Alican Temel on stackoverflow
 * http://stackoverflow.com/questions/3940127/intercept-back-button-from-soft-keyboard
 */
public class CustomEditText extends EditText {

    Context context;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CustomEditText(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Toast.makeText(context, "Back Button is Disabled", Toast.LENGTH_SHORT).show();
            // User has pressed Back key. So hide the keyboard
            //InputMethodManager mgr = (InputMethodManager)
                    //context.getSystemService(Context.INPUT_METHOD_SERVICE);
            //mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
            // TODO: Hide your view as you do it in your activity
        } //else if (keyCode == KeyEvent.KEYCODE_TV_NUMBER_ENTRY) {
            //Toast.makeText(context, "This Button is Disabled", Toast.LENGTH_SHORT).show();
        //}
        return true;
    }
}
