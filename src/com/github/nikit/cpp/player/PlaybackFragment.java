package com.github.nikit.cpp.player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.UUID;

/**
 * Created by nik on 06.02.15.
 */
public class PlaybackFragment extends Fragment {
    public static final String EXTRA_CRIME_ID = "extra_crimeId";
    private Song mCrime;
    private TextView mSongName;
    private TextView mSongArtist;

    public static PlaybackFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);
        PlaybackFragment fragment = new PlaybackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);

        mCrime = SongFabric.get(getActivity()).getSong(crimeId);
    }

    /**
     * Экземпляр фрагмента настраивается в Fragment.onCreate(...),
     * но создание и настройка представления фрагмента осуществляются
     в другом методе жизненного цикла фрагмента:
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        /**
         * Третий параметр указывает, нужно ли включать заполненное
         представление в родителя. Мы передаем false, потому что
         представление будет добавлено в коде активности.
         */
        View v = inflater.inflate(R.layout.fragment_playback, parent, false);
        mSongName   = (TextView) v.findViewById(R.id.song_name);
        mSongArtist = (TextView) v.findViewById(R.id.song_artist);

        mSongName.setText(mCrime.getName());
        mSongArtist.setText(mCrime.getArtist());

        return v;
    }
}
