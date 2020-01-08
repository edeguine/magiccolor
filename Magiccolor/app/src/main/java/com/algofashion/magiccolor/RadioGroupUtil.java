package com.algofashion.magiccolor;

import android.app.Activity;
import android.util.ArrayMap;
import android.view.View;
import android.widget.RadioButton;

import java.util.Vector;

public class RadioGroupUtil {

    Activity activity;
    ArrayMap<View, Integer> buttons;

    public RadioGroupUtil(Activity a) {
        activity = a;
        buttons = new ArrayMap<View, Integer>();
    }

    public void addButton(int id) {
        buttons.put(activity.findViewById(id), new Integer(id));
    }

    public void uncheckAll() {
        for(View v: buttons.keySet()) {
            ((RadioButton) v).setChecked(false);
        }
    }

    public void check(View view) {
        uncheckAll();
        ((RadioButton) view).setChecked(true);
    }

    public int getChecked() {
        for(View v: buttons.keySet()) {
            if(((RadioButton) v).isChecked()) {
                return buttons.get(v).intValue();
            }
        }
        return -1;
    }
}
