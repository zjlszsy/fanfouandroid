package com.wentingzhou.android.fanfouclient;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * Created by wenting on 3/14/18.
 */

public class DisplayPhotoActivity extends Activity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.displayphoto);

        Bundle bundle = getIntent().getExtras();
        String photoURL = bundle.getString("photoLink");

        ImageView photo = (ImageView) findViewById(R.id.enlargedPhoto);
        Glide.with(getApplicationContext())
                .load(photoURL)
                .into(photo);
    }

}
