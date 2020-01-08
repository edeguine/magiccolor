package com.algofashion.magiccolor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class FilterBWActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    public static final String TAG = "FilterBWActivity";
    public native void separateBW(int[] pixels, int w, int h, float threshold, String jpalette);

    public Uri imageUri;
    public PaletteControls paletteControls;
    public TouchHandler touchHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initTouchHandler();

    }

    // UI code

    public void initUI() {
        setContentView(R.layout.activity_filterbw);

        initToolbar();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String value = extras.getString("Image");
            imageUri = Uri.parse(value);
            ImageView iv = findViewById(R.id.iv_picture);
            iv.setImageURI(imageUri);
        }

        Button btn_pickPhoto = findViewById(R.id.btn_separate);
        btn_pickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                separateBW();
            }
        });

        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImageHelper();
            }
        });

        paletteControls = new PaletteControls(this);
        paletteControls.initUI();

        scrollView(27);

    }

    void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Magic color");
        toolbar.setNavigationIcon(R.drawable.ic_home_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(FilterBWActivity.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });
    }

    public void initTouchHandler() {

        touchHandler = new TouchHandler(this);
        View iv = findViewById(R.id.iv_picture);
        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    touchHandler.touchDown(view, motionEvent);
                    touchHandler.dispTouch();
                }
                return true;
            }
        });
    }

    public final void scrollView(float dp){
        final ScrollView sview = findViewById(R.id.sv_main);
        final float dparg = dp;
        sview.post(new Runnable() {
            @Override
            public void run() {
                sview.scrollTo(0, Utils.dpToPixels(getApplicationContext(), dparg));
            }
        });
    }

    public final void scrollViewPx(int px){
        final ScrollView sview = findViewById(R.id.sv_main);
        final int pxarg = px;
        sview.post(new Runnable() {
            @Override
            public void run() {
                sview.scrollTo(0, pxarg);
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public static void test(Activity a) {
        String path = "@drawable-nodpi/sunset.png";
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(a.getResources(), R.drawable.sunset, options);
        for(int i = 0; i < bitmap.getWidth(); i++) {
            Log.v(TAG, String.valueOf(bitmap.getPixel(i, 0)));
            Color c = Color.valueOf(bitmap.getPixel(i, 0));
            Log.v(TAG, c.toString());
        }
    }

    // Drawing code

    public void separateBW() {
        Bitmap bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888);
        Bitmap bcopy = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            bcopy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int w = bcopy.getWidth();
        int h = bcopy.getHeight();
        int[] pixels = new int[w * h];
        bcopy.getPixels(pixels, 0, w, 0, 0, w, h);

        SeekBar slider = findViewById(R.id.slider_threshold);
        int ithreshold = slider.getProgress();
        float threshold = ithreshold / 1000.0f;
        Log.v(TAG, "slider value: " + threshold);

        String jpalette = paletteControls.getColorPalette();
        Log.v(TAG, "jpalette: " + jpalette);

        String jparam = jpalette;
        jparam = JSONUtils.addGradientDirection(jpalette, touchHandler.getStart(), touchHandler.getEnd());

        String gradientType = paletteControls.getGradientType();
        jparam = JSONUtils.addGradientType(jparam, gradientType);

        Log.v(TAG, "jparam: " + jparam);


        if(paletteControls.paletteMode() == "custom") {
            if (jpalette != null) {
                separateBW(pixels, w, h, threshold, jparam);
                bcopy.setPixels(pixels, 0, w, 0, 0, w, h);
                ImageView iv = findViewById(R.id.iv_picture);
                iv.setImageBitmap(bcopy);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Pick a palette")
                        .setMessage("Please pick colors for your palette by clicking \"Add\"")
                        .setPositiveButton("OK", null)
                        .show();
            }
        }

        if(paletteControls.paletteMode() == "standard") {

            separateBW(pixels, w, h, threshold, jparam);
            bcopy.setPixels(pixels, 0, w, 0, 0, w, h);
            ImageView iv = findViewById(R.id.iv_picture);
            iv.setImageBitmap(bcopy);
        }
    }

    // Saving code

    public boolean mediaPermissions = false;
    public int saveImageHelperPermissionCheck = 12;

    private void saveImageHelper() {
        if(mediaPermissions == false) {
            checkMediaPermissionsForSaveImage();
        } else {
            saveImage();
        }
    }

    public void checkMediaPermissionsForSaveImage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        saveImageHelperPermissionCheck);
            } else {
                mediaPermissions = true;
                saveImage();
        }
    }

    public void onRequestPermissionsResult (int requestCode,
                                     String[] permissions,
                                     int[] grantResults) {

        if(requestCode == saveImageHelperPermissionCheck) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                mediaPermissions = true;
                saveImage();
            } else {
                mediaPermissions = false;
                Toast.makeText(getApplicationContext(), "Please allow media permissions to save files", 5);
            }
        }
    }

    public void saveImage() {
        /*
        ImageView iv = findViewById(R.id.iv_picture);
        Bitmap bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap();
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "colored" , "Gradient colored image");

        */

        saveImagePNG();
        Toast.makeText(getApplicationContext(), "Saved image", 5).show();
    }

    public void saveImagePNG() {

            ImageView iv = findViewById(R.id.iv_picture);
            Bitmap bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap();
            ContentResolver cr = getApplication().getContentResolver();
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "Colored");
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "Colored image");
            values.put(MediaStore.Images.Media.DESCRIPTION, "Gradient colored image");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            // Add the date meta data to ensure the image is added at the front of the gallery
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

            Uri url = null;   /* value to be returned */

            try {
                url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                if (bitmap != null) {
                    OutputStream imageOut = cr.openOutputStream(url);
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOut);
                    } finally {
                        imageOut.close();
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
    }

}
