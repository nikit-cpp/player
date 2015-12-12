package com.github.nikit.cpp.player.dao;

import android.util.Log;
import com.github.nikit.cpp.player.Constants;
import com.github.nikit.cpp.player.PlayListFabrics;
import com.github.nikit.cpp.player.adapters.PlaylistAdapter;
import com.github.nikit.cpp.player.model.PlayList;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.SelectListTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListenerAdapter;
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
public class PlayListDAO extends AbstractDAO {
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

    public static void createNewPlaylist(String name, String source) {
        PlayList playList = new PlayList();
        playList.setName(name);
        playList.setSource(source);
        playList.insert();
    }

    public static void updatePlaylists(final PlaylistAdapter playlistAdapter) {
        TransactionManager.getInstance().addTransaction(
                new SelectListTransaction<>(
                        new Select().from(PlayList.class),
                        new TransactionListenerAdapter<List<PlayList>>( ) {
                            @Override
                            public void onResultReceived(List<PlayList> playLists) {
                                Log.d(Constants.LOG_TAG, "getted " + playLists.size() + " playlists");
                                playlistAdapter.updateList(playLists);
                            }
                        }
                )
        );
    }
}
