package com.github.nikit.cpp.player;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.util.UUID;

/**
 * Created by nik on 06.02.15.
 * Этот фрагмент внутри ViewPager'а
 */
public class PlaybackButtonsFragment extends Fragment {

    private Button mPlayButton;
    private Button mStopButton;
    private SeekBar mSeekBar;
    private Button mPauseButton;
    private UUID songId;

    public static final int MILISECONDS_UPDATE_INTERVAL = 200;
    private static Handler seekHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };


    public PlaybackButtonsFragment() {
        super();
        // Just to be an empty Bundle. You can use this later with getArguments().set...
        setArguments(new Bundle());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG_TAG, "PlaybackFragment.onCreate()" + ", address= " + toString());

        Bundle arguments = getArguments();
        songId = (UUID)arguments.getSerializable(Constants.SONG_ID);

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
        Log.d(Constants.LOG_TAG, "PlaybackFragment.onCreateView()" + ", address= " + toString());
        /**
         * Третий параметр указывает, нужно ли включать заполненное
         представление в родителя. Мы передаем false, потому что
         представление будет добавлено в коде активности.
         */
        View v = inflater.inflate(R.layout.fragment_playbuttons, parent, false);

        mPlayButton = (Button)v.findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PlayerService.class);
                i.putExtra(Constants.PLAYER_SERVICE_ACTION, PlayerService.Action.PLAY);
                i.putExtra(Constants.SONG_ID, songId);
                getActivity().startService(i);
            }
        });

        mStopButton = (Button)v.findViewById(R.id.stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PlayerService.class);
                i.putExtra(Constants.PLAYER_SERVICE_ACTION, PlayerService.Action.STOP);
                getActivity().startService(i);
            }
        });

        mSeekBar = (SeekBar) v.findViewById(R.id.playbackSeekBar);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( fromUser){
                    stopUpdation();
                    Intent i = new Intent(getActivity(), PlayerService.class);
                    i.putExtra(Constants.PLAYER_SERVICE_ACTION, PlayerService.Action.SEEK);
                    i.putExtra(Constants.SONG_SEEK_TO_POSITION, progress);
                    getActivity().startService(i);
                    seekUpdation();
                }
            }
        });

        mPauseButton = (Button) v.findViewById(R.id.pauseButton);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PlayerService.class);
                i.putExtra(Constants.PLAYER_SERVICE_ACTION, PlayerService.Action.PAUSE);
                getActivity().startService(i);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        seekUpdation();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopUpdation();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_playback, menu);
    }

    public void seekUpdation() {
        PlaybackActivity playbackActivity = (PlaybackActivity) getActivity();
        if (null!=playbackActivity) {
            playbackActivity.updateSeek();
            int position = playbackActivity.getCurrentPosition();
            int duration = playbackActivity.getDuration();
            mSeekBar.setMax(duration);
            Log.d(Constants.LOG_TAG, "Seeking to " + position + "/" + duration + ", PlaybackFragment address= " + toString());
            mSeekBar.setProgress(position);
            seekHandler.postDelayed(mRunnable, MILISECONDS_UPDATE_INTERVAL);
        }
    }

    public void stopUpdation() {
        seekHandler.removeCallbacksAndMessages(null);
    }

}
