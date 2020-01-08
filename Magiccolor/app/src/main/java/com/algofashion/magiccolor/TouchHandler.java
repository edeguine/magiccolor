package com.algofashion.magiccolor;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchHandler {

    // This class is used to draw the gradient direction by touching the screen twice
    // once a the start once at the end

    public float ax, ay;
    public float bx, by;
    String status;
    FilterBWActivity activity;

    public TouchHandler(FilterBWActivity a) {
        ax = ay = 0;
        bx = by = 400;
        status = "start";
        activity = a;
    }

    public void touchDown(View v, MotionEvent ev) {

        String newStatus = status;
        if(status == "start") { // Accepting the first event
            ax = ev.getX();
            ay = ev.getY();
            newStatus = "end";
        }

        if(status == "end") { // Accepting the second event
            bx = ev.getX();
            by = ev.getY();
            newStatus = "start";
            activity.separateBW();
        }

        status = newStatus;
    }

    public myPoint getStart() {
        return new myPoint(ax, ay);
    }

    public myPoint getEnd() {
        return new myPoint(bx, by);
    }

    public float getTranslationX() {
        return bx - ax;
    }

    public float getTranslationY() {
        return by - ay;
    }

    public void dispTouch() {
        if(status == "start") {
            Log.v("BW Touch", " " + String.valueOf(getTranslationX()) + " " + String.valueOf(getTranslationY()));
        }
    }


}
