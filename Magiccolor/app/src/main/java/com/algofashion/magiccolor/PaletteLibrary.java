package com.algofashion.magiccolor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.ArrayMap;

import java.util.Collection;
import java.util.Vector;

public class PaletteLibrary {

    public ArrayMap library, renderings;
    public Activity activity;

    static {
        System.loadLibrary("native-lib");
    }
    public native void renderGradient(int[] pixels, int w, int h, String jpalette);
    public native void separateBW(int[] pixels, int w, int h, float threshold, String jpalette);

    public PaletteLibrary(Activity a) {
        activity = a;

        library = new ArrayMap();

        Vector<String> palettes = new Vector<String>();
        palettes.add("carmin");
        palettes.add("cd");
        palettes.add("evening");
        palettes.add("goldmine");
        palettes.add("insta");
        palettes.add("lightdrop");
        palettes.add("peach");
        palettes.add("rasta");
        palettes.add("sunset");

        for(String name: palettes) {
            library.put(name, createPaletteFromPNG(name));
        }

    }

    public Bitmap createRendering(int w, int h, String name) {
        Collection<Integer> pal = getPalette(name);
        String jpalette = PaletteUtils.makeJSONPalette(pal);
        Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        int[] pixels = new int[w * h];
        b.getPixels(pixels, 0, w, 0, 0, w, h);
        renderGradient(pixels, w, h, jpalette);
        b.setPixels(pixels, 0, w, 0, 0, w, h);
        return b;
    }

    public Bitmap createRainbowRendering(int w, int h) {

        String jparam = "{}";
        jparam = JSONUtils.addGradientType(jparam, "rainbow");
        jparam = JSONUtils.addGradientDirection(jparam, new myPoint(0, 0), new myPoint(w, 0));

        Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        int[] pixels = new int[w * h];
        b.getPixels(pixels, 0, w, 0, 0, w, h);
        separateBW(pixels, w, h, 0.5f, jparam);
        b.setPixels(pixels, 0, w, 0, 0, w, h);

        return b;
    }

    public Collection<Integer> createGoldmine() {
        return createPaletteFromPNG("goldmine");
    }

    public Collection<Integer> createSunset() {
        return createPaletteFromPNG("sunset");
    }

    public Collection<Integer> createPaletteFromPNG(String filename) {

        String path = "@drawable/" + filename;
        int id = activity.getResources().getIdentifier(path, null, activity.getPackageName());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), id, options);

        Vector<Integer> colors = new Vector<Integer>();
        for(int i = 0; i < bitmap.getWidth(); i++) {
            colors.add(new Integer(bitmap.getPixel(i, 0)));
        }
        return colors;
    }

    public Collection getPalette(String name) {
        return (Collection) library.get(name);
    }

}
