package com.github.nikit.cpp.player;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by nik on 13.02.15.
 */
public class SongFabric {
    private ArrayList<Song> mSongs;
    private static SongFabric sSongFabric;
    private Context mAppContext;
    private File dir = new File("/sdcard/Sounds/Digital");


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

        int i = 0;
        for (File f : dir.listFiles()) {
            Song c = new Song();
            c.setName(f.getName());
            c.setArtist("Song artist #" + i % 50); // Для каждого второго объекта
            c.setFile(f);
            mSongs.add(c);
            ++i;
        }
    }

    public static SongFabric get(Context c) {
        if (sSongFabric == null) {
            sSongFabric = new SongFabric(c.getApplicationContext());
        }
        return sSongFabric;
    }

    public ArrayList<Song> getSongs() {
        return mSongs;
    }

    public Song getSong(UUID id) {
        for (Song c : mSongs) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }

}
