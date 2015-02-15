package com.github.nikit.cpp.player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by nik on 07.02.15.
 */
public class SongListActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twopane);

        /**
         * Может показаться странным, что FragmentManager идентифицирует CrimeFragment
         по идентификатору ресурса FrameLayout. Однако идентификация UI-фрагмента по
         идентификатору ресурса его контейнерного представления встроена в механизм
         работы FragmentManager.
         */
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.song_list);
        if (fragment == null) {
            fragment = new SongListFragment();
            fm.beginTransaction()
                    .add(R.id.song_list, fragment)
                    .commit();
        }

        fragment = fm.findFragmentById(R.id.incremental_search);
        if (fragment == null) {
            fragment = new SongListFragment();
            fm.beginTransaction()
                    .add(R.id.incremental_search, fragment)
                    .commit();
        }
    }

}
