package com.github.nikit.cpp.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

/**
 * Created by nik on 15.02.15.
 */
public class AudioPlayer {

    private MediaPlayer mPlayer;

    static private AudioPlayer mAudioPlayer;
    private Context mAppContext;


    private AudioPlayer(){
    }

    private AudioPlayer(Context c){
        mAppContext = c;
    }

    public static AudioPlayer get(Context c) {
        if (mAudioPlayer == null) {
            mAudioPlayer = new AudioPlayer(c.getApplicationContext());
        }
        return mAudioPlayer;
    }

    /**
     * При простом воспроизведении аудиоданных корректнее
     уничтожить экземпляр вызовом release() , а потом создать его заново.

     */
    public void stop() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void play(File file) {
        /**
         * Вызов stop() в начале play(Context) предотвращает возможное создание несколь-
         ких экземпляров MediaPlayer, если пользователь щелкнет на кнопке Play повторно.
         */
        stop();

        //mPlayer = MediaPlayer.create(c, R.raw.explosion);
        //mPlayer.start();
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(file.getAbsolutePath());
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });

    }

    public int getDuration() {
        if(mPlayer==null)
            return 0;
        return mPlayer.getDuration();
    }

    public int getCurrentPosition() {
        if(mPlayer==null)
            return 0;
        return mPlayer.getCurrentPosition();
    }

    public void seekTo(int progress) {
        if(mPlayer!=null)
            mPlayer.seekTo(progress);
    }
}