package com.drecot.chopperattack;


import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Fuelnegative extends GameObject {

    private Bitmap spritesheet;
    private long startTime;
    private Random random = new Random();
    private Animation animation = new Animation();
    private int maxBorderHeight = 856;


    public Fuelnegative(Bitmap res, int w, int h, int numFrames) {
        x = GamePanel.WIDTH + 2000;
        y = random.nextInt(GamePanel.HEIGHT-20);
        dy =0;
        dx = +GamePanel.MOVESPEED;
        height = h;
        width = w;


        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(100-dx);
        animation.update();
    }
    public void update()
    {
        if (x < 0) {
            reset();
        }
        x += dx;
        dx = dx- 1;

        if (dx <= -15) {
            dx = -15;
        }

        animation.update();
    }
    public void draw(Canvas canvas)
    {
        try {
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }catch (Exception e){}
    }
    public void reset(){
        x = GamePanel.WIDTH + 2000 ;
        y = random.nextInt(GamePanel.HEIGHT-20);
        dy = 0;
        dx = +GamePanel.MOVESPEED;
    }

    public void fuelCollectedNegative(){

        reset();
    }


}

