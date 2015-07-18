package com.github.nikit.cpp.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

/**
 * Created by nik on 07.02.15.
 */
public class PlaylistFragment extends ListFragment {

    private List<PlayList> mPlaylists;
    private PlaylistAdapter adapter;

    private EditText incrementalSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Tags.LOG_TAG, "PlaylistFragment.onCreate()");

        Activity activity = getActivity();
        activity.setTitle("playlists");

        mPlaylists = PlayListManager.getPlaylists();
        adapter = new PlaylistAdapter(activity, mPlaylists);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.d(Tags.LOG_TAG, "PlaylistFragment.onCreateView()");
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
        Log.d("PlayList " + Tags.LOG_TAG, c.getName() + " was clicked");
        // Запуск Activity
        Intent i = new Intent(getActivity(), SongListActivity.class);

        i.putExtra(Tags.PLAYLIST_ID, position); // передаём номер плейлиста
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
        Log.d(Tags.LOG_TAG, "onActivityResult() requestCode="+requestCode + ", resultCode=" + resultCode);
        if (requestCode == Tags.REQUEST_PLAY_LIST) {
        // Обработка результата, который может вернуть запускаемая активити
        }
    }


}