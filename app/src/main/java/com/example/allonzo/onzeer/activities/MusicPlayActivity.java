package com.example.allonzo.onzeer.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.allonzo.onzeer.R;
import com.example.allonzo.onzeer.controller.CommandEnum;
import com.example.allonzo.onzeer.controller.MetadataProvider;
import com.example.allonzo.onzeer.controller.MusicPlayer;
import com.example.allonzo.onzeer.controller.MusicPlayerException;

import java.util.Map;

/**
 * Created by Allonzo on 08/05/2017.
 */

public class MusicPlayActivity extends AppCompatActivity implements View.OnClickListener {
    private MusicPlayer musicPlayer;
    private MetadataProvider metadataProvider;
    private TextView musicTitle;
    private ImageView albumCover;
    private ImageButton previousButton;
    private ImageButton playButton;
    private ImageButton nextButton;
    private ProgressBar musicProgressionBar;
    private ListView musicMetadata;
    private Boolean isPlaying;
    private int musicProgression;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.metadataProvider = new MetadataProvider(CommandEnum.PLAY,
                //(String)savedInstanceState.get("commandValue"));
                "hotel california");
        this.musicPlayer = new MusicPlayer(this.metadataProvider);
        setContentView(R.layout.activity_music_play);
        findViews();
        //runProgressBar();
        updateMusic();
        this.isPlaying = false;
    }
    private void updateMusic(){
        this.setMusicMetadata(metadataProvider.getMetadata());
        this.setTitle(metadataProvider.getTitle());
    }
    private void runProgressBar(){
        new Thread(new Runnable() {
            public void run() {
                musicProgression = 0;
                while (musicProgression <= 100) {
                    musicProgression = Math.round(musicPlayer.getProgression()*100);
                    // Update the progress bar
                    musicProgressionBar.post(new Runnable() {
                        public void run() {
                            musicProgressionBar.setProgress(musicProgression);
                            if(musicProgression == 100){
                                next();
                            }
                        }
                    });
                }
            }
        }).start();

    }
    private void findViews() {
        musicTitle = (TextView) findViewById( R.id.title );
        albumCover = (ImageView)findViewById( R.id.album_cover );
        previousButton = (ImageButton)findViewById( R.id.previous_button );
        playButton = (ImageButton)findViewById( R.id.play_button );
        nextButton = (ImageButton)findViewById( R.id.next_button );
        musicProgressionBar = (ProgressBar)findViewById( R.id.music_progression );
        musicMetadata = (ListView)findViewById( R.id.music_metadata );

        previousButton.setOnClickListener( this );
        playButton.setOnClickListener( this );
        nextButton.setOnClickListener( this );
    }
    private void setMusicMetadata(Map<String,String> metadata){
        if (metadata == null){
            return;
        }
        musicMetadata.removeAllViewsInLayout();
        for (Map.Entry<String, String> entry : metadata.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            TextView metadataElement = new TextView(this);
            metadataElement.setText(key+": "+value);
            musicMetadata.addFooterView(metadataElement);
        }
    }
    private void setTitle(String title){
        if(title != null){
            this.musicTitle.setText(title);
        }
    }
    @Override
    public void onClick(View v) {
        if ( v == previousButton ) {
            this.previous();
        } else if ( v == playButton ) {
            if (musicPlayer.isPlaying()){
                this.pause();
            }else{
                this.play();
            }
        } else if ( v == nextButton ) {
            this.next();
        }
    }
    private void previous(){
        try {
            this.musicPlayer.previous();
            this.updateMusic();
        }catch (MusicPlayerException e){

        }
    }
    private void next(){
        try {
            this.musicPlayer.next();
            this.updateMusic();
        }catch (MusicPlayerException e){

        }

    }
    private void play(){
        playButton.setImageDrawable(getDrawable(R.drawable.pause));
        this.musicPlayer.playMusic();
    }
    private void pause(){
        playButton.setImageDrawable(getDrawable(R.drawable.play));
        this.musicPlayer.stopMusic();
        isPlaying = false;
    }
}
