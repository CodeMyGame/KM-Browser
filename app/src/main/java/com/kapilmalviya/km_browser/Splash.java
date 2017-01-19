package com.kapilmalviya.km_browser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends Activity {
TextView km;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        km = (TextView) findViewById(R.id.kmsplash);
        Animation fadein = new AlphaAnimation(0,1);
        fadein.setInterpolator(new AccelerateInterpolator());
        fadein.setDuration(4000);
        AnimationSet anim = new AnimationSet(false);
        anim.addAnimation(fadein);
        try {
            Typeface tf = Typeface.createFromAsset(getBaseContext().getAssets(), "ff.ttf");
            km.setTypeface(tf);
        }catch (Exception e){
            Toast.makeText(Splash.this, "ERROR:"+e, Toast.LENGTH_SHORT).show();
        }
        km.setAnimation(anim);

        Thread timer = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(4000);

                }catch(InterruptedException e){
                    getStackTrace();
                }finally {
                    Intent openActivity = new Intent(getBaseContext(),MainActivity.class);   //here it should be match with action name of activity which occur after spalsh activity
                    startActivity(openActivity);
                }
            }
        };
        timer.start();
    }
    @Override
    protected void onPause() {
        super.onPause();                    //here we can save the data of splash activity becoz after on create method all data be distroyed
        finish();                                  //because in gaming we have to save data.
        //look at activity cycle diagram.
    }
}
