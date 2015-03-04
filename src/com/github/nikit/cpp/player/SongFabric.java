package com.github.nikit.cpp.player;

import android.content.Context;
import android.util.Log;
import com.mpatric.mp3agic.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by nik on 13.02.15.
 */
public class SongFabric {
    private ArrayList<Song> mSongs;
    private static SongFabric sSongFabric;
    private Context mAppContext;
    private File dir = new File("/sdcard/Sounds/Digital2");


    /**
     * Конструктору CrimeLab передается параметр Context . В Android такая ситуация
     встречается очень часто; наличие параметра Context позволяет синглетному классу
     запускать активности, обращаться к ресурсам проекта, находить закрытое храни-
     лище вашего приложения и т. д.
     *
     * @param appContext
     */
    private SongFabric(Context appContext){
        mAppContext = appContext;
        mSongs = new ArrayList<Song>();

        for (File f : dir.listFiles()) {
            String name = "Song name";
            String artist = "Song artist";
            byte [] image = null;
            try {
                Mp3File mp3file = new Mp3File(f);
                ID3v1 id3v1 = mp3file.getId3v1Tag();
                if(id3v1 != null){
                    name = id3v1.getTitle();
                    artist = id3v1.getArtist();
                }
                ID3v2 id3v2 = mp3file.getId3v2Tag();
                if(id3v2 != null){
                    name = id3v2.getTitle();
                    artist = id3v2.getArtist();
                    image = id3v2.getAlbumImage();
                }
            } catch (IOException | UnsupportedTagException | InvalidDataException e) {
                Log.e(PlaybackPagerActivity.TAG, "Error on get tag", e);
            }
            Song c = new Song();
            c.setName(name);
            c.setArtist(artist);
            c.setFile(f);
            c.setImage(image);
            mSongs.add(c);
        }
    }

    public static SongFabric get(Context c) {
        if (sSongFabric == null) {
            sSongFabric = new SongFabric(c.getApplicationContext());
        }
        return sSongFabric;
    }

    /*public ArrayList<Song> getSongs() {
        return mSongs;
    }*/

    private static PlayList currentPlaylist;

    public PlayList getCurrentPlayList(){
        if(currentPlaylist == null) {
            currentPlaylist = new PlayList(mSongs);
        }
        return currentPlaylist;
    }

}
