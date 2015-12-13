package com.github.nikit.cpp.player;

import android.util.Log;
import com.github.nikit.cpp.player.model.PlayList;
import com.github.nikit.cpp.player.model.Song;
import com.mpatric.mp3agic.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Генерирует плейлист из чего-либо
 * Created by nik on 13.02.15.
 */
public class PlayListFabrics {

    public static void initPlayList(PlayList playList) {
        switch (playList.getType()) {
            case DIR:
                getPlaylistFromDirectory(new File(playList.getSource()), playList);
        }
    }

    /**
     * Генерирует плейлист из папки.
     * Будет вызываться менеджером плейлистов
     * @return
     */
    public static void getPlaylistFromDirectory(File dir, PlayList playList){
        ArrayList<Song> mSongs = new ArrayList<Song>();

        for (File f : dir.listFiles()) {
            String name = "Name";
            String artist = "Artist";
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
                Log.e(Constants.LOG_TAG, "Error on get tag", e);
            }
            Song c = new Song();
            c.setName(name);
            c.setArtist(artist);
            c.setFile(f);
            c.setImage(image);
            mSongs.add(c);
        }

        playList.setSongs(mSongs);
    }
}
