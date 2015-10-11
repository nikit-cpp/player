package com.github.nikit.cpp.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by nik on 04.03.15.
 */
public class PlayList {

    private String name;
    private String source;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return source + " " + name;
    }
}
