package com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode;

import android.graphics.drawable.Drawable;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by ryanbrummet on 8/22/15.
 */
public class SingleSelectionButtonGroup  {

    private final ArrayList<Button> buttons;
    private final ArrayList<Drawable> originalBackgrounds;
    private final int selectedBackground;  // must be defined in drawable
    private Button selectedButton;

    public SingleSelectionButtonGroup(ArrayList<Button> buttons, int backgroundColor) {
        this.buttons = buttons;
        selectedBackground = backgroundColor;
        this.originalBackgrounds = new ArrayList<Drawable>();
        for(int i = 0; i < buttons.size(); i ++) {
            this.originalBackgrounds.add(buttons.get(i).getBackground());
        }
        this.selectedButton = null;
    }

    public void resetBackground() {
        for(int i = 0; i < buttons.size(); i ++) {
            buttons.get(i).setBackgroundDrawable(originalBackgrounds.get(i));
        }
        selectedButton = null;
    }

    public void selectButton(Button buttonToSelect) {
        resetBackground();
        selectedButton = buttons.get(buttons.indexOf(buttonToSelect));
        selectedButton.setBackgroundResource(selectedBackground);
    }

    public Button getSelectedButton() {
        return selectedButton;
    }

    public int getIndexOfSelectedButton() {
        return buttons.indexOf(selectedButton);
    }

    public Button getButtonInGroup(int i) {
        return buttons.get(i);
    }

    public int getNumberOfButtonsInGroup() {
        return buttons.size();
    }

}
