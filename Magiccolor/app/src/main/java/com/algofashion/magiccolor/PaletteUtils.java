package com.algofashion.magiccolor;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

public class PaletteUtils {

    public static void fillColorBitmap(Bitmap b, int color) {
        // Creates a bitmap of a single color

        for(int i = 0; i < b.getWidth(); i++) {
            for(int j = 0; j < b.getHeight(); j++) {
                b.setPixel(i, j, color);
            }
        }
    }

    public static String makeJSONPalette(Collection<Integer> colors) {

        // Creates a valid JSON palette from the colors
        // {"palette"; [[A0, R0, G0, B0] , [A1, R1, G1, B1]]

        JSONObject json = new JSONObject();
        JSONArray jcolors = new JSONArray();

        for(Integer ic: colors) {
            int c = ic.intValue();
            JSONArray argb = new JSONArray();
            argb.put((int) Color.alpha(c));
            argb.put((int) Color.red(c));
            argb.put((int) Color.green(c));
            argb.put((int) Color.blue(c));
            jcolors.put(argb);
        }

        try {
            json.put("palette", jcolors);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static String makeJSONAlgo(String mode) {
        // Make a JSON when the color palette is an algo, like rainbow

        JSONObject json = new JSONObject();
        try {
            json.put("algorithm", mode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}
