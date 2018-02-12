package com.example.noli.sphinx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 21-Apr-17.
 */

public class Player {

    private String name;
    private int wins;
    private int draws;
    private int losses;
    private int isAvailable;
    public List<Invitation> invitations = new ArrayList<>();

   public Player(){

    }

    public Player(String name, int wins, int draws, int losses, int is, List<Invitation> invitations) {
        this.name = name;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        isAvailable= is;
        this.invitations = invitations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWins() {
        return wins;
    }

    public int getisAvailable() {
        return isAvailable;
    }

    public void setisAvailable(int isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public List<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }
}
