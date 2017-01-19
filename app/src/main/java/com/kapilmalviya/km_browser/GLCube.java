package com.kapilmalviya.km_browser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kapil Malviya on 1/9/2016.
 */
public class GLCube {
    private float vertices[] ={
            1,1,-1,
            1,-1,-1,
            -1,-1,-1,
            -1,1,-1,
            1,1,1,
            1,-1,1,
            -1,-1,1,
            -1,1,1
    } ;
    private short index[] = {
            3,4,0   ,0,4,1   ,3,0,1,
            3,7,4   ,7,6,4   ,7,3,6,
            3,1,2   ,1,6,2   ,6,3,2,
            1,4,5   ,5,6,1   ,6,5,4

    };


    private FloatBuffer verBuff,colBuff;
    private ShortBuffer indBuff;

    public GLCube(){
        ByteBuffer bBuff = ByteBuffer.allocateDirect(vertices.length * 4);
        bBuff.order(ByteOrder.nativeOrder());
        verBuff = bBuff.asFloatBuffer();
        verBuff.put(vertices);
        verBuff.position(0);

        ByteBuffer iBuff = ByteBuffer.allocateDirect(index.length * 2);
        iBuff.order(ByteOrder.nativeOrder());
        indBuff = iBuff.asShortBuffer();
        indBuff.put(index);
        indBuff.position(0);

        /*ByteBuffer cBuff = ByteBuffer.allocateDirect(color.length * 4);
        bBuff.order(ByteOrder.nativeOrder());
        colBuff = cBuff.asFloatBuffer();
        colBuff.put(color);
        colBuff.position(0);*/
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CW);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //  gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verBuff);
        //  gl.glColorPointer(4, GL10.GL_FLOAT, 0, colBuff);
        gl.glDrawElements(GL10.GL_TRIANGLES, index.length, GL10.GL_UNSIGNED_SHORT, indBuff);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        // gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);
    }

}

