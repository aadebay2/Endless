package edu.uco.aadebayo.termproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Ayodeji on 10/22/2015.
 */
public class Player {

    private static final String TAG = Player.class.getSimpleName();
    private Bitmap bitmap; //the actual bitmap used for animation
    private Rect sourceRect;//the rectangle used to draw the animation
    private int frameNumber; // the number of frames in the animation
    private int currentFrame; //the current frame being displayed
    private long frameTime; // the milliseconds between each frame (1000/fps)
    private int x,y; // x and y coordinates
    private boolean touched;//if player is touched
    private Speed speed;

    public Player(Bitmap bitmap, int x, int y){
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.speed = new Speed();
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public Speed getSpeed() {
        return speed;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth()/2), y - (bitmap.getHeight()/2), null);
    }

    public void handleActionDown(int eventX, int eventY){
        if(eventX >=(x - bitmap.getWidth()/2) && (eventX <= (x + bitmap.getWidth()/2))) {
            if (eventY >= (y - bitmap.getHeight() / 2) && (y <= (y + bitmap.getHeight() / 2))) {
                //player touched
                setTouched(true);
            } else {
                setTouched(false);
            }
        }else {
            setTouched(false);
        }
    }

    public void update(){
        if(!touched){
            x += (speed.getVelX() * speed.getxDirection());
            y += (speed.getVelY() * speed.getyDirection());
        }
    }
}
