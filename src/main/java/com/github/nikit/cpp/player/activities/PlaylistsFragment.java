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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.github.nikit.cpp.player.Constants;
import com.github.nikit.cpp.player.adapters.PlaylistAdapter;
import com.github.nikit.cpp.player.R;
import com.github.nikit.cpp.player.dao.AbstractDAO;
import com.github.nikit.cpp.player.dao.PlayListDAO;
import com.github.nikit.cpp.player.model.PlayList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nik on 07.02.15.
 */
public class PlaylistsFragment extends ListFragment {

    private List<PlayList> mPlaylists = new ArrayList<>();
    private PlaylistAdapter adapter;

    private EditText incrementalSearch;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        registerForContextMenu(getListView());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG_TAG, "PlaylistsFragment.onCreate()");

        setHasOptionsMenu(true);

        Activity activity = getActivity();
        activity.setTitle("playlists");

        AbstractDAO.init(getContext());

        adapter = new PlaylistAdapter(activity, mPlaylists);
        setListAdapter(adapter);

        updatePlaylists();
    }


    private void updatePlaylists() {
        PlayListDAO.updatePlaylists(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "PlaylistsFragment.onCreateView()");
        View v = inflater.inflate(R.layout.fragment_list, null);

        incrementalSearch = (EditText) v.findViewById(R.id.editText);
        incrementalSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                PlaylistsFragment.this.adapter
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
            dialog.setTargetFragment(PlaylistsFragment.this, Constants.ADDING_PLAYLIST_CODE);

            dialog.show(fm, Constants.ADDING_PLAYLIST_TAG);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(Constants.LOG_TAG, "onCreateContextMenu() " + menu + " " + v+ " " + menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.playlists_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = info.position;

        Log.d(Constants.LOG_TAG, item.getTitle() + " for " + pos);

        return super.onContextItemSelected(item);
    }
}