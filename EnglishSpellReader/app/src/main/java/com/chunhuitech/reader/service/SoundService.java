package com.chunhuitech.reader.service;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;

import com.chunhuitech.reader.entity.PlaySoundParam;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class SoundService {

    private MediaPlayer mediaPlayer;
    private Handler handler;
    private String mp3Path;

    public SoundService() {
        handler = new Handler();
        mp3Path = null;
    }

    public void playMp3(File mp3File, Map<String, Object> selectedObject) {

        PlaySoundParam playSoundParam = new PlaySoundParam();
        playSoundParam.setMp3Path(mp3File.getAbsolutePath());
        playSoundParam.setBeginPoint(Integer.parseInt(selectedObject.get("beginPoint").toString()) * 1000);
        playSoundParam.setEndPoint(Integer.parseInt(selectedObject.get("endPoint").toString()) * 1000);

        new AsyncTask<PlaySoundParam, Void, Void>() {
            @Override
            protected Void doInBackground(PlaySoundParam... playSoundParams) {
                PlaySoundParam playParam = playSoundParams[0];
                if(!playParam.getMp3Path().equals(mp3Path)) {
                    mp3Path = playParam.getMp3Path();
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(playParam.getMp3Path());
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (mediaPlayer == null) {
                        mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(playParam.getMp3Path());
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                mediaPlayer.seekTo(playParam.getBeginPoint());
                mediaPlayer.start();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                        }
                    }
                }, playParam.getDelay());

                return null;
            }
        }.execute(playSoundParam);
    }

}
