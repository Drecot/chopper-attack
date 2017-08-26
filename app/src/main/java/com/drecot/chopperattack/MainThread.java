package com.drecot.chopperattack;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceHolder;


public class MainThread extends Thread
{
    private int FPS = 40;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    boolean isPaused;
    public static Canvas canvas;




    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel)
    {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;

    }



    @Override
    public void run()
    {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount =0;
        long targetTime = 1000/FPS;
        long sleepTime;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            if (isPaused)
            {
                try
                {
                    this.sleep(10);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }else {
                //try locking the canvas for pixel editing
                try {
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {


                        this.gamePanel.update();
                        this.gamePanel.draw(canvas);
                    }
                } catch (Exception e) {
                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }




            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime-timeMillis;

            try{
                this.sleep(waitTime);
            }catch(Exception e){}

            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if(frameCount == FPS)
            {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount =0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }
    }

    public void setPause(int i)
    {
        synchronized (gamePanel.getHolder())
        {


            if(i==0)
            {
                isPaused=false;
            }
            if(i==1)
            {
                isPaused = true;
            }
        }
    }

    public void setRunning(boolean b) {
        running=b;
    }
}