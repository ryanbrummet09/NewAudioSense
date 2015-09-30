package com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode;

import android.graphics.drawable.Drawable;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by ryanbrummet on 9/22/15.
 */
public class MultiSelectionButtonGroup {

    private final ArrayList<Button> buttons;
    private final ArrayList<Drawable> originalBackgrounds;
    private ArrayList<Drawable> currentBackgrounds;
    private final int selectedBackground;  // must be defined in drawable
    private ArrayList<Button> selectedButtons;

    public MultiSelectionButtonGroup(ArrayList<Button> buttons, int backgroundColor) {
        this.buttons = buttons;
        selectedBackground = backgroundColor;
        this.originalBackgrounds = new ArrayList<Drawable>();
        for(int i = 0; i < buttons.size(); i ++) {
            this.originalBackgrounds.add(buttons.get(i).getBackground());
        }
        this.currentBackgrounds = new ArrayList<Drawable>();
        this.selectedButtons = new ArrayList<Button>();
    }

    public void resetBackground() {
        for(int i = 0; i < buttons.size(); i ++) {
            buttons.get(i).setBackgroundDrawable(originalBackgrounds.get(i));
        }
        this.currentBackgrounds = originalBackgrounds;
        selectedButtons = new ArrayList<Button>();
    }

    public void selectButton(Button buttonToSelect) {

        Button selectedButton = buttons.get(buttons.indexOf(buttonToSelect));
        if(selectedButtons.contains(selectedButton)) {
            selectedButtons.remove(selectedButton);
        } else {
            selectedButtons.add(selectedButton);
        }

        if(selectedButton.getBackground().equals(originalBackgrounds.get(buttons.indexOf(buttonToSelect)))) {
            selectedButton.setBackgroundResource(selectedBackground);
        } else {
            selectedButton.setBackgroundDrawable(originalBackgrounds.get(buttons.indexOf(buttonToSelect)));
        }
    }

    public ArrayList<Button> getSelectedButtons() {
        return selectedButtons;
    }

    public ArrayList<Integer> getIndexOfSelectedButtons() {
        ArrayList<Integer> toReturn = new ArrayList<Integer>();
        for(int i = 0; i < selectedButtons.size(); i++){
            toReturn.add(buttons.indexOf(selectedButtons.get(i)));
        }
        return toReturn;
    }

    public int getIndexOfButton(Button button) {
        return buttons.indexOf(button);
    }

    public Button getButtonInGroup(int i) {
        return buttons.get(i);
    }

    public int getNumberOfButtonsInGroup() {
        return buttons.size();
    }
}
