package com.example.allonzo.onzeer.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.allonzo.onzeer.R;
import com.example.allonzo.onzeer.controller.CommandAnalyser;
import com.example.allonzo.onzeer.controller.CommandEnum;
import com.example.allonzo.onzeer.controller.MetadataProvider;
import com.example.allonzo.onzeer.controller.MusicPlayer;
import com.example.allonzo.onzeer.controller.MusicPlayerException;

import java.util.Map;

/**
 * Created by Allonzo on 08/05/2017.
 */

public class MusicPlayActivity extends AppCompatActivity implements View.OnClickListener,VocalCommandActivity {
    private CommandAnalyser commandAnalyser;

    private MusicPlayer musicPlayer;
    private MetadataProvider metadataProvider;
    private TextView musicTitle;
    private ImageView albumCover;
    private ImageButton previousButton;
    private ImageButton vocalCommandButton;

    private ImageButton playButton;
    private ImageButton nextButton;
    private ProgressBar musicProgressionBar;
    private ListView musicMetadata;
    private Boolean isPlaying;
    private int musicProgression;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        this.metadataProvider = new MetadataProvider(
                (CommandEnum) extras.get("command"),
                extras.getString("commandValue")
            );
        this.musicPlayer = new MusicPlayer(this.metadataProvider);
        commandAnalyser = new CommandAnalyser(this);

        setContentView(R.layout.activity_music_play);
        findViews();
        runProgressBar();
        updateMusic();

    }
    private void updateMusic(){

        this.setMusicMetadata(metadataProvider.getMetadata());
        this.setTitle(metadataProvider.getTitle());
        musicPlayer.startMusic();
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
        vocalCommandButton = (ImageButton) findViewById( R.id.player_vocal_command_button );
        musicTitle = (TextView) findViewById( R.id.title );
        albumCover = (ImageView)findViewById( R.id.album_cover );
        previousButton = (ImageButton)findViewById( R.id.previous_button );
        playButton = (ImageButton)findViewById( R.id.play_button );
        nextButton = (ImageButton)findViewById( R.id.next_button );
        musicProgressionBar = (ProgressBar)findViewById( R.id.music_progression );
        musicMetadata = (ListView)findViewById( R.id.music_metadata );
        vocalCommandButton.setOnClickListener( this );
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
        Log.d("Debug",v.toString());
        if (v == vocalCommandButton){
            Log.d("Debug","vocalCommandButton");
            this.vocalSearchAction();
        }
       else if ( v == previousButton ) {
            this.previous();
        } else if ( v == playButton ) {
            if (musicPlayer.isPlaying()){
                this.pause();
            }else{
                this.resume();
            }
        } else if ( v == nextButton ) {
            Log.d("Debug","nextButton");
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
            musicPlayer.startMusic();
        }catch (MusicPlayerException e){
            Log.d("Debug","nextButtonError");
        }

    }
    private void resume(){
        playButton.setImageDrawable(getDrawable(R.drawable.pause));
        this.musicPlayer.resumeMusic();
    }
    private void pause(){
        playButton.setImageDrawable(getDrawable(R.drawable.play));
        this.musicPlayer.pauseMusic();
        isPlaying = false;
    }
    public void dispatchCommands(Map<CommandEnum,String> commandList) {
        if(commandList.size() == 1){
            if(commandList.containsKey(CommandEnum.PLAY)){
                Intent intent = new Intent(this,MusicPlayActivity.class);
                intent.putExtra("command",CommandEnum.PLAY);
                this.startActivity(intent);
            }else if(commandList.containsKey(CommandEnum.STOP)){
                this.pause();
            }else if(commandList.containsKey(CommandEnum.NEXT)){
                this.next();
            }else if(commandList.containsKey(CommandEnum.PREVIOUS)){
                this.previous();
            }/**/
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("commande non reconnue play/jouer pour jouer une musique")
                    .setTitle("command not found")
                    .setCancelable(true)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    private void showSimplePopup(String message){

    }
    public void searchResultAction(){
        //Log.d("searchResult",vocalCommandAnalyser.getCommandResult().toString());
    }
    public void onVocalCommandResult(){
        this.dispatchCommands(commandAnalyser.getCommandResult());
    }
    public void vocalSearchAction(){
        commandAnalyser.startListening();
    }
}
