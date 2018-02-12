package com.example.noli.sphinx;

/**
 * Created by Adhu on 5/11/2017.
 */

public class ChatMessage {
    private String msg;
    private String sender;
    //private Date time_sent;

    public ChatMessage(String msg, String sender) {
        this.msg = msg;
        this.sender = sender;
    }

    public String getMsg() {
        return msg;
    }

    public String getSender() {
        return sender;
    }
}
