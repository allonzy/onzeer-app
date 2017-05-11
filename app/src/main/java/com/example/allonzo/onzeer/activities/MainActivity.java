package com.example.allonzo.onzeer.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.allonzo.onzeer.R;
import com.example.allonzo.onzeer.controller.CommandAnalyser;
import com.example.allonzo.onzeer.controller.CommandEnum;

import java.util.Collections;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener,VocalCommandActivity{
    private CommandAnalyser commandAnalyser;
    private EditText homeSearchInput;
    private ImageButton vocalCommandButton;
    private ImageButton searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        commandAnalyser = new CommandAnalyser(this);
    }

    private void findViews() {
        homeSearchInput = (EditText)findViewById( R.id.search_input );
        vocalCommandButton = (ImageButton)findViewById( R.id.vocal_command_button );
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
        commandAnalyser.startListening();
    }
    public void textSearchAction(){

        commandAnalyser.analyseCommand(
                Collections.singletonList(homeSearchInput.getText().toString())
        );
        this.dispatchCommands(commandAnalyser.getCommandResult());

    }
    public void dispatchCommands(Map<CommandEnum,String> commandList) {
        if(commandList.size() == 1){
            if(commandList.containsKey(CommandEnum.PLAY)){
                Intent intent = new Intent(this,MusicPlayActivity.class);
                intent.putExtra("command",CommandEnum.PLAY);
                intent.putExtra("commandValue",commandAnalyser.get(CommandEnum.PLAY));
                this.startActivity(intent);
            }/*else if(){
                //TODO other command
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
}
