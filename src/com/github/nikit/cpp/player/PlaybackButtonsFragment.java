package com.github.nikit.cpp.player;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
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
//    private Song mSong;

    //private AudioPlayer mPlayer;
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

    private UUID songId;

    public PlaybackButtonsFragment() {
        super();
        // Just to be an empty Bundle. You can use this later with getArguments().set...
        setArguments(new Bundle());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Tags.LOG_TAG, "PlaybackFragment.onCreate()" + ", address= " + toString());

        Bundle arguments = getArguments();
        songId = (UUID)arguments.getSerializable(Tags.SONG_ID);

        //mPlayer = AudioPlayer.get(this.getActivity());

        //mSong = SongFabric.get(getActivity()).getCurrentPlayList().getSong(songId);

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
        Log.d(Tags.LOG_TAG, "PlaybackFragment.onCreateView()" + ", address= " + toString());
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
                i.putExtra(Tags.PLAYER_SERVICE_ACTION, PlayerService.Action.PLAY);
                i.putExtra(Tags.SONG_ID, songId);
                getActivity().startService(i);

                //mPlayer.play(mSong.getFile());
                seekUpdation();
            }
        });

        mStopButton = (Button)v.findViewById(R.id.stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //mPlayer.stop();
                Intent i = new Intent(getActivity(), PlayerService.class);
                i.putExtra(Tags.PLAYER_SERVICE_ACTION, PlayerService.Action.STOP);
                getActivity().startService(i);

                stopUpdation();
            }
        });

        mSeekBar = (SeekBar) v.findViewById(R.id.playbackSeekBar);
             //seekUpdation(); // НАХУЯ ??

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( fromUser){
                    //mPlayer.seekTo(progress);
                    Intent i = new Intent(getActivity(), PlayerService.class);
                    i.putExtra(Tags.PLAYER_SERVICE_ACTION, PlayerService.Action.SEEK);
                    i.putExtra(Tags.SONG_SEEK_TO_POSITION, progress);
                    getActivity().startService(i);

                }
            }
        });

        mPauseButton = (Button) v.findViewById(R.id.pauseButton);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PlayerService.class);
                i.putExtra(Tags.PLAYER_SERVICE_ACTION, PlayerService.Action.PAUSE);
                getActivity().startService(i);

                //mPlayer.pause();
                stopUpdation();
            }
        });

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_playback, menu);
    }


    public void seekUpdation() {
        PlaybackActivity playbackActivity = (PlaybackActivity) getActivity();

        if (null != playbackActivity) {
            SeekReceiver seekReceiver = playbackActivity.getReceiver();
            if (null != seekReceiver) {
                Intent intent = new Intent();

                intent.putExtra(Tags.SEEK_RECEIVER, seekReceiver);

                int position = playbackActivity.getCurrentPosition();
                int duration = playbackActivity.getDuration();
                //Log.d(PlaybackActivity.TAG, "Setting max " + duration);
                mSeekBar.setMax(duration);
                Log.d(Tags.LOG_TAG, "Seeking to " + position + ", PlaybackFragment address= " + toString());
                mSeekBar.setProgress(position);
                seekHandler.postDelayed(mRunnable, 1000);
            }
        }
    }

    ResultReceiver resultReceiver = null;



    public void stopUpdation() {
        seekHandler.removeCallbacksAndMessages(null);
    }

}
