package com.example.noli.sphinx;

/**
 * Created by User on 21-Apr-17.
 */

public class Game {
    private String creator;
    private String joiner;
    private int connect1;
    private int connect2;
    private int firstresult;
    private int secondresult;
    private String winner;
    private String losser;
    private int draw;
    private boolean finished = false;
    private String quiz;

    public Game(String creator, String joiner, int connect1, int connect2, int firstresult, int secondresult, String winner, String losser, int draw, boolean finished,String quiz) {
        this.creator = creator;
        this.joiner = joiner;
        this.connect1 = connect1;
        this.connect2 = connect2;
        this.firstresult = firstresult;
        this.secondresult = secondresult;
        this.winner = winner;
        this.losser = losser;
        this.draw = draw;
        this.finished = finished;
        this.quiz = quiz;
    }

    public Game(){

    }
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getJoiner() {
        return joiner;
    }

    public void setJoiner(String joiner) {
        this.joiner = joiner;
    }

    public int getConnect1() {
        return connect1;
    }

    public void setConnect1(int connect1) {
        this.connect1 = connect1;
    }

    public int getConnect2() {
        return connect2;
    }

    public void setConnect2(int connect2) {
        this.connect2 = connect2;
    }

    public int getFirstresult() {
        return firstresult;
    }

    public void setFirstresult(int firstresult) {
        this.firstresult = firstresult;
    }

    public int getSecondresult() {
        return secondresult;
    }

    public void setSecondresult(int secondresult) {
        this.secondresult = secondresult;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getLosser() {
        return losser;
    }

    public void setLosser(String losser) {
        this.losser = losser;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }
}
