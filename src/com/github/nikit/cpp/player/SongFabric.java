package com.github.nikit.cpp.player;

import android.content.Context;
import android.util.Log;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

        for (File f : dir.listFiles()) {
            String name = "Song name";
            String artist = "Song artist";
            byte [] image = null;

            InputStream input = null;
            try {
                input = new FileInputStream(f);
                ContentHandler handler = new DefaultHandler();
                Metadata metadata = new Metadata();
                Parser parser = new Mp3Parser();
                ParseContext parseCtx = new ParseContext();

                parser.parse(input, handler, metadata, parseCtx);
                String rawName = metadata.get("title");
                if(rawName!=null){
                    name = rawName;
                }
                String rawArtist = metadata.get("xmpDM:artist");
                if(rawArtist!=null){
                    artist = rawArtist;
                }
            } catch (IOException | SAXException | TikaException e) {
                Log.e(PlaybackPagerActivity.TAG, "Error on reading tag", e);
            }finally {
                if(input!=null)
                    try {
                        input.close();
                    } catch (IOException e) {
                        Log.e(PlaybackPagerActivity.TAG, "Error on close stream", e);
                    }
            }
            Song c = new Song();
            c.setName(name);
            c.setArtist(artist);
            c.setFile(f);
            c.setImage(image);
            mSongs.add(c);
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
