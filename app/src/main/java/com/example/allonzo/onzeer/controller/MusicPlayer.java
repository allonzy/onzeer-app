package com.example.allonzo.onzeer.controller;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.EmptyStackException;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by Allonzo on 08/05/2017.
 */

public class MusicPlayer implements MediaPlayer.OnPreparedListener{
    private MetadataProvider metadataProvider;
    MediaPlayer mediaPlayer;
    private Stack<String> playHistory;
    private Queue<String> playList;
    private String url;
    private Boolean isPrepared;
    public MusicPlayer(MetadataProvider metadataProvider){
        this.playHistory = new Stack<String>();
        this.playList = new ArrayDeque<String>();
        this.metadataProvider = metadataProvider;
        this.mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.url = metadataProvider.getStreamingUrl();
        this.startMusic();

    }
    public void update(){
        this.url = metadataProvider.getStreamingUrl();
        this.startMusic();
    }
    public void startMusic(){
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e("setUrl: ",e.toString() );
        } catch (IllegalArgumentException e) {
            Log.e("setUrl: ",e.toString() );
        } catch (IllegalStateException e) {
            Log.e("setUrl: ",e.toString() );
        }
    }

    public void onPrepared(MediaPlayer player){
        player.start();
    }
    public void stopMusic(){
        mediaPlayer.stop();
    }
    public void pauseMusic(){
        mediaPlayer.pause();
    }
    public void resumeMusic(){
        mediaPlayer.start();
    }
    public float getProgression(){
        /*
        float value = mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();
        if(value > 0 && value < 1){
            return value;
        }else if(value < 0){
            return 0;
        }else {
            return 100;
        }/**/
        return 0;
    }
    public MetadataProvider next() throws MusicPlayerException{
        try{
            String url = playList.remove();
            if (url == null){
                throw new MusicPlayerException();
            }
            this.url = url;
            this.startMusic();
            return this.metadataProvider;
        }catch (NoSuchElementException e){
            throw new MusicPlayerException();
        }
    }
    public MetadataProvider previous() throws MusicPlayerException{
        try{
            String url = playHistory.pop();
            this.url = url;
            this.startMusic();
            return this.metadataProvider;
        }catch (EmptyStackException e){
            throw new MusicPlayerException();
        }
    }
    public Boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
    public void onMusicEnd() throws MusicPlayerException{
        this.playHistory.push(url);
        this.next();
    }
}
