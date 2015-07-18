package com.github.nikit.cpp.player;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nik on 18.07.15.
 */
public class SongAdapter extends ArrayAdapter<Song> {

    private List<Song> originalList;
    private List<Song> resultList;
    private SongFilter filter;
    private Activity parentActivity;

    public SongAdapter(Activity parentActivity, List<Song> originalList) {
        super(parentActivity, 0, new ArrayList<Song>(originalList));
        this.resultList = new ArrayList<Song>();
        this.resultList.addAll(originalList);
        this.originalList = originalList;
        this.parentActivity = parentActivity;
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
            convertView = parentActivity.getLayoutInflater().inflate(R.layout.list_item_song, null);
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
            Log.d(Tags.LOG_TAG, "SongListFragment...performFiltering(" + constraint + ")");
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0) {
                ArrayList<Song> filteredItems = new ArrayList<Song>();

                for(int i = 0, l = originalList.size(); i < l; i++) {
                    Song song = originalList.get(i);
                    if (song.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredItems.add(song);
                    }
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
        protected void publishResults(CharSequence constraint, FilterResults results) {

            resultList = (ArrayList<Song>) results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = resultList.size(); i < l; i++)
                add(resultList.get(i));
            notifyDataSetInvalidated();
        }
    }


}