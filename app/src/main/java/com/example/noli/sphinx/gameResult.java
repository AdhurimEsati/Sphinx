package com.example.noli.sphinx;

import java.util.List;

/**
 * Created by albnor on 8.5.2017.
 */

public class gameResult {
    private static String myUsername;
    private static String oponentUsername;

    private static List<Integer> myArray;
    private static List<Integer> oponentArray;
    private static List<Question> questions;

    public gameResult(String m, String o, List<Integer> mArr, List<Integer> oArr){
        myUsername = m;
        oponentUsername = o;
        myArray = mArr;
        oponentArray = oArr;
    }

    public static List<Question> getQuestions() {
        return questions;
    }

    public static void setQuestions(List<Question> questions) {
        gameResult.questions = questions;
    }

    public static String getMyUsername() {
        return myUsername;
    }

    public static void setMyUsername(String myU) {
        gameResult.myUsername = myU;
    }

    public static String getOponentUsername() {
        return oponentUsername;
    }

    public static void setOponentUsername(String oponentU) {
        gameResult.oponentUsername = oponentU;
    }

    public static List<Integer> getMyArray() {
        return myArray;
    }

    public static void setMyArray(List<Integer> myA) {
        gameResult.myArray = myA;
    }

    public static List<Integer> getOponentArray() {
        return oponentArray;
    }

    public static void setOponentArray(List<Integer> oponentA) {
        gameResult.oponentArray = oponentA;
    }
}
