package com.github.nikit.cpp.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by nik on 15.02.15.
 */
public class AudioPlayer2 {

    private MediaPlayer mPlayer;

    static private AudioPlayer2 mAudioPlayer;
    private Context mAppContext;

    boolean isPaused = false;

    private AudioPlayer2(){
    }

    private AudioPlayer2(Context c){
        mAppContext = c;
    }

    public static AudioPlayer2 get(Context c) {
        if (mAudioPlayer == null) {
            mAudioPlayer = new AudioPlayer2(c.getApplicationContext());
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

    public void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
            isPaused = true;
        }
    }

    public void play(File file) {
        /**
         * Вызов stop() в начале play(Context) предотвращает возможное создание несколь-
         ких экземпляров MediaPlayer, если пользователь щелкнет на кнопке Play повторно.
         */
        if(!isPaused) {
            stop();

            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(file.getAbsolutePath());
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                Log.e(Tags.LOG_TAG, "Error on play", e);
            }

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    stop();
                }
            });
        }else if(mPlayer!=null) {
            mPlayer.start();
            isPaused = false;
        }
    }

    public void next() {

    }

    public  void prev() {

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