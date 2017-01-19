package com.kapilmalviya.km_browser;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/**
 * Created by Kapil Malviya on 12/29/2015.
 */
class CircleTransform implements Transformation {

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(),source.getHeight());
        int x = (source.getWidth()-size)/2;
        int y = (source.getHeight()-size)/2;
        Bitmap bitmap = Bitmap.createBitmap(source,x,y,size,size);
        if(bitmap!=source){
            source.recycle();

        }
        Bitmap nbitmap =Bitmap.createBitmap(size,size,source.getConfig());
        Canvas c = new Canvas(nbitmap);
        Paint p = new Paint();
        BitmapShader bs = new BitmapShader(bitmap,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP);
        p.setShader(bs);
        p.setAntiAlias(true);
        float r = size/2f;
        c.drawCircle(r,r,r,p);
        bitmap.recycle();
        return nbitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}
