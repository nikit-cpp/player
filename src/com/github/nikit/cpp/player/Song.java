package com.github.nikit.cpp.player;

import java.util.UUID;

/**
 * Created by nik on 13.02.15.
 */
public class Song {
    String name;
    String artist;
    UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
