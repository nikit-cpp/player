package com.github.nikit.cpp.player.activities;

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
import com.github.nikit.cpp.player.Constants;
import com.github.nikit.cpp.player.dao.PlayListDao;
import com.github.nikit.cpp.player.adapters.PlaylistAdapter;
import com.github.nikit.cpp.player.R;
import com.github.nikit.cpp.player.model.PlayList;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.SelectListTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.sql.language.Select;

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
        Log.d(Constants.LOG_TAG, "PlaylistFragment.onCreate()");

        Activity activity = getActivity();
        activity.setTitle("playlists");

        FlowManager.init(getContext());

        //mPlaylists = PlayListDao.getPlaylists();
        TransactionListener<List<PlayList>> transactionListener = null;
        TransactionManager.getInstance().addTransaction(
                new SelectListTransaction<>(
                        new Select().from(PlayList.class),
                        new TransactionListener<List<PlayList>>( ) {
                            @Override
                            public void onResultReceived(List<PlayList> playLists) {
                                mPlaylists = playLists;
                            }

                            @Override
                            public boolean onReady(BaseTransaction<List<PlayList>> baseTransaction) {
                                return false;
                            }

                            @Override
                            public boolean hasResult(BaseTransaction<List<PlayList>> baseTransaction, List<PlayList> playLists) {
                                return false;
                            }
                        }
                )
        );
        adapter = new PlaylistAdapter(activity, mPlaylists);
        setListAdapter(adapter);
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
        Log.d("PlayList " + Constants.LOG_TAG, c.getName() + " was clicked");
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
        if (requestCode == Constants.REQUEST_PLAY_LIST) {
        // Обработка результата, который может вернуть запускаемая активити
        }
    }


}