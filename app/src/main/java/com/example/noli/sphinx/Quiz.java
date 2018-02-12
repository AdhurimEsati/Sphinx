package com.example.noli.sphinx;

import java.util.List;
import java.util.Map;

/**
 * Created by User on 11-Apr-17.
 */

public class Quiz {

    private String emri;
    private String pershkrimi;
    List<Question> pyetjet;

    public Quiz(String emri, String pershkrimi, List<Question> pyetjet) {
        this.emri = emri;
        this.pershkrimi = pershkrimi;
        this.pyetjet = pyetjet;

    }

    public Quiz(){

    }

    public String getEmri() {
        return emri;
    }

    public void setEmri(String emri) {
        this.emri = emri;
    }

    public String getPershkrimi() {
        return pershkrimi;
    }

    public void setPershkrimi(String pershkrimi) {
        this.pershkrimi = pershkrimi;
    }

    public List<Question> getPyetjet() {
        return pyetjet;
    }

    public void setPyetjet(List<Question> pyetjet) {
        this.pyetjet = pyetjet;
    }
}
