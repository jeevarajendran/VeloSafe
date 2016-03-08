package com.techgenie.velosafe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;

public class ImageTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Code for image

        final Intent inputIntent = getIntent();
        String bike_image_string = inputIntent.getExtras().getString("bike_image");
        //Log.d("bike_image",bike_image_string);

        byte[] bike_image_bytes = Base64.decode(bike_image_string, Base64.DEFAULT);
        Bitmap resultImage = BitmapFactory.decodeByteArray(bike_image_bytes, 0, bike_image_bytes.length);
        //Log.d("decoded String", resultImage.toString());

        /*ImageView imageView = (ImageView) findViewById(R.id.bike_image_test);
        imageView.setImageBitmap(resultImage);
        */
        // Code for image

    }

}
