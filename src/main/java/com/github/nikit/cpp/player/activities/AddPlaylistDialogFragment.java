package com.github.nikit.cpp.player.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import com.github.nikit.cpp.player.Constants;
import com.github.nikit.cpp.player.R;
import com.github.nikit.cpp.player.model.PlayList;
import com.raizlabs.android.dbflow.sql.language.Insert;

import java.io.File;

/**
 * Created by nik on 17.10.15.
 */
public class AddPlaylistDialogFragment extends DialogFragment {

    private String playlistName = "";
    private String playlistSource = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.adding_playlist, null);

        EditText editTestPlaylistName = (EditText)v.findViewById(R.id.editTextPlaylistName);
        EditText editTestPlaylistSource = (EditText)v.findViewById(R.id.editTextPlaylistSource);

        editTestPlaylistName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                playlistName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        editTestPlaylistSource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                playlistSource = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.adding_playlist_title)
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PlayList playList = new PlayList();
                        playList.setName(playlistName);
                        playList.setSource(playlistSource);
                        playList.insert();
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;

        getTargetFragment().onActivityResult(Constants.REQUEST_PLAY_LIST, resultCode, null);
    }
}
