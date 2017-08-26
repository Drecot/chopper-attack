package com.drecot.chopperattack;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * Created by Admin on 21/06/2017.
 */

public class SoundPlayer {

    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 2;


    private GamePanel gamePanel;
    private static SoundPool soundPool;
    private static int explosionSound;
    private static int fuelPositiveSound;
    private static int fuelNegativeSound;
    private static int bonusSound;

public SoundPlayer(Context context, GamePanel gamePanel ){

    this.gamePanel = gamePanel;


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(SOUND_POOL_MAX)
                .build();
    } else {

        //SoundPool (int maxStreams, int streamType, int srcQuality)
        soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);

    }



    explosionSound = soundPool.load(context, R.raw.expl, 1 );
    fuelPositiveSound = soundPool.load(context, R.raw.fuelpos, 1 );
    fuelNegativeSound = soundPool.load(context, R.raw.fuelneg, 1 );
    bonusSound = soundPool.load(context, R.raw.bonus,1);

}

    public void playExplosionSound() {
    //play(int,soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
    soundPool.play(explosionSound, 1.0f, 1.0f, 1, 0 , 1.0f);
}

    public void playPositiveSound(){
        soundPool.play(fuelPositiveSound, 1.0f, 1.0f, 1, 0 , 1.0f);
    }

    public void playNegativeSound(){
        soundPool.play(fuelNegativeSound, 1.0f, 1.0f, 1, 0 , 1.0f);
    }

    public void playbonusSound(){
        soundPool.play(bonusSound, 1.0f, 1.0f, 1, 0 , 1.0f);
    }


}
