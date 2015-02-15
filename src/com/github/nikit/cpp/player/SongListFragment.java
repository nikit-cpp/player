package com.github.nikit.cpp.player;

import android.content.Context;
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

/**
 * Created by nik on 07.02.15.
 */
public class SongListFragment extends ListFragment {

    public static final String TAG = PlaybackPagerActivity.TAG;

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

    /*private class SongAdapter extends ArrayAdapter<Song> {
        public SongAdapter(ArrayList<Song> songs){
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
    }*/

    private class SongAdapter extends ArrayAdapter<Song> {

        private ArrayList<Song> originalList;
        private ArrayList<Song> countryList;
        private CountryFilter filter;

        public SongAdapter(ArrayList<Song> countryList) {
            super(getActivity(), 0, countryList);
            this.countryList = new ArrayList<Song>();
            this.countryList.addAll(countryList);
            this.originalList = new ArrayList<Song>();
            this.originalList.addAll(countryList);
        }

        @Override
        public Filter getFilter() {
            if (filter == null){
                filter  = new CountryFilter();
            }
            return filter;
        }


        private class ViewHolder {
            TextView code;
            TextView name;
            TextView continent;
            TextView region;
        }

        /*@Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));
            if (convertView == null) {

                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.country_info, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.continent = (TextView) convertView.findViewById(R.id.continent);
                holder.region = (TextView) convertView.findViewById(R.id.region);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Country country = countryList.get(position);
            holder.code.setText(country.getCode());
            holder.name.setText(country.getName());
            holder.continent.setText(country.getContinent());
            holder.region.setText(country.getRegion());

            return convertView;

        }*/
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


        private class CountryFilter extends Filter
        {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d(PlaybackPagerActivity.TAG, "SongListFragment...performFiltering");
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                if(constraint != null && constraint.toString().length() > 0)
                {
                    ArrayList<Song> filteredItems = new ArrayList<Song>();

                    for(int i = 0, l = originalList.size(); i < l; i++)
                    {
                        Song country = originalList.get(i);
                        if(country.toString().toLowerCase().contains(constraint))
                            filteredItems.add(country);
                    }
                    result.count = filteredItems.size();
                    result.values = filteredItems;
                }
                else
                {
                    synchronized(this)
                    {
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

                countryList = (ArrayList<Song>)results.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = countryList.size(); i < l; i++)
                    add(countryList.get(i));
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
        Log.d(TAG, "onActivityResult() requestCode="+requestCode + ", resultCode=" + resultCode);
        if (requestCode == REQUEST_CRIME) {
        // Обработка результата
        }
    }


}