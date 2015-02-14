package com.github.nikit.cpp.player;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by nik on 15.02.15.
 */
public class AudioPlayer {
    private MediaPlayer mPlayer;

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

    public void play(Context c) {
        /**
         * Вызов stop() в начале play(Context) предотвращает возможное создание несколь-
         ких экземпляров MediaPlayer, если пользователь щелкнет на кнопке Play повторно.
         */
        stop();

        mPlayer = MediaPlayer.create(c, R.raw.explosion);
        mPlayer.start();

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });

    }
}