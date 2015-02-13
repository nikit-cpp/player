package com.github.nikit.cpp.player;

import android.support.v4.app.Fragment;

/**
 * Created by nik on 07.02.15.
 */
public class SongListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new SongListFragment();
    }
}
