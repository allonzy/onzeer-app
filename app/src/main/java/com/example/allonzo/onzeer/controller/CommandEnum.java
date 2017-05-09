package com.example.allonzo.onzeer.controller;

/**
 * Created by Allonzo on 08/05/2017.
 */

public enum  CommandEnum {
    PLAY("(play|jou[a-z]{0,3})","play"),;
    private String commandRegex;
    private String text;
    private CommandEnum(String commandRegex,String text){
        this.commandRegex = commandRegex;
        this.text = text;
    }
    public String getRegex(){
        return this.commandRegex;
    }
    public String toString(){
        return text;
    }
}
