package com.example.noli.sphinx;

/**
 * Created by User on 21-Apr-17.
 */

public class Invitation {

    private String inviter;
    private String gamekey;
    private boolean accepted;
    private String date;

    public Invitation(){

    }

    public Invitation(String inviter, String gamekey, boolean accepted, String date) {
        this.inviter = inviter;
        this.gamekey = gamekey;
        this.accepted = accepted;
        this.date = date;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public String getGamekey() {
        return gamekey;
    }

    public void setGamekey(String gamekey) {
        this.gamekey = gamekey;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date= date;
    }
}
