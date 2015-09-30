package com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;


import com.example.ryanbrummet.newaudiosense2.AudioSense.Survey.Content.PathSizeDoesNotMatchNumComponents;

import java.util.ArrayList;


/**
 * Created by ryanbrummet on 8/23/15.
 */
abstract public class AbstractSingleSelectionSubComponent extends AbstractSurveySubComponent {

    private final AbstractSingleSelectionContent content;
    public final AbstractSurveySubComponent[] subComponents;
    public ArrayList<AbstractSurveySubComponent> selectedSubComponents;
    private final int[][] subComponentExecutePaths;
    private int subComponentIndex; // zero indicates the component is the contextOccuranceUI.  Any other
                                // value greater than zero subtract one to get the index in
                                // singleChoiceMultiOptionUI


    // int[][] subComponentExecutePaths: first dim is number of buttons and second dim is the size of subComponents
    public AbstractSingleSelectionSubComponent(String id, int color,AbstractSingleSelectionContent content,
                                               AbstractSurveySubComponent[] subComponents,
                                               int[][] subComponentExecutePaths) {
        super(id,color);
        this.content = content;
        this.subComponents = subComponents;
        this.subComponentExecutePaths = subComponentExecutePaths;
        selectedSubComponents = new ArrayList<AbstractSurveySubComponent>();
        subComponentIndex = -1;

        if(subComponentExecutePaths[0].length != subComponents.length) {
            throw new PathSizeDoesNotMatchNumComponents("The number of questions does not match the sub component execution path size");
        }
    }

    // for when component has no children
    public AbstractSingleSelectionSubComponent(String id, int color, AbstractSingleSelectionContent content) {
        super(id, color);
        this.content = content;
        this.subComponents = null;
        this.subComponentExecutePaths = null;
        selectedSubComponents = new ArrayList<AbstractSurveySubComponent>();
        subComponentIndex = -1;
    }

    public void route(Activity activity, Display display, ViewGroup rootView,
                       RouteComponents parentRenderComponents, int thisComponentIndex) {

        Log.i("AbstractSingleSelectionSubComponent", "Rendering AbstractSingleSelectionSubComponent " +
                Integer.toString(thisComponentIndex));

        super.route(activity, display, rootView, parentRenderComponents, thisComponentIndex);

        if (subComponents == null) {
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

    public ArrayList<String> getResults() {

        ArrayList<String> results = new ArrayList<String>();
        if (subComponents != null) {
            for (int i = 0; i < subComponents.length; i++) {
                results.addAll(subComponents[i].getResults());
            }
        }
        /*
        ArrayList<String> temp = content.getResponseContent();
        for(int i = 0; i < temp.size(); i++) {
            results.add(getComponentID() + ":" + temp.get(i));
        }*/
        if(content.getResponseIndex() == -1) {
            results.add(getComponentID() + ":" + "NaN");
        } else {
            results.add(getComponentID() + ":" + Integer.toString(content.getResponseIndex()));
        }


        return results;
    }

    public void gotoPreviousComponent(){
        Log.i(getClass().getSimpleName(),this.getComponentID() + " Moving to previous AbstractSingleSelectionSubComponent");
        if(subComponents == null) {
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
    public void gotoNextComponent(){
        Log.i(getClass().getSimpleName(), this.getComponentID() + " Moving to next AbstractSingleSelectionSubComponent");
        if(subComponents == null) {
            getRouteComponents().route(getThisComponentIndex() + 1);
        } else {
            if (subComponentIndex < 0) {
                int[] subComponentsToRender = subComponentExecutePaths[content.getResponseIndex()];
                for (int i = 0; i < subComponentsToRender.length; i++) {
                    if (subComponentsToRender[i] == 1) {
                        selectedSubComponents.add(subComponents[i]);
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

    public AbstractSingleSelectionContent getContent() {
        return content;
    }
}
