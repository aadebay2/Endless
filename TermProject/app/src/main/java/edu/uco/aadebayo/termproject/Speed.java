package edu.uco.aadebayo.termproject;

import android.graphics.Path;

/**
 * Created by Ayodeji on 10/22/2015.
 */
public class Speed {

    public static final int direction_right =1;
    public static final int direction_left = -1;
    public static final int direction_up = -1;
    public static final int direction_down = 1;

    private float velX = 1, velY =1; // velocity on x and y axis

    private int xDirection = direction_right;
    private int yDirection = direction_down;

    public Speed(){
        this.velY =0;
        this.velX = 2;
    }

    public Speed(float velX, float velY){
        this.velX = velX;
        this.velY = velY;
    }

    public float getVelX(){
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelX(float velX){
        this.velX = velX;
    }

    public void setVelY(float velY){
        this.velY = velY;
    }

    public int getxDirection() {
        return xDirection;
    }

    public int getyDirection() {
        return yDirection;
    }

    public void setxDirection(int xDirection) {
        this.xDirection = xDirection;
    }

    public void setyDirection(int yDirection) {
        this.yDirection = yDirection;
    }

    //change direction on X and Y-axis
    public void toggleXDirection(){
        xDirection = xDirection * -1;
    }

    public void toggleYDirection(){
        yDirection = yDirection * -1;
    }
}
