package com.github.nikit.cpp.player.dao;

import com.github.nikit.cpp.player.PlayListFabrics;
import com.github.nikit.cpp.player.model.PlayList;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Всегда владеет списком плейлистов
 * для начала будет возвращать встроенные плейлисты
 * 1 из папки и 1 из вконтача
 * Created by nik on 14.07.15.
 */
public class PlayListDao {
    //static List<PlayList> output;
    static {
        //output = new ArrayList<>();
        //output.add(PlayListFabrics.getPlaylistFromDirectory(new File("/sdcard/Sounds/Digital2")));

        PlayList pl = new PlayList();
        pl.setName("Supername");
        pl.save();
    }

    public static List<PlayList> getPlaylists() {
        List<PlayList> playlists = new Select().from(PlayList.class).queryList();
        return playlists;
    }
}
