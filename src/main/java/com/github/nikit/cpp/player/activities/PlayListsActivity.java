package com.github.nikit.cpp.player.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.github.nikit.cpp.player.Constants;
import com.github.nikit.cpp.player.R;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Активность, содпржащая список плейлистов
 * Created by nik on 14.07.15.
 */
public class PlayListsActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_playlists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_new_playlist) {
            Log.d(Constants.LOG_TAG, "Add pressed");
            FragmentManager fm = getSupportFragmentManager();
            AddPlaylistDialogFragment dialog = new AddPlaylistDialogFragment();
            dialog.setTargetFragment(PlayListsFragment.class.cast(Fragment.class), Constants.ADDING_PLAYLIST_CODE);

            dialog.show(fm, Constants.ADDING_PLAYLIST_TAG);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}