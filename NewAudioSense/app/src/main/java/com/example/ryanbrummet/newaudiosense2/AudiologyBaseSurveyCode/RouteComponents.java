package com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by ryanbrummet on 8/21/15.
 */
public class RouteComponents {

    private final Activity activity;
    private final ViewGroup rootView;
    private final ArrayList<AbstractSurveySubComponent> components;
    private final AbstractSurveyRootComponent parentComponent;
    private final Display display;
    private boolean allComponentsRendered;

    public RouteComponents(Activity activity, ViewGroup rootView, AbstractSurveyRootComponent parentComponent,
                           ArrayList<AbstractSurveySubComponent> components) {

        Log.i("RouteComponents", "New renderComponent object created with " + Integer.toString(components.size()) + " components");
        this.activity = activity;
        this.rootView = rootView;
        this.parentComponent = parentComponent;
        this.components = components;

        // finds the screen dimensions.  This approach is taken because damn if I was able to get things right using the
        // inbuilt weighting (ie the weighting is broken for LinearLayout, and RelativeLayout has no weighting.  In addition
        // The "gravity" feature of LinearLayout is also broken).
        this.display = this.activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        this.display.getSize(size);

        allComponentsRendered = false;
    }

    public void route(int currentComponent) {
        Log.i("RouteComponents", "Rendering  sub component " + Integer.toString(currentComponent) + " of " + Integer.toString(components.size() - 1));

        if (currentComponent < components.size() && currentComponent >= 0) {
            components.get(currentComponent).route(activity, display, rootView, this, currentComponent);
        } else if(currentComponent < 0){
            parentComponent.gotoPreviousComponent();
        } else {
            parentComponent.gotoNextComponent();
        }
    }
}
