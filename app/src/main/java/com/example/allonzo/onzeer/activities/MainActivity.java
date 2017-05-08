package com.example.allonzo.onzeer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.allonzo.onzeer.R;
import com.example.allonzo.onzeer.controller.CommandAnalyser;
import com.example.allonzo.onzeer.controller.CommandEnum;

public class MainActivity extends Activity implements View.OnClickListener{
    private CommandAnalyser vocalCommandAnalyser;
    private EditText homeSearchInput;
    private Button vocalCommandButton;
    private ImageButton searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        //vocalCommandAnalyser = new CommandAnalyser(this);
    }

    private void findViews() {
        homeSearchInput = (EditText)findViewById( R.id.search_input );
        vocalCommandButton = (Button)findViewById( R.id.vocal_command_button );
        vocalCommandButton.setOnClickListener( this );
        searchButton = (ImageButton)findViewById( R.id.search_button );
        searchButton.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        if ( v == searchButton ) {
            textSearchAction();
        }else if( v == vocalCommandButton){
            Log.d("test","test");
            vocalSearchAction();
        }
    }

    public void vocalSearchAction(){
        //vocalCommandAnalyser.startListening();
    }
    public void textSearchAction(){
        Intent intent = new Intent(this,MusicPlayActivity.class);
        intent.putExtra("commandValue",homeSearchInput.getText());
        this.startActivity(intent);
    }
    public void searchResultAction(){
        Log.d("searchResult",vocalCommandAnalyser.getCommandResult().toString());
    }
    public void playAction(){
        Intent intent = new Intent(this,MusicPlayActivity.class);
        Log.d("play",vocalCommandAnalyser.getCommandResult().toString());
        intent.putExtra("commandValue",vocalCommandAnalyser.getCommandResult().get(CommandEnum.PLAY));
        this.startActivity(intent);
    }
}
