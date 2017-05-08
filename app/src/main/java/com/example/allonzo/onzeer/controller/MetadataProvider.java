package com.example.allonzo.onzeer.controller;

import java.util.List;
import java.util.Map;

/**
 * Created by Allonzo on 08/05/2017.
 */

public class MetadataProvider {
    private static final String WEBSERVICE_URL="http://localhost/onzeer/";
    private String url;
    public MetadataProvider(CommandEnum command,String commandValue){
        this.url=WEBSERVICE_URL+command+"?value="+commandValue;
    }
    public String getTitle(){
        return "";
    }
    public String getStreamingUrl(){
        return "http://streaming.radio.funradio.fr/fun-1-48-192";

    }
    public Map<String,String> getMetadata(){
        return null;

    }
    public String getNextSuggestion(){
        return null;

    }
    private void requestMetadata(){

    }
    public static List<String> getAllTitle(){
        return null;

    }
}
