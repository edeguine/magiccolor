package com.algofashion.magiccolor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {

    public static String addGradientDirection(String jparams, myPoint start, myPoint end) {

        try {
            JSONObject json = new JSONObject(jparams);
            JSONObject jstart, jend;
            jstart = new JSONObject();
            jend = new JSONObject();

            jstart.put("x", (int) start.x);
            jstart.put("y", (int) start.y);
            jend.put("x", (int) end.x);
            jend.put("y", (int) end.y);

            JSONObject points = new JSONObject();
            points.put("start", jstart);
            points.put("end", jend);

            json.put("gradientDirection", points);
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String addGradientType(String jparams, String type) {
        try {
            JSONObject json = new JSONObject(jparams);
            json.put("type", type);
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
