package com.github.nikit.cpp.player;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by nik on 10.07.15.
 */
public class PlayerService extends IntentService {
    public PlayerService() {
        super(Tags.PLAYER_SERVICE_NAME);
    }

    private static State state = State.NOT_INITIALIZED;

    private static MediaPlayer mPlayer;
    private static Song mSong;


    @Override
    protected void onHandleIntent(Intent intent) {
        Action action = (Action) intent.getExtras().get(Tags.PLAYER_SERVICE_ACTION);

        switch(action) {
            case PLAY:
                play((UUID) intent.getExtras().get(Tags.SONG_ID));
                break;
            case PAUSE:
                pause();
                break;
            case NEXT:
                next();
                break;
            case PREV:
                prev();
                break;
            case STOP:
                stop();
                break;
            case SEEK:
                break;
            case SET_SONG:
                break;
            case GET_CURRENT_INFO:
                ResultReceiver receiver = intent.getParcelableExtra(Tags.SEEK_RECEIVER);
                Bundle bundle = new Bundle();
                bundle.putInt(Tags.SONG_CURRENT_POSITION_KEY, getCurrentPosition());
                bundle.putInt(Tags.SONG_DURATION_KEY, getDuration());
                receiver.send(Tags.SONG_CURRENT_INFO_CODE, bundle);
                break;
        }

        Log.i(Tags.LOG_TAG, "Received an intent: " + action);
    }

    private int getCurrentPosition() {
        if(state == State.NOT_INITIALIZED)
            return Tags.SONG_NOT_INITIALIZED;
        return mPlayer.getCurrentPosition();
    }

    private int getDuration() {
        if(state == State.NOT_INITIALIZED)
            return Tags.SONG_NOT_INITIALIZED;
        return mPlayer.getDuration();
    }

    public static enum Action {
        PLAY,
        PAUSE,
        NEXT,
        PREV,
        STOP,
        SEEK,
        SET_SONG,
        GET_CURRENT_INFO,
        GET_DURATION
    }

    public static enum State {
        PLAYING,
        STOPPED,
        PAUSED,
        NOT_INITIALIZED
    }


    public void stop() {
        switch (state) {
            case PLAYING:
            case PAUSED:
                mPlayer.stop();
                mPlayer.reset();
                state = State.STOPPED;
        }
    }

    public void pause() {
        switch (state) {
            case PLAYING:
                mPlayer.pause();
                state = State.PAUSED;
        }
    }

    public void play(UUID songUuid) {
        try {
            switch (state) {
                case NOT_INITIALIZED:
                    mPlayer = new MediaPlayer();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            stop();
                        }
                    });
                case STOPPED:
                    mPlayer.setDataSource(SongFabric.get(null).getCurrentPlayList().getSong(songUuid).getFile().getAbsolutePath());// TODO FIXME
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mPlayer.prepare();
                    mPlayer.start();
                    break;
                case PLAYING:
                    Log.d(Tags.LOG_TAG, "Already playing");
                    break;
                case PAUSED:
                    mPlayer.start();
                    break;
            }
            state = State.PLAYING;
        } catch (IOException e) {
            Log.e(Tags.LOG_TAG,"Error in play()", e);
        }
    }

    public void seekTo(int progress) {
        if (mPlayer != null)
            mPlayer.seekTo(progress);
    }

    public void next() {

    }

    public void prev() {
    }
}
