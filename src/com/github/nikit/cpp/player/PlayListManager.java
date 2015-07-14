package com.github.nikit.cpp.player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Владеет списком плейлистов
 * для начала будет возвращать встроенные плейлисты
 * 1 из папки и 1 из вконтача
 * Created by nik on 14.07.15.
 */
public class PlayListManager {
    public static List<List<Song>> getPlaylists() {
        List<List<Song>> output = new ArrayList<>();

        // добавляем первый дефолтный плейлист
        output.add(PlayListFabric.getPlaylistFromDirectory(new File("/sdcard/Sounds/Digital2")));

        return  output;
    }
}
