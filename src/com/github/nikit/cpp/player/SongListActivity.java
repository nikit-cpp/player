package com.github.nikit.cpp.player;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by nik on 07.02.15.
 */
public class SongListActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }
}
