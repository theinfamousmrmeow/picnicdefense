package com.examples.drawviewtest;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.content.Context;


public class MainThread extends Thread {

    private SurfaceHolder holder;
    //public static MediaPlayer mp;
    private boolean isRunning = false;
    private static final Object lock = new Object();
    //private SurfaceHolder holder;
    Bitmap tiles;
    Bitmap bug1,bug2,bug3;
    float bug_radius;
    boolean touched;
    boolean initialized = false;
    boolean bug_squashed = false;
    int x, y, tx, ty;
    Context context;



    public MainThread (SurfaceHolder surfaceHolder, Context context) {
        holder = surfaceHolder;
        touched = false;
        this.context = context;
        bug_squashed = false;
        x = y = 0;
    }

    public void setRunning(boolean b) {
        isRunning = b;
    }

    public void setXY (int x, int y) {
        synchronized (lock) {
            this.tx = x;
            this.ty = y;
            touched=true;
        }
    }

    //This stays here.
    private void LoadGraphics(Canvas canvas){
        if (!initialized){
            //Load the gfx
            Bitmap bmp;
            //float newWidth = 0.1f;
            bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.floor);
            //bmp = BitmapFactory.decodeResource (getContext().getResources(), R.drawable.sorcerer_spider);

           int newWidth = (int)(canvas.getWidth());
            float factor = (float)newWidth / bmp.getWidth();
            int newHeight = (int)(canvas.getHeight());

             tiles = Bitmap.createScaledBitmap(bmp,newWidth,newHeight,false);

            bmp= BitmapFactory.decodeResource (context.getResources(), R.drawable.bug1);
            newWidth = (int)(128);
            factor = (float)newWidth / bmp.getWidth();
            newHeight = (int)(128);
            bug1 = Bitmap.createScaledBitmap(bmp,newWidth,newHeight,false);
            bmp= BitmapFactory.decodeResource (context.getResources(), R.drawable.bug2);

            bug2 = Bitmap.createScaledBitmap(bmp,newWidth,newHeight,false);
            bmp= BitmapFactory.decodeResource (context.getResources(), R.drawable.bug3);

            bug3 = Bitmap.createScaledBitmap(bmp,newWidth,newHeight,false);

            //
//
//            bmp= BitmapFactory.decodeResource (context.getResources(), R.drawable.bug2);
//
//            newHeight = (int)(canvas.getHeight());
//
//            bug2 = Bitmap.createScaledBitmap(bug2,newWidth,newHeight,false);
//

            bug_radius = newWidth * 0.65f;
            //bmp = null;
            initialized=true;
        }
    }


    @Override
    public void run() {
        //while (!isRunning);
        while (isRunning) {
            // Lock the canvas before drawing
            if (holder != null) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    update(canvas);
                    // Perform drawing operations on the canvas
                    render(canvas);
                    // After drawing, unlock the canvas and display it
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }


    private boolean TouchInCircle(int x,int y, float radius){
        double dis = Math.sqrt((tx-x)*(tx-x)+(ty-y)*(ty-y));
        if (dis<=bug_radius){
            return true;
        }

        return false;
    }

    private void update (Canvas canvas){
        if (touched){
            //If bug squash
            if (TouchInCircle(x,y,bug_radius)){
                //Squash
                bug_squashed=true;
                Assets.tryPlaySound(context,R.raw.sfx_squash,false);
                //Assets.sp.play(Assets.sfx_squash,1,1,0,0,1.0f);
            }
            else {
                Assets.tryPlaySound(context,R.raw.sfx_miss,false);
                //Assets.sp.play(Assets.sfx_miss,1,1,0,0,1.0f);
            }

            touched=false;
        }
    }

    private void render (Canvas canvas) {
        int xx, yy;
        LoadGraphics(canvas);
        // Fill the entire canvas' bitmap with 'black'
        canvas.drawColor(Color.BLACK);
        // Instantiate a Paint object
        Paint paint = new Paint();
        // Set the paint color to 'white'
        paint.setColor(Color.WHITE);
        // Draw a white circle at position (100, 100) with a radius of 100
        synchronized (lock) {
            if (!bug_squashed) {
                x = (x + 1) % canvas.getWidth();
                y = (y + 1) % canvas.getHeight();
            }
                xx = x;
                yy = y;

        }

        //FLOOR DID
        canvas.drawBitmap (tiles, 0, 0, null);


        long curTime = System.currentTimeMillis() / 100 % 10;
        if (!bug_squashed){
            if (curTime % 2 == 0)
                canvas.drawBitmap(bug1,xx,yy,null);
            else
                canvas.drawBitmap(bug2,xx,yy,null);
            //canvas.drawCircle(xx, yy, 100, paint);

        }
        else {
            canvas.drawBitmap(bug3,xx,yy,null);
        }
    }
}
