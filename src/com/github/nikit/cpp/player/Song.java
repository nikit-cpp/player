package com.github.nikit.cpp.player;

import java.io.File;
import java.util.UUID;

/**
 * Created by nik on 13.02.15.
 */
public class Song {
    String name;
    String artist;
    UUID id;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    File file;

    public Song(){
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
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
