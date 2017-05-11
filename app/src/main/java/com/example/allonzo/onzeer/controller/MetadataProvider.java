package com.example.allonzo.onzeer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Allonzo on 08/05/2017.
 */

public class MetadataProvider {
    private static final String WEBSERVICE_URL="http://localhost/onzeer/";
    private String streamingUrl;
    private String title;
    public MetadataProvider(CommandEnum command,String commandValue){
        this.streamingUrl="http://radioclassique.ice.infomaniak.ch/radioclassique-high.mp3";
        title = "Radio Classique";
    }
    public String getTitle(){
        return title;
    }
    public String getStreamingUrl(){
        return this.streamingUrl;
    }
    public Map<String,String> getMetadata(){
        Map<String,String> metadata = new HashMap<String,String>();
        metadata.put("radio","classique");
        metadata.put("tags","Radio Classique, musique classique");
        return metadata;

    }
    public String getNextSuggestion(){
        this.streamingUrl = "http://broadcast.infomaniak.ch/tsfjazz-high.mp3";
        this.title = "Jazz";

        return this.streamingUrl;

    }
    private void requestMetadata(){

    }
    public static List<String> getAllTitle(){
        return null;

    }
}
