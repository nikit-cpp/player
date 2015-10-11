package com.github.nikit.cpp.player.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.nikit.cpp.player.Constants;
import com.github.nikit.cpp.player.PlayListManager;
import com.github.nikit.cpp.player.R;
import com.github.nikit.cpp.player.model.Song;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by nik on 06.02.15.
 * Этот фрагмент внутри ViewPager'а
 */
public class PlaybackViewPagerFragment extends Fragment {
    private Song mSong;
    private TextView mSongName;
    private TextView mSongArtist;

    private ImageView mAlbumImage;


    public static PlaybackViewPagerFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.SONG_ID, crimeId);
        PlaybackViewPagerFragment fragment = new PlaybackViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG_TAG, "PlaybackFragment.onCreate()" + ", address= " + toString());

        UUID songId = (UUID) getArguments().getSerializable(Constants.SONG_ID);
        int playListId = getArguments().getInt(Constants.PLAYLIST_ID);
        Log.d(Constants.LOG_TAG, "In PlaybackViewPagerFragment: songUuid=" + songId);
        mSong = PlayListManager.getPlaylists().get(playListId).getSong(songId);
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
        View v = inflater.inflate(R.layout.fragment_viewpager, parent, false);
        mSongName   = (TextView) v.findViewById(R.id.song_name);
        mSongArtist = (TextView) v.findViewById(R.id.song_artist);

        mSongName.setText(mSong.getName());
        mSongArtist.setText(mSong.getArtist());


        mAlbumImage = (ImageView) v.findViewById(R.id.album_image);

        byte[] image = mSong.getImage();
        if(image!=null) {
            InputStream is = new ByteArrayInputStream(image);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            mAlbumImage.setImageBitmap(bmp);
        }

        return v;
    }
}
