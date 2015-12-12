package com.github.nikit.cpp.player.model;

import com.github.nikit.cpp.player.LulzPlayerDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by nik on 04.03.15.
 */
@Table(databaseName = LulzPlayerDatabase.NAME)
public class PlayList extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    private String name;

    @Column
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
