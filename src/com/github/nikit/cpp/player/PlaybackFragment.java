package com.github.nikit.cpp.player;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by nik on 06.02.15.
 * Этот фрагмент внутри ViewPager'а
 */
public class PlaybackFragment extends Fragment {
    public static final String EXTRA_CRIME_ID = "extra_crimeId";
    private Song mSong;
    private TextView mSongName;
    private TextView mSongArtist;

    private AudioPlayer mPlayer;
    private Button mPlayButton;
    private Button mStopButton;
    private SeekBar mSeekBar;
    private Button mPauseButton;
    private ImageView mAlbumImage;

    private Handler seekHandler;
    private Runnable mRunnable;


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
        Log.d(PlaybackPagerActivity.TAG, "PlaybackFragment.onCreate()");
        UUID songId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);

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
        Log.d(PlaybackPagerActivity.TAG, "PlaybackFragment.onCreateView()");
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

        seekHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                seekUpdation();
            }
        };


        mPauseButton = (Button) v.findViewById(R.id.pauseButton);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPlayer.pause();
                stopUpdation();
            }
        });

        mAlbumImage = (ImageView) v.findViewById(R.id.album_image);

        byte[] image = mSong.getImage();
        if(image!=null) {
            InputStream is = new ByteArrayInputStream(image);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            mAlbumImage.setImageBitmap(bmp);
        }

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
        Log.d(PlaybackPagerActivity.TAG, "Setting max " + duration);
        mSeekBar.setMax(duration);
        Log.d(PlaybackPagerActivity.TAG, "Seeking to " + position);
        mSeekBar.setProgress(position);
        seekHandler.postDelayed(mRunnable, 1000);
    }

    public void stopUpdation() {
        seekHandler.removeCallbacksAndMessages(null);
    }

}
