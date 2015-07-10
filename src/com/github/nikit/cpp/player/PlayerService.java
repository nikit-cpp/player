package com.github.nikit.cpp.player;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by nik on 10.07.15.
 */
public class PlayerService extends IntentService {
    public PlayerService() {
        super(Tags.PLAYER_SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Action action = (Action) intent.getExtras().get(Tags.PLAYER_SERVICE_ACTION);

        Log.i(Tags.LOG_TAG, "Received an intent: " + action);
    }

    public static enum Action{
        PLAY,
        PAUSE
    }

}
