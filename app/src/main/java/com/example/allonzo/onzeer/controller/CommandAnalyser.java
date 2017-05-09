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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Allonzo on 08/05/2017.
 */

public class CommandAnalyser implements RecognitionListener {
    private SpeechRecognizer speechRecognizer;
    private MainActivity caller;
    private Intent intent;
    private Map<CommandEnum,String> commandResult;
    private TextView status;
    private TextView subStatus;
    public CommandAnalyser(MainActivity caller){
        this.caller = caller;
        Context context = caller.getApplicationContext();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        intent = RecognizerIntent.getVoiceDetailsIntent(context);
    }
    public Map<CommandEnum,String> getCommandResult(){
        return commandResult;
    }
    public void startListening(){
        speechRecognizer.startListening(intent);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        status.setText("ready");
    }

    @Override
    public void onBeginningOfSpeech() {
        status.setText("listen");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        String s = String.format("recieve : % 2.2f[dB]", rmsdB);
        subStatus.setText(s);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
    }

    @Override
    public void onEndOfSpeech() {
        status.setText("listen complete");

    }

    @Override
    public void onError(int error) {
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
        List<String> results = data.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        this.analyseCommand(results);
        if (commandResult.size() > 1)
            caller.searchResultAction();
        else if(commandResult.size() > 1){
            caller.playAction();
        }
        else
            caller.vocalSearchAction();
    }
    public void analyseCommand(List<String> possibleCommand){
        commandResult = new HashMap<CommandEnum,String>();
        for(CommandEnum command : CommandEnum.values()){
            Pattern pattern = Pattern.compile("("+command.getRegex()+") (.*)");
            for (String commandText : possibleCommand) {
                Matcher matcher = pattern.matcher(commandText);
                if (matcher.matches()){
                    String commandValue = matcher.group(matcher.groupCount());
                    commandResult.put(command,commandValue);
                }
            }
        }
    }
    @Override
    public void onPartialResults(Bundle partialResults) {
    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}

