package com.github.nikit.cpp.player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;

import java.util.UUID;

/**
 * Created by nik on 06.02.15.
 */
public class PlaybackFragment extends Fragment {
    public static final String EXTRA_CRIME_ID = "extra_crimeId";
    private Song mSong;
    private TextView mSongName;
    private TextView mSongArtist;

    private AudioPlayer mPlayer = new AudioPlayer();
    private Button mPlayButton;
    private Button mStopButton;


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

        mSong = SongFabric.get(getActivity()).getSong(crimeId);
        /* Вызов setRetainInstance(true) сохраняет фрагмент,
        который не уничтожается вместе с активностью, а передается новой активности
        в неизменном виде.
        */
        setRetainInstance(true);

        /**
         * сообщите FragmentManager, что экземпляр
         PlaybackFragment должен получать обратные вызовы командного меню.
         */
        setHasOptionsMenu(true);
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

        mSongName.setText(mSong.getName());
        mSongArtist.setText(mSong.getArtist());

        mPlayButton = (Button)v.findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPlayer.play(getActivity());
            }
        });

        mStopButton = (Button)v.findViewById(R.id.stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPlayer.stop();
            }
        });

        return v;
    }

    /**
     * В PlaybackFragment.onDestroy() освобождается экземпляр Media-
     Player, что приводит к остановке воспроизведения.

     */


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_playback, menu);
    }

}
