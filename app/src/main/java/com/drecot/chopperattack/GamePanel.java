package com.drecot.chopperattack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;

import android.util.Log;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;


import java.util.ArrayList;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;


public class GamePanel extends GLSurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;
    private long smokeStartTime;
    private long missileStartTime;
    private MainThread thread;
    private Background bg;
    protected Player player;
    private boolean playing;
    private Fuelsmall fuelsmall;
    private Multiplier multiplier;
    private Fuelbig fuelbig;
    private Fuelnegative fuelnegative;
    private Game game;
    private ArrayList<Smokepuff> smoke;
    private ArrayList<Missile> missiles;
    private ArrayList<TopBorder> topborder;
    private ArrayList<BotBorder> botborder;
    private Random rand = new Random();
    private int maxBorderHeight;
    private int minBorderHeight;
    private boolean topDown = true;
    private boolean botDown = true;
    private boolean newGameCreated;
    private SoundPlayer sp;
    private int sound = 1;
    Bitmap note1,note2;
    int score;
    int volume;
    int highScore[] = new int[5];
    int dead = 0;
    SharedPreferences sharedPreferences;

    //increase to slow down difficulty progression, decrease to speed up difficulty progression
    private int progressDenom = 20;

    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean disappear;
    private boolean started;
    private boolean running;
    private int best;

    public GamePanel(Context context)
    {
        super(context);
        score = 0;


        sharedPreferences = getContext().getSharedPreferences("SHAR_PREF_NAME",Context.MODE_PRIVATE);

//initializing the array high scores with the previous values
        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);
        highScore[3] = sharedPreferences.getInt("score4",0);
        highScore[4] = sharedPreferences.getInt("score5",0);
        score = sharedPreferences.getInt("score6",0);

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        thread.setPause(1);
        while(retry && counter<1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            }catch(InterruptedException e){e.printStackTrace();}

        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter), 74, 27, 3);
        fuelsmall = new Fuelsmall(BitmapFactory.decodeResource(getResources(), R.drawable.fuelsmall),20,20,1);
        fuelbig = new Fuelbig(BitmapFactory.decodeResource(getResources(), R.drawable.fuelbig),40,40,1);
        fuelnegative = new Fuelnegative(BitmapFactory.decodeResource(getResources(), R.drawable.fuelnegative),20,20,1);
        multiplier = new Multiplier(BitmapFactory.decodeResource(getResources(), R.drawable.multiply),30,30,1);


        smoke = new ArrayList<Smokepuff>();
        missiles = new ArrayList<Missile>();
        topborder = new ArrayList<TopBorder>();
        botborder = new ArrayList<BotBorder>();
        smokeStartTime=  System.nanoTime();
        missileStartTime = System.nanoTime();


        sp = new SoundPlayer(getContext(), this);


        thread = new MainThread(getHolder(), this);
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();
    }

    public void onPause() {
        thread.setPause(1);
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }
    public void onResume() {

        thread.setPause(0);
        thread = new MainThread(getHolder(),this);
        thread.start();
    }


        @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        if(event.getAction()==MotionEvent.ACTION_DOWN){

            if(!player.getPlaying() && newGameCreated && reset)
            {
                player.setPlaying(true);
                player.setUp(true);

            }


            if(player.getPlaying())
            {

                if(!started)


                    started = true;

                reset = false;
                player.setUp(true);
                player.fuel-=50;

                if (player.fuel<=0) {
                    player.setUp(false);
                }



            }
            return true;

        }
        if(event.getAction()==MotionEvent.ACTION_UP)
        {
            player.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update()

    {

            if(player.getPlaying()) {
                if (score > getBestScore()) {
                    setBestScore(score);
                }

            if(botborder.isEmpty())
            {
                player.setPlaying(false);
                return;
            }
            if(topborder.isEmpty())
            {
                player.setPlaying(false);
                return;
            }

            bg.update();
            player.update();
            fuelsmall.update();
            fuelbig.update();
            fuelnegative.update();
            multiplier.update();

            score++;
                SharedPreferences.Editor e = sharedPreferences.edit();
                    e.putInt("score6",score);

                e.apply();
            if(collectFuelSmall(player, fuelsmall)){
                if (sound ==1){
                    sp.playPositiveSound();
                }
                player.fuel +=500;
            }
            if(collectFuelBig(player, fuelbig)){
                if (sound ==1){

                    sp.playPositiveSound();
                }
                player.fuel +=1000;
            }
            if(collectFuelNegative(player, fuelnegative)){
                if (sound ==1){

                    sp.playNegativeSound();
                }
                player.fuel -= 250;

            }

            if(collectBonus (player, multiplier)){
                if (sound ==1){
                    sp.playbonusSound();
                }

                  score+=2500;

            }


            //update top border
            this.updateTopBorder();

            //update bottom border
            this.updateBottomBorder();

            //add missiles on timer
            long missileElapsed = (System.nanoTime()-missileStartTime)/1000000;
            if(missileElapsed >(2000 - player.getScore()/4)){


                //first missile always goes down the middle
                if(missiles.size()==0)
                {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.
                            missile),WIDTH + 10, HEIGHT/2, 38, 14, player.getScore(), 2));
                }
                else
                {

                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.missile),
                            WIDTH+10, (int)(rand.nextDouble()*(HEIGHT - (maxBorderHeight * 2))+maxBorderHeight),38,14, player.getScore(),2));
                }

                //reset timer
                missileStartTime = System.nanoTime();
            }
           gameend();

            //add smoke puffs on timer
            long elapsed = (System.nanoTime() - smokeStartTime)/1000000;
            if(elapsed > 120){
                smoke.add(new Smokepuff(player.getX(), player.getY()+10));
                smokeStartTime = System.nanoTime();
            }

            for(int i = 0; i<smoke.size();i++)
            {
                smoke.get(i).update();
                if(smoke.get(i).getX()<-10)
                {
                    smoke.remove(i);
                }
            }
        }

        else{
            player.resetDY();


            if(!reset)
            {
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                disappear = true;
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion),player.getX(),
                        player.getY()-30, 100, 100, 25);
                player.fuel=3500;




                for(int i=0;i<5;i++){
                    if(highScore[i]<score){

                        final int finalI = i;
                        highScore[i] = score ;
                        break;
                    }

                }
//storing the scores through shared Preferences
                SharedPreferences.Editor e = sharedPreferences.edit();
                for(int i=0;i<5;i++){
                    int j = i+1;
                    e.putInt("score"+j,highScore[i]);
                }
                e.apply();



            }


            explosion.update();
            long resetElapsed = (System.nanoTime()-startReset)/1000000;


            if(resetElapsed > 2500 && !newGameCreated)
            {
                newGame();

            }

        }

    }

    public void gameend(){


        //loop through every missile and check collision and remove
        for(int i = 0; i<missiles.size();i++)
        {
            //update missile
            missiles.get(i).update();

            if(collision(missiles.get(i),player))
            {
                    Intent gameOver = new Intent(getContext(), GameOver.class);
                    getContext().startActivity(gameOver);
                missiles.remove(i);
                player.setPlaying(false);
                if (sound ==1){
                    sp.playExplosionSound();

                }

                break;

            }
            //remove missile if it is way off the screen
            if(missiles.get(i).getX()<-100)
            {
                missiles.remove(i);
                break;
            }
        }

        for(int i = 0; i <topborder.size(); i++)
        {
            topborder.get(i).update();
            if(collision(topborder.get(i),player)){

                    Intent gameOver = new Intent(getContext(), GameOver.class);

                    getContext().startActivity(gameOver);


                player.setPlaying(false);
                if (sound ==1){
                    sp.playExplosionSound();
                }

break;

            }
        }

        for(int i = 0; i<botborder.size(); i++)
        {
            botborder.get(i).update();
            if(collision(botborder.get(i), player)) {
                    Intent gameOver = new Intent(getContext(), GameOver.class);

                    getContext().startActivity(gameOver);

                if (sound ==1){
                    sp.playExplosionSound();
                }

                player.setPlaying(false);
                break;
            }
        }
    }


    public boolean collision(GameObject a, GameObject b)
    {

        return Rect.intersects(a.getRectangle(), b.getRectangle());
    }
    public boolean collectFuelSmall(GameObject player, GameObject fuelsmall){
        if(Rect.intersects(player.getRectangle(),fuelsmall.getRectangle()))
        {
            fuelCollectedSmall();
            return true;
        }
        return false;
    }

    public boolean collectFuelBig(GameObject player, GameObject fuelbig) {
        if (Rect.intersects(player.getRectangle(), fuelbig.getRectangle()))
        {
            fuelCollectedBig();
            return true;
        }
        return false;
    }

    public boolean collectFuelNegative(GameObject player, GameObject fuelnegative) {
        if (Rect.intersects(player.getRectangle(), fuelnegative.getRectangle()))
        {
            fuelCollectedNegative();

            return true;
        }
        return false;
    }

    public boolean collectBonus(GameObject player, GameObject multiplier) {
        if (Rect.intersects(player.getRectangle(), multiplier.getRectangle()))
        {
            collectMultiplier();

            return true;
        }
        return false;
    }

    public void fuelCollectedSmall(){fuelsmall.fuelCollectedSmall();}

    public void fuelCollectedBig(){

        fuelbig.fuelCollectedBig();}

    public void fuelCollectedNegative(){

        fuelnegative.fuelCollectedNegative();}

    public void collectMultiplier(){

        multiplier.collectMultiplier();}




    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

            //volume
            SharedPreferences pref = getContext().getSharedPreferences("higher", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            volume = pref.getInt("vloume", 1);
            if (volume == 0) {
                sound = 0;
            }

            final float scaleFactorX = getWidth() / (WIDTH * 1.f);
            final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

            if (canvas != null) {
                final int savedState = canvas.save();
                canvas.scale(scaleFactorX, scaleFactorY);
                bg.draw(canvas);


                if (!disappear) {
                    player.draw(canvas);
                }
                // draw fuelsmall can
                fuelsmall.draw(canvas);

                //draw fuelbig can
                fuelbig.draw(canvas);

                //draw fuelnegative can
                fuelnegative.draw(canvas);

                multiplier.draw(canvas);

                //draw smokepuffs
                for (Smokepuff sp : smoke) {
                    sp.draw(canvas);
                }
                //draw missiles
                for (Missile m : missiles) {
                    m.draw(canvas);
                }


                //draw topborder
                for (TopBorder tb : topborder) {
                    tb.draw(canvas);
                }

                //draw botborder
                for (BotBorder bb : botborder) {
                    bb.draw(canvas);
                }
                //draw explosion
                if (started) {
                    explosion.draw(canvas);
                }
                drawText(canvas);
                canvas.restoreToCount(savedState);

            }
        }


    public void updateTopBorder()
    {
        //every 50 points, insert randomly placed top blocks that break the pattern
        if(player.getScore()%40 ==0)
        {
            topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick
            ),topborder.get(topborder.size()-1).getX()+20,0,(int)((rand.nextDouble()*(maxBorderHeight
            ))+1)));
        }
        for(int i = 0; i<topborder.size(); i++)
        {
            topborder.get(i).update();
            if(topborder.get(i).getX()<-20)
            {
                topborder.remove(i);
                //remove element of arraylist, replace it by adding a new one

                //calculate topdown which determines the direction the border is moving (up or down)
                if(topborder.get(topborder.size()-1).getHeight()>=maxBorderHeight)
                {
                    topDown = false;
                }
                if(topborder.get(topborder.size()-1).getHeight()<=minBorderHeight)
                {
                    topDown = true;
                }
                //new border added will have larger height
                if(topDown)
                {
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.brick),topborder.get(topborder.size()-1).getX()+20,
                            0, topborder.get(topborder.size()-1).getHeight()));
                }
                //new border added wil have smaller height
                else
                {
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.brick),topborder.get(topborder.size()-1).getX()+20,
                            0, topborder.get(topborder.size()-1).getHeight()));
                }

            }
        }

    }

    public void updateBottomBorder()
    {
        //every 40 points, insert randomly placed bottom blocks that break pattern
        if(player.getScore()%40 == 0)
        {
            botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                    botborder.get(botborder.size()-1).getX()+20,(int)((rand.nextDouble()
                    *maxBorderHeight)+(HEIGHT-maxBorderHeight))));
        }

        //update bottom border
        for(int i = 0; i<botborder.size(); i++)
        {
            botborder.get(i).update();

            //if border is moving off screen, remove it and add a corresponding new one
            if(botborder.get(i).getX()<-20) {
                botborder.remove(i);


                //determine if border will be moving up or down
                if (botborder.get(botborder.size() - 1).getY() <= HEIGHT-maxBorderHeight) {
                    botDown = true;
                }
                if (botborder.get(botborder.size() - 1).getY() >= HEIGHT - minBorderHeight) {
                    botDown = false;
                }

                if (botDown) {
                    botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
                    ), botborder.get(botborder.size() - 1).getX() + 20, botborder.get(botborder.size() - 1
                    ).getY() ));
                } else {
                    botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
                    ), botborder.get(botborder.size() - 1).getX() + 20, botborder.get(botborder.size() - 1
                    ).getY() ));
                }
            }
        }
    }

    public void newGame()
    {

        disappear = false;

        botborder.clear();
        topborder.clear();

        missiles.clear();
        smoke.clear();
        fuelsmall.reset();
        fuelbig.reset();
        fuelnegative.reset();
        multiplier.reset();

        minBorderHeight = 0;
        maxBorderHeight = 0;

        player.resetDY();
        player.resetScore();
        score=0;
        player.setY(HEIGHT/2);
        player.fuel = 3500;

        //create initial borders

        //initial top border
        for(int i = 0; i*20<WIDTH+40;i++)
        {
            //first top border create
            if(i==0)
            {
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick
                ),i*20,0, 10));
            }
            else
            {
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick
                ),i*20,0, topborder.get(i-1).getHeight()));
            }
        }
        //initial bottom border
        for(int i = 0; i*20<WIDTH+40; i++)
        {
            //first border ever created
            if(i==0)
            {
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick)
                        ,i*20,HEIGHT - minBorderHeight));
            }
            //adding borders until the initial screen is filed
            else
            {
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                        i * 20, botborder.get(i - 1).getY()));
            }
        }

        newGameCreated = true;


    }

    public void drawText(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(255,211,38));
        paint.setTextSize(15);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        canvas.drawText("FUEL: " + player.getFuel(), 10, HEIGHT - 10, paint);
        canvas.drawText("BEST: " + getBestScore(), WIDTH - 215, HEIGHT - 10, paint);
        canvas.drawText("SCORE: " + score, 350, HEIGHT - 10, paint);
        paint.setStyle(Paint.Style.FILL);


        if(!player.getPlaying()&&newGameCreated&&reset)
        {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/GoodDog.otf");
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTextAlign(Paint.Align.CENTER);
            paint1.setTypeface(tf);

            canvas.drawText("PRESS TO START", WIDTH-300, HEIGHT/2, paint1);

        }
        if(player.getPlaying() && collectFuelNegative(player, fuelsmall)){
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/GoodDog.otf");
            Paint paint2 = new Paint();
            paint2.setTextSize(40);
            paint2.setTextAlign(Paint.Align.CENTER);
            paint2.setTypeface(tf);
            canvas.drawText("+ 500", WIDTH-300, HEIGHT/2, paint2);

        }




    }


    private void setBestScore(int bestScore) {
        SharedPreferences.Editor editor = getContext().getSharedPreferences("gamepanel", MODE_PRIVATE).edit();
        editor.putInt("bestScore", bestScore);

        editor.apply();
    }


    protected int getBestScore() {
        SharedPreferences prefs = getContext().getSharedPreferences("gamepanel", MODE_PRIVATE);
        return prefs.getInt("bestScore", 0);


    }

}
