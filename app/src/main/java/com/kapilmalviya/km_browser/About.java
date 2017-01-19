package com.kapilmalviya.km_browser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class About extends Activity {
TextView abut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        abut = (TextView) findViewById(R.id.abt);
        Typeface typeface = Typeface.createFromAsset(getBaseContext().getAssets(),"ww.ttf");
        abut.setTypeface(typeface);
    }

}
