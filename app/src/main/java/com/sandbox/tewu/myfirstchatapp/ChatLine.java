package com.sandbox.tewu.myfirstchatapp;

public class ChatLine {
    private String message;
    private String sender;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private ChatLine(){  }

    ChatLine(String message, String sender){
        this.message = message;
        this.sender = sender;
    }

    public String getMessage(){
        return message;
    }

    public String getSender(){
        return sender;
    }
}
