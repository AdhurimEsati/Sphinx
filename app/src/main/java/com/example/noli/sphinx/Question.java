package com.example.noli.sphinx;

import java.util.List;

/**
 * Created by User on 11-Apr-17.
 */

public class Question {

    private int nr;
    private String pyetja;
    private List<String> pergjigjet;

    public Question(int nr, String pyetja, List<String> pergjigjet) {
        this.nr = nr;
        this.pyetja = pyetja;
        this.pergjigjet = pergjigjet;

    }



    public Question(){

    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public String getPyetja() {
        return pyetja;
    }

    public void setPyetja(String pyetja) {
        this.pyetja = pyetja;
    }

    public List<String> getPergjigjet() {
        return pergjigjet;
    }

    public void setPergjigjet(List<String> pergjigjet) {
        this.pergjigjet = pergjigjet;
    }
}
