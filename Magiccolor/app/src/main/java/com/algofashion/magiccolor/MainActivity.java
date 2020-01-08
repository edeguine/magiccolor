package com.algofashion.magiccolor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private static final String TAG = "MainActivity";
    public static final int PICK_IMAGE = 1;
    public PaletteControls paletteControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initUI();

    }

    void initUI() {

        setContentView(R.layout.activity_main);

        initToolbar();

        Button btn_pickPhoto = findViewById(R.id.btn_pickPhoto);
        btn_pickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {pickPicture();
            }
        });

        final Activity a = this;
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterBWActivity.test(a);
            }
        });
    }

    void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Magic color");
        toolbar.setNavigationIcon(R.drawable.ic_home_24px);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public void pickPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            Log.v(TAG, data.getData().toString());
            Intent intent = new Intent(this, FilterBWActivity.class);
            intent.putExtra("Image", data.getData().toString());
            startActivity(intent);
        }
    }
}
