package com.example.noli.sphinx;

/**
 * Created by Ashu on 24/11/15.
 */
public class Message {
    private String msg;
    private String sender;

    public Message(String msg, String sender) {
        this.msg = msg;
        this.sender = sender;
    }

    public String getMsg() {
        return msg;
    }

    public void setText(String text) {
        msg = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}