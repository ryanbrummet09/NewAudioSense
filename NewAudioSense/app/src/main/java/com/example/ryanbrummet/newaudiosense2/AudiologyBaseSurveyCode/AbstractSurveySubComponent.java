package com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode;

import android.app.Activity;
import android.graphics.Color;
import android.view.Display;
import android.view.ViewGroup;

/**
 * Created by ryanbrummet on 8/23/15.
 */
public abstract class AbstractSurveySubComponent extends AbstractSurveyRootComponent{

    private RouteComponents routeComponents;
    private int thisComponentIndex;
    private int color;

    public AbstractSurveySubComponent(String componentID, int color) {
        super(componentID);
        this.routeComponents = null;
        this.color = color;
        this.thisComponentIndex = -2; //this is -2 for a reason.  We want to make sure that if this value
                                      // has not been "initialized" and gotoNextComponent() is called
                                      // that an error will be thrown.
    }

    /*
    all subclasses of this class MUST CALL super.render(activity,display,rootView,routeComponents,currentScreen);
    In addition, all contents must be sized so that the total percentage of the screen used is no more
    than 85% if using LinearLayout as the root otherwise components will appear off screen.
     */
    public void route(Activity activity, Display display, ViewGroup rootView,
                           RouteComponents routeComponents, int currentComponent) {
        this.routeComponents = routeComponents;
        this.thisComponentIndex = currentComponent;
    }

    abstract public void render(Activity activity, Display display, ViewGroup rootView);

    abstract public void resetUI();

    public RouteComponents getRouteComponents() {
        return routeComponents;
    }

    public int getThisComponentIndex(){
        return thisComponentIndex;
    }

    public int getColor() {
        return color;
    }


}
