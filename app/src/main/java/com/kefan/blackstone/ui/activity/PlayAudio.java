package com.kefan.blackstone.ui.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by MY SHIP on 2017/5/23.
 */

public class PlayAudio {
    private String URL;
    private Context context;
    private MediaPlayer mediaPlayer;
    private Uri uri;

    public PlayAudio(String URL, Context context) {
        this.URL = URL;
        this.context = context;
        mediaPlayer = new MediaPlayer();
        uri = Uri.parse(URL);
        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
