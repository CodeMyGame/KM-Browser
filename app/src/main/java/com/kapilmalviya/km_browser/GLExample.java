package com.kapilmalviya.km_browser;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * Created by Kapil Malviya on 1/9/2016.
 */
public class GLExample extends Activity {

    GLSurfaceView oursurfaceview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oursurfaceview = new GLSurfaceView(this);
        setContentView(oursurfaceview);
        oursurfaceview.setRenderer(new GLRendererEx());


    }

    @Override
    protected void onResume() {
        super.onResume();
        oursurfaceview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        oursurfaceview.onPause();
    }

}

