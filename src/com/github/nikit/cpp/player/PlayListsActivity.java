package com.github.nikit.cpp.player;

import android.app.Activity;
import android.os.Bundle;

/**
 * Активность, содпржащая список плейлистов
 * Created by nik on 14.07.15.
 */
public class PlayListsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);
    }
}