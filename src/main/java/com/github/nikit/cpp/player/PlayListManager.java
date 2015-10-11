package com.github.nikit.cpp.player;

import com.github.nikit.cpp.player.model.PlayList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Всегда владеет списком плейлистов
 * для начала будет возвращать встроенные плейлисты
 * 1 из папки и 1 из вконтача
 * Created by nik on 14.07.15.
 */
public class PlayListManager {
    static List<PlayList> output;
    static {
        output = new ArrayList<>();
        output.add(PlayListFabrics.getPlaylistFromDirectory(new File("/sdcard/Sounds/Digital2")));
    }

    public static List<PlayList> getPlaylists() {
        return output;
    }
}
