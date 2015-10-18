package com.github.nikit.cpp.player.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.ListView;
import com.github.nikit.cpp.player.Constants;
import com.github.nikit.cpp.player.adapters.PlaylistAdapter;
import com.github.nikit.cpp.player.R;
import com.github.nikit.cpp.player.model.PlayList;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.SelectListTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListenerAdapter;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nik on 07.02.15.
 */
public class PlaylistFragment extends ListFragment {

    private List<PlayList> mPlaylists = new ArrayList<>();
    private PlaylistAdapter adapter;

    private EditText incrementalSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG_TAG, "PlaylistFragment.onCreate()");

        setHasOptionsMenu(true);

        Activity activity = getActivity();
        activity.setTitle("playlists");

        FlowManager.init(getContext());

        adapter = new PlaylistAdapter(activity, mPlaylists);
        setListAdapter(adapter);

        updatePlaylists();
    }

    private void updatePlaylists() {
        TransactionManager.getInstance().addTransaction(
                new SelectListTransaction<>(
                        new Select().from(PlayList.class),
                        new TransactionListenerAdapter<List<PlayList>>( ) {
                            @Override
                            public void onResultReceived(List<PlayList> playLists) {
                                Log.d(Constants.LOG_TAG, "getted " + playLists.size() + " playlists");
                                adapter.updateList(playLists);
                            }
                        }
                )
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "PlaylistFragment.onCreateView()");
        View v = inflater.inflate(R.layout.fragment_list, null);

        incrementalSearch = (EditText) v.findViewById(R.id.editText);
        incrementalSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                PlaylistFragment.this.adapter
                        .getFilter().filter(cs);
            }
        });
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        PlayList c = ((PlaylistAdapter)getListAdapter()).getItem(position);
        Log.d(Constants.LOG_TAG, "PlayList " + c.getName() + " was clicked");
        // Запуск Activity
        Intent i = new Intent(getActivity(), SongListActivity.class);

        i.putExtra(Constants.PLAYLIST_ID, position); // передаём номер плейлиста
        startActivity(i);
    }

    /**
     * Так надо.
     */
    @Override
    public void onResume() {
        super.onResume();
        ((PlaylistAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(Constants.LOG_TAG, "onActivityResult() requestCode="+requestCode + ", resultCode=" + resultCode);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == Constants.REQUEST_PLAY_LIST) {
            // Обновить список плейлистов
            Log.d(Constants.LOG_TAG, "Refreshing list of playlists");
            updatePlaylists();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_playlists, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_new_playlist) {
            Log.d(Constants.LOG_TAG, "Add pressed");
            FragmentManager fm = getActivity().getSupportFragmentManager();
            AddPlaylistDialogFragment dialog = new AddPlaylistDialogFragment();
            dialog.setTargetFragment(PlaylistFragment.this, Constants.ADDING_PLAYLIST_CODE);

            dialog.show(fm, Constants.ADDING_PLAYLIST_TAG);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}