package edu.uco.aadebayo.termproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Ayodeji on 10/14/2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = GamePanel.class.getSimpleName();
    private GameLoop thread;
    private Player player;
    //private Image backgroud;
    private double fps;

    // the fps to be displayed
    private String avgFps;
    public void setAvgFps(String avgFps) {
        this.avgFps = avgFps;
    }

    public GamePanel(Context context) {
        super(context);
        //adding the callback to the surface holder to intercept events
        getHolder().addCallback(this);

        //create player and load bitmap
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player), 150,150);

        //create game loop
        thread = new GameLoop(getHolder(),this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /*@Override
   / public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface destryed");
        //shutdown thread
        boolean retry = true;
        while (retry){
            try{
                thread.join();
                retry = false;
            }catch (InterruptedException e){
                //try again shutting down the thread
            }
        }
        Log.d(TAG, "Thread killed cleanly");
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            //make player handle event
            player.handleActionDown((int)event.getX(),(int)event.getY());
            //check if it is in the lower part of the screen
            if(event.getY() > getHeight() - 50){
                thread.setRunning(false);
                ((Activity)getContext()).finish();
            } else {
                Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
            }
        } if(event.getAction() == MotionEvent.ACTION_MOVE){
            //the gestures
            if(player.isTouched()){
                //move the player
                player.setX((int)event.getX());
                player.setY((int)event.getY());
            }
        }if(event.getAction() == MotionEvent.ACTION_UP){
            //touch was released
            if(player.isTouched()){
                player.setTouched(false);
            }
        }
        return true;
    }

    public void render(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        player.draw(canvas);
        // display fps
        displayFps(canvas, avgFps);
    }

    public void update(){
        //check collision with right wall if heading right
        if (player.getSpeed().getxDirection() == Speed.direction_right
                && player.getX() + player.getBitmap().getWidth() / 2 >= getWidth()) {
            player.getSpeed().toggleXDirection();
        }
        // check collision with left wall if heading left
        if (player.getSpeed().getxDirection() == Speed.direction_left
                && player.getX() - player.getBitmap().getWidth() / 2 <= 0) {
            player.getSpeed().toggleXDirection();
        }
        // check collision with bottom wall if heading down
        if (player.getSpeed().getyDirection() == Speed.direction_down
                && player.getY() + player.getBitmap().getHeight() / 2 >= getHeight()) {
            player.getSpeed().toggleYDirection();
        }
        // check collision with top wall if heading up
        if (player.getSpeed().getyDirection() == Speed.direction_up
                && player.getY() - player.getBitmap().getHeight() / 2 <= 0) {
            player.getSpeed().toggleYDirection();
        }
        // Update the lone player
        player.update();
    }


    private void displayFps(Canvas canvas, String fps) {
        if (canvas != null && fps != null) {
            Paint paint = new Paint();
            paint.setTextSize(50f);
            paint.setARGB(255, 255, 255, 255);
            canvas.drawText(fps, this.getWidth() -350, 200, paint);
            //this.fps = Double.parseDouble(fps);
        }
    }


}
