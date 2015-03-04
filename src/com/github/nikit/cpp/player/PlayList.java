package com.github.nikit.cpp.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by nik on 04.03.15.
 */
public class PlayList {
    private List<Song> songs;

    public PlayList() {
        songs = new ArrayList<Song>();
    }

    public PlayList(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public Song getSong(UUID id) {
        for (Song c : songs) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }
}
