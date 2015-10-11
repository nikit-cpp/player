package com.github.nikit.cpp.player.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.github.nikit.cpp.player.R;

/**
 * Created by nik on 07.02.15.
 */
public class SongListActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
    }
}
