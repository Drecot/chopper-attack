package com.drecot.chopperattack;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class Fuel extends GameObject {

        private Bitmap spritesheet;
        private double dya;
        private boolean playing;
        private long startTime;

        private Animation animation = new Animation();
        private int num = 0;



        public Fuel(Bitmap res, int w, int h, int numFrames) {
            x = GamePanel.WIDTH + 20;
            y = GamePanel.HEIGHT - GamePanel.HEIGHT / 4 - 200;
            dy = 0;
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
            animation.setDelay(100);
            animation.update();
        }
        public void update()
        {
            if (x < 0) {
                resetO();
            }
            x += dx;
            long elapsed = (System.nanoTime() - startTime) / 1000000;
            if (elapsed > 1000) {
                dx = dx - 1 ;
                startTime = System.nanoTime();
                System.out.println(dx);
                num = num + 30;
            }
            if (dx <= -25) {
                dx = -24;
            }
            if (num >= 200) {
                num = 200;
            }
        animation.update();
        }
        public void draw(Canvas canvas)
        {
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }
        public void resetO(){
            x = GamePanel.WIDTH + 20;
            y = GamePanel.HEIGHT - GamePanel.HEIGHT / 4 - 200;
            dy = 0;
            dx = +GamePanel.MOVESPEED;
            num = 0;

        }
        public void fuelCollected(){

            resetO();
        }


    }

