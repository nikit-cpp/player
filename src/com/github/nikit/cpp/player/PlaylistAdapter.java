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
 * Адаптирует плейлисты в список плейлистов
 * Created by nik on 18.07.15.
 */
public class PlaylistAdapter extends ArrayAdapter<PlayList> {

    private List<PlayList> originalList;
    private List<PlayList> resultList;
    private PlaylistFilter filter;
    private Activity parentActivity;

    public PlaylistAdapter(Activity parentActivity, List<PlayList> originalList) {
        super(parentActivity, 0, new ArrayList<PlayList>(originalList));
        this.resultList = new ArrayList<PlayList>();
        this.resultList.addAll(originalList);
        this.originalList = originalList;
        this.parentActivity = parentActivity;
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new PlaylistFilter();
        }
        return filter;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        // Если мы не получили представление, заполняем его
        if (convertView == null) {
            convertView = parentActivity.getLayoutInflater().inflate(R.layout.list_item_playlist, null);
        }

        // Настройка представления для объекта Playlist
        PlayList c = getItem(position);
        TextView nameTextView = (TextView)convertView.findViewById(R.id.play_list_item_nameTextView);
        nameTextView.setText(c.getName());

        TextView artistTextView = (TextView)convertView.findViewById(R.id.play_list_item_sourceView);
        artistTextView.setText(c.getSource());

        return convertView;
    }


    private class PlaylistFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(Constants.LOG_TAG, "PlaylistFragment...performFiltering(" + constraint + ")");
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0) {
                ArrayList<PlayList> filteredItems = new ArrayList<>();

                for(int i = 0, l = originalList.size(); i < l; i++) {
                    PlayList pl = originalList.get(i);
                    if (pl.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredItems.add(pl);
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

            resultList = (ArrayList<PlayList>) results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = resultList.size(); i < l; i++)
                add(resultList.get(i));
            notifyDataSetInvalidated();
        }
    }


}