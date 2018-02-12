package com.example.noli.sphinx;

/**
 * Created by User on 11-Apr-17.
 */

public class Answer {

    private int nr;
    private String answer;
    private boolean isright;

    public Answer(int nr, String answer, boolean isright) {
        this.nr = nr;
        this.answer = answer;
        this.isright = isright;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isright() {
        return isright;
    }

    public void setIsright(boolean isright) {
        this.isright = isright;
    }
}
