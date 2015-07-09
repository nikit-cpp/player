package com.github.nikit.cpp.player;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by nik on 10.07.15.
 */
public class PollService extends IntentService {
    private static final String TAG = "PollService";
    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);
    }


}
