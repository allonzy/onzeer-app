package com.example.allonzo.onzeer.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.TextView;

import com.example.allonzo.onzeer.activities.MainActivity;
import com.example.allonzo.onzeer.activities.VocalCommandActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;
/**
 * Created by Allonzo on 08/05/2017.
 */

public class CommandAnalyser implements  RecognitionListener{
    private SpeechRecognizer speechRecognizer;
    private VocalCommandActivity caller;
    private Intent intent;
    private Map<CommandEnum,String> commandResult;
    private TextView status;
    private TextView subStatus;
    public CommandAnalyser(VocalCommandActivity caller){
        this.caller = caller;
        Context context = caller.getApplicationContext();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(this);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.FRANCE);
    }
    public Map<CommandEnum,String> getCommandResult(){
        return commandResult;
    }
    public void startListening(){
        speechRecognizer.startListening(intent);
    }
    public String get(CommandEnum command){
        return commandResult.get(command);
    }
    @Override public void onReadyForSpeech(Bundle params){Log.d("CommandAnalyser","onReadyForSpeech");}
    @Override public void onBeginningOfSpeech(){Log.d("CommandAnalyser","onBeginningOfSpeech");}
    @Override public void onRmsChanged(float rms_dB){Log.d("CommandAnalyser","onRmsChanged");}
    @Override public void onBufferReceived(byte[] buffer){Log.d("CommandAnalyser","onBufferReceived");}
    @Override public void onEndOfSpeech(){Log.d("CommandAnalyser","onEndOfSpeech");}
    @Override public void onPartialResults(Bundle partialResults){Log.d("CommandAnalyser","onPartialResults");}
    @Override public void onEvent(int eventType, Bundle params){Log.d("CommandAnalyser","onEvent");}
    @Override
    public void onError(int error) {
        Log.d("CommandAnalyser","onError");

        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                subStatus.setText("ERROR_AUDIO");
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                subStatus.setText("ERROR_CLIENT");
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                subStatus.setText("ERROR_INSUFFICIENT_PERMISSIONS");
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                subStatus.setText("ERROR_NETWORK");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                subStatus.setText("ERROR_NETWORK_TIMEOUT");
                break;
            default:
        }
    }

    @Override
    public void onResults(Bundle data) {
        Log.d("CommandAnalyser","onResults");
        List<String> results = data.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        this.analyseCommand(results);
        Log.d("Debug",commandResult.toString());

        if (commandResult.isEmpty() == false){
            Log.d("CommandAnalyser","onResultsSuccess");
            caller.onVocalCommandResult();
        }
       /* else if(commandResult.size() == 1){
            Log.d("CommandAnalyser","onResultsSuccess");
            caller.playAction();
        }*/
        else{
            caller.vocalSearchAction();
            Log.d("CommandAnalyser","onResultsError");

        }
    }
    public void analyseCommand(List<String> possibleCommand){

        commandResult = new HashMap<CommandEnum,String>();
        for(CommandEnum command : CommandEnum.values()){
            Pattern pattern = Pattern.compile("("+command.getRegex()+")( )?(.*)");
            for (String commandText : possibleCommand) {
                Log.d("Commands",commandText);
                Matcher matcher = pattern.matcher(commandText);
                if (matcher.matches()){
                    String commandValue = matcher.group(matcher.groupCount());
                    Log.d("CommandsSelected",commandValue);
                    commandResult.put(command,commandValue);
                }
            }
        }
    }

}

