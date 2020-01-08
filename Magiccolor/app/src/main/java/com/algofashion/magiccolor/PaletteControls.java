package com.algofashion.magiccolor;

import android.graphics.Bitmap;
import android.util.ArrayMap;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;

import java.util.Vector;

public class PaletteControls {

    ColorPickerDialog colorPicker;
    public String pickerAction = "new"; // new to add a color to the palette, edit to change one
    public TextView currentTV;
    public ArrayMap colorViews = new ArrayMap();
    public ArrayMap colorPalette = new ArrayMap();
    public FilterBWActivity activity;
    public PaletteLibrary paletteLibrary;
    public RadioGroupUtil rd_group_standard_palette;

    public PaletteControls(FilterBWActivity a) {
        activity = a;
        paletteLibrary = new PaletteLibrary(a);
    }

    public void registerListeners() {

        // Creates the UX for adding and editing colors in custom mode

        RadioGroup paletteType = activity.findViewById(R.id.rd_group_palette_type);

        paletteType.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        showPaletteControls(i);
                    }
                });

        loadPaletteRenderings();

        TextView add = activity.findViewById(R.id.tv_add_color);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickerAction = "new";
                showColorPicker();
            }
        });

        paletteType.clearCheck();
        RadioButton defaultPalette = activity.findViewById(R.id.rd_palette_standard);
        defaultPalette.setChecked(true);

        colorPicker = ColorPickerDialog.createColorPickerDialog(activity);
        colorPicker.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
            @Override
            public void onColorPicked(int color, String hexVal) {
                if(pickerAction == "new") {
                    addColor(color);
                }
                if(pickerAction == "edit") {
                    editColor(currentTV, color);
                }
            }
        });
    }

    public void initUI() {
        hidePaletteLibrary();
        hidePaletteCustom();
        registerListeners();
        initRadioGroupsPalette();
        pickDefaultPalette();
    }

    public void initRadioGroupsPalette() {

        // Creates the radio group with all the palettes

        Vector<Integer> buttons = new Vector<Integer>();
        buttons.add(R.id.rd_rainbow);
        buttons.add(R.id.rd_carmin);
        buttons.add(R.id.rd_cd);
        buttons.add(R.id.rd_evening);
        buttons.add(R.id.rd_goldmine);
        buttons.add(R.id.rd_insta);
        buttons.add(R.id.rd_lightdrop);
        buttons.add(R.id.rd_peach);
        buttons.add(R.id.rd_rasta);
        buttons.add(R.id.rd_sunset);

        RadioButton rd;
        rd_group_standard_palette = new RadioGroupUtil(activity);
        for(int id: buttons) {
            rd_group_standard_palette.addButton(id);
            rd = activity.findViewById(id);
            rd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rd_group_standard_palette.check(view);
                }
            });
        }
    }

    public void pickDefaultPalette() {

        // Picks a palette by default

        rd_group_standard_palette.check(activity.findViewById(R.id.rd_rainbow));
    }

    public void loadPaletteRenderings() {

        // Add the gradients showing the palette in the UI

        Vector<Pair<Integer, String>> iv = new Vector<Pair<Integer, String>>();
        iv.add(new Pair(R.id.iv_carmin, "carmin"));
        iv.add(new Pair(R.id.iv_cd, "cd"));
        iv.add(new Pair(R.id.iv_evening, "evening"));
        iv.add(new Pair(R.id.iv_goldmine, "goldmine"));
        iv.add(new Pair(R.id.iv_insta, "insta"));
        iv.add(new Pair(R.id.iv_lightdrop, "lightdrop"));
        iv.add(new Pair(R.id.iv_peach, "peach"));
        iv.add(new Pair(R.id.iv_rasta, "rasta"));
        iv.add(new Pair(R.id.iv_rainbow, "rainbow"));
        iv.add(new Pair(R.id.iv_sunset, "sunset"));

        ImageView view;

        for(Pair<Integer, String> p: iv) {
            view = activity.findViewById(p.first);
            setPaletteRenderingIV(view, p.second);
        }
    }


    public void showColorPicker() {
        colorPicker.show();
    }

    public void addColor(int color) {
        // Just adds the view, which calls edit color to create the bitmap and add to the palette

        LinearLayout paletteLayout = activity.findViewById(R.id.layout_palette_picker);
        LinearLayout layout = createColorView(color);
        paletteLayout.addView(layout);

        activity.scrollViewPx(paletteLayout.getBottom());
    }

    public void editColor(TextView tv, int color) {
        // Changes the color for that line

        Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        PaletteUtils.fillColorBitmap(b, color);
        ((ImageView) colorViews.get(tv)).setImageBitmap(b);
        colorPalette.put(tv, new Integer(color)); // works both when adding a new color or updating existing one
    }

    public void removeColor(TextView tv) {
        // Remove the color from the palette

        View topLayout = (View) ((View) tv.getParent()).getParent();
        ((ViewManager) topLayout).removeView((View) tv.getParent());

        LinearLayout layout = (LinearLayout) tv.getParent();
        TextView tv_edit = (TextView) layout.getChildAt(1);
        colorViews.remove(tv_edit);
        colorPalette.remove(tv_edit);
    }

    public void showPaletteControls(int radioId) {
        // Switches between predefined color palettes and custom color palette

        switch (radioId) {
            case R.id.rd_palette_standard:
                hidePaletteCustom();
                showPaletteLibrary();
                break;
            case R.id.rd_palette_custom:
                hidePaletteLibrary();
                showPaletteCustom();
                break;
        }
    }

    public void showPaletteLibrary() {
        // Shows the predefined palettes

        activity.findViewById(R.id.layout_palette_library).setVisibility(View.VISIBLE);
    }

    public void showPaletteCustom() {
        // Shows the UI to build a custom color palette

        activity.findViewById(R.id.layout_palette_picker).setVisibility(View.VISIBLE);
    }


    public void hidePaletteLibrary() {
        activity.findViewById(R.id.layout_palette_library).setVisibility(View.GONE);
    }

    public void hidePaletteCustom() {
        activity.findViewById(R.id.layout_palette_picker).setVisibility(View.GONE);
    }


    public int dpToPx(int dp) {
        // Convenience function

        final float scale = activity.getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (dp * scale + 0.5f);
        return padding_in_px;
    }

    public LinearLayout createColorView(int color) {

        /*
        Creating a color view for the custom palettes involved creating:
         - the small bitmap with the color
         - An edit button
         - A remove button

         The quirk is that we call edit on all views to register the color into the palette, because the Map is keyed by the edit view

         */

        LinearLayout layout = new LinearLayout(activity);
        layout.setLayoutDirection(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int pxMargin = dpToPx(15);
        params.setMargins(pxMargin, pxMargin, pxMargin, pxMargin);

        ImageView iv_color = new ImageView(activity);
        iv_color.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        iv_color.setPadding(0,0, dpToPx(25), 0);

        TextView tv_edit = new TextView(activity);
        tv_edit.setText("Edit");
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv_edit.setLayoutParams(tvParams);
        tv_edit.setGravity(Gravity.CENTER_VERTICAL);

        tv_edit.setPadding(dpToPx(25),0, dpToPx(25), 0);
        colorViews.put(tv_edit, iv_color);
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickerAction = "edit";
                currentTV = (TextView) view;
                showColorPicker();
            }
        });

        // Simpler logic, adding a new color is the same codepath as editing it
        editColor(tv_edit, color);

        TextView tv_remove = new TextView(activity);
        tv_remove.setText("Remove");
        tv_remove.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tv_remove.setPadding(dpToPx(25),0, dpToPx(25), 0);
        tv_remove.setGravity(Gravity.CENTER_VERTICAL);

        tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeColor((TextView) view);
            }
        });

        layout.addView(iv_color);
        layout.addView(tv_edit);
        layout.addView(tv_remove);

        return layout;
    }

    public String getSelectedPalette() {
        RadioGroupUtil rg = rd_group_standard_palette;
        int radioId = rg.getChecked();

        switch(radioId) {
            case R.id.rd_carmin:
                return "carmin";
            case R.id.rd_cd:
                return "cd";
            case R.id.rd_evening:
                return "evening";
            case R.id.rd_goldmine:
                return "goldmine";
            case R.id.rd_insta:
                return "insta";
            case R.id.rd_lightdrop:
                return "lightdrop";
            case R.id.rd_peach:
                return "peach";
            case R.id.rd_rasta:
                return "rasta";
            case R.id.rd_rainbow:
                return "rainbow";
            case R.id.rd_sunset:
                return "sunset";
        }
        return null;
    }

    public String getGradientType() {
        String mode = paletteMode();

        if(mode == "standard") {
            String palette = getSelectedPalette();

            if(palette == "rainbow") {
                return "rainbow";
            } else {
                return "palette";
            }
        }

        return "";
    }

    public String getColorPalette() {

        // Returns a JSON of the color palette

        String mode = paletteMode();

        if(mode == "custom") {
            if (colorPalette.size() > 0) {
                return PaletteUtils.makeJSONPalette(colorPalette.values());
            } else {
                return null;
            }
        }

        if(mode == "standard") {
            String spalette = getSelectedPalette();
            if(spalette != "rainbow") {
                return PaletteUtils.makeJSONPalette(paletteLibrary.getPalette(spalette));
            }

            if(spalette == "rainbow") {
                return PaletteUtils.makeJSONAlgo("rainbow");
            }
        }

        return null;
    }

    public String paletteMode() {

        // Returns which palette mode is selected

        RadioGroup rg = activity.findViewById(R.id.rd_group_palette_type);
        int radioId = rg.getCheckedRadioButtonId();

        switch(radioId) {
            case R.id.rd_palette_custom:
                return "custom";
            case R.id.rd_palette_standard:
                return "standard";
        }
        return null;
    }

    public void setPaletteRenderingIV(ImageView view, String name) {

        // Generates a small bitmap showing the color palette

        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if(name == "rainbow") {
            view.setImageBitmap(paletteLibrary.createRainbowRendering(dpToPx(220), dpToPx(50)));
        } else {
            view.setImageBitmap(paletteLibrary.createRendering(dpToPx(220), dpToPx(50), name));
        }
    }

}
