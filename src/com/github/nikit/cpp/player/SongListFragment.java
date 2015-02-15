package com.github.nikit.cpp.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nik on 07.02.15.
 */
public class SongListFragment extends ListFragment {

    public static final String TAG = "CRIMINAL_TAG";

    private ArrayList<Song> mSongs;
    private SongAdapter adapter;

    private static final int REQUEST_CRIME = 1;
    private EditText incrementalSearch;

    /**
     * Мы не будем переопределять onCreateView(...) или заполнять макет CrimeList-
     Fragment.

     Реализация ListFragment по умолчанию заполняет макет, определяю-
     щий полноэкранный виджет ListView; пока мы будем использовать этот макет.

     В следующих главах мы переопределим CrimeListFragment.onCreateView(...) для
     расширения функциональности приложения.

     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(PlaybackPagerActivity.TAG, "SongListFragment.onCreate()");

        getActivity().setTitle(R.string.app_name);

        mSongs = SongFabric.get(getActivity()) .getSongs();
        adapter = new SongAdapter(mSongs);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        Log.d(PlaybackPagerActivity.TAG, "SongListFragment.onCreateView()");
        View v = inflater.inflate(R.layout.fragment_list, null);

        incrementalSearch = (EditText) v.findViewById(R.id.editText);
        incrementalSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                SongListFragment.this.adapter
                        .getFilter().filter(cs);
            }
        });
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Song c = ((SongAdapter)getListAdapter()).getItem(position);
        Log.d(TAG, c.getName() + " was clicked");
        // Запуск CrimePagerActivity
        Intent i = new Intent(getActivity(), PlaybackPagerActivity.class);
        i.putExtra(PlaybackFragment.EXTRA_CRIME_ID, c.getId());
        startActivity(i);
    }

    private class SongAdapter extends ArrayAdapter<Song> {
        public SongAdapter(ArrayList<Song> songs){
            /**
             * Мы не будем использовать предопределенный макет, поэтому вместо идентифика-
             тора макета можно передать 0.
             */
            super(getActivity(), 0, songs);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            // Если мы не получили представление, заполняем его
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_song, null);
            }

            // Настройка представления для объекта Song
            Song c = getItem(position);
            TextView nameTextView =
                    (TextView)convertView.findViewById(R.id.song_list_item_nameTextView);
            nameTextView.setText(c.getName());

            TextView artistTextView =
                    (TextView)convertView.findViewById(R.id.song_list_item_artistTextView);
            artistTextView.setText(c.getArtist());

            return convertView;
        }
    }

    /**
     * Так надо.
     */
    @Override
    public void onResume() {
        super.onResume();
        ((SongAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult() requestCode="+requestCode + ", resultCode=" + resultCode);
        if (requestCode == REQUEST_CRIME) {
        // Обработка результата
        }
    }


}