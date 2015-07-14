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
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nik on 07.02.15.
 */
public class SongListFragment extends ListFragment {

    private List<Song> mSongs;
    private SongAdapter adapter;

    private EditText incrementalSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Tags.LOG_TAG, "SongListFragment.onCreate()");

        getActivity().setTitle(R.string.app_name);

        mSongs = PlayerService.getCurrentPlaylist();
        adapter = new SongAdapter(mSongs);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.d(Tags.LOG_TAG, "SongListFragment.onCreateView()");
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
        Log.d(Tags.LOG_TAG, c.getName() + " was clicked");
        // Запуск CrimePagerActivity
        Intent i = new Intent(getActivity(), PlaybackActivity.class);
        i.putExtra(Tags.SONG_ID, c.getId());
        startActivity(i);
    }

    private class SongAdapter extends ArrayAdapter<Song> {

        private List<Song> originalList;
        private List<Song> resultList;
        private SongFilter filter;

        public SongAdapter(List<Song> originalList) {
            super(getActivity(), 0, new ArrayList<Song>(originalList));
            this.resultList = new ArrayList<Song>();
            this.resultList.addAll(originalList);
            this.originalList = originalList;
        }

        @Override
        public Filter getFilter() {
            if (filter == null){
                filter  = new SongFilter();
            }
            return filter;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            // Если мы не получили представление, заполняем его
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_song, null);
            }

            // Настройка представления для объекта Song
            Song c = getItem(position);
            TextView nameTextView = (TextView)convertView.findViewById(R.id.song_list_item_nameTextView);
            nameTextView.setText(c.getName());

            TextView artistTextView = (TextView)convertView.findViewById(R.id.song_list_item_artistTextView);
            artistTextView.setText(c.getArtist());

            return convertView;
        }


        private class SongFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d(Tags.LOG_TAG, "SongListFragment...performFiltering("+constraint+")");
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                if(constraint != null && constraint.toString().length() > 0) {
                    ArrayList<Song> filteredItems = new ArrayList<Song>();

                    for(int i = 0, l = originalList.size(); i < l; i++) {
                        Song song = originalList.get(i);
                        if(song.toString().toLowerCase().contains(constraint.toString().toLowerCase()))
                            filteredItems.add(song);
                    }
                    result.count = filteredItems.size();
                    result.values = filteredItems;
                } else {
                    synchronized(this) {
                        result.values = originalList;
                        result.count = originalList.size();
                    }
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                resultList = (ArrayList<Song>) results.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = resultList.size(); i < l; i++)
                    add(resultList.get(i));
                notifyDataSetInvalidated();
            }
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
        Log.d(Tags.LOG_TAG, "onActivityResult() requestCode="+requestCode + ", resultCode=" + resultCode);
        if (requestCode == Tags.REQUEST_SONG_LIST) {
        // Обработка результата
        }
    }


}