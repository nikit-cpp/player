package com.github.nikit.cpp.player;

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
    private Song mSong;

    private AudioPlayer mPlayer;
    private Button mPlayButton;
    private Button mStopButton;
    private SeekBar mSeekBar;
    private Button mPauseButton;

    private Handler seekHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(PlaybackActivity.TAG, "PlaybackFragment.onCreate()" + ", address= " + toString());

        Bundle arguments = getArguments();
        UUID songId = (UUID)arguments.getSerializable(SongListFragment.SONG_ID);

        mPlayer = AudioPlayer.get(this.getActivity());

        mSong = SongFabric.get(getActivity()).getCurrentPlayList().getSong(songId);
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
        Log.d(PlaybackActivity.TAG, "PlaybackFragment.onCreateView()" + ", address= " + toString());
        /**
         * Третий параметр указывает, нужно ли включать заполненное
         представление в родителя. Мы передаем false, потому что
         представление будет добавлено в коде активности.
         */
        View v = inflater.inflate(R.layout.fragment_playbuttons, parent, false);

        mPlayButton = (Button)v.findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPlayer.play(mSong.getFile());
                seekUpdation();
            }
        });

        mStopButton = (Button)v.findViewById(R.id.stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPlayer.stop();
                stopUpdation();
            }
        });

        mSeekBar = (SeekBar) v.findViewById(R.id.playbackSeekBar);
        seekUpdation();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mPlayer != null && fromUser){
                    mPlayer.seekTo(progress);
                }
            }
        });

        mPauseButton = (Button) v.findViewById(R.id.pauseButton);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPlayer.pause();
                stopUpdation();
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

    public void seekUpdation() {
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        Log.d(PlaybackActivity.TAG, "Setting max " + duration);
        mSeekBar.setMax(duration);
        Log.d(PlaybackActivity.TAG, "Seeking to " + position + ", PlaybackFragment address= " + toString());
        mSeekBar.setProgress(position);
        seekHandler.postDelayed(mRunnable, 1000);
    }

    public void stopUpdation() {
        seekHandler.removeCallbacksAndMessages(null);
    }

}