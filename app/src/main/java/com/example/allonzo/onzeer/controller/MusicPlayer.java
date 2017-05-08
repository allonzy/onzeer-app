package com.example.allonzo.onzeer.controller;

import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.EmptyStackException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by Allonzo on 08/05/2017.
 */

public class MusicPlayer {
    private MetadataProvider metadataProvider;
    public MusicPlayer(MetadataProvider metadataProvider){
        this.playHistory = new Stack<String>();
        this.playList = new ArrayDeque<String>();
        this.metadataProvider = metadataProvider;
        this.setUrl(metadataProvider.getStreamingUrl());
    }
    public void update(){
        this.setUrl(metadataProvider.getStreamingUrl());
    }
    MediaPlayer mediaPlayer;
    private Stack<String> playHistory;
    private Queue<String> playList;
    private void setUrl(String url){
        this.playHistory.push(url);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    public void playMusic(){
        mediaPlayer.start();
    }
    public void stopMusic(){
        mediaPlayer.stop();
    }
    public float getProgression(){
        return mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();
    }
    public MetadataProvider next() throws MusicPlayerException{
        try{
            String url = playList.remove();
            if (url == null){
                throw new MusicPlayerException();
            }
            this.setUrl(url);
            return this.metadataProvider;
        }catch (NoSuchElementException e){
            throw new MusicPlayerException();
        }
    }
    public MetadataProvider previous() throws MusicPlayerException{
        try{
            String url = playHistory.pop();
            this.setUrl(url);
            return this.metadataProvider;
        }catch (EmptyStackException e){
            throw new MusicPlayerException();
        }
    }
}
