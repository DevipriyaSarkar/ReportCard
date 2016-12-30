package com.devipriya.reportcard.models;

/**
 * Created by Devipriya on 02-Aug-15.
 */
public class GradeRangeRow {

    private String gText;
    private int gScore;
    private String gChar;

    public GradeRangeRow(){
        this.gText = "Above ";
        this.gScore = 0;
        this.gChar = "";
    }

    public GradeRangeRow(int gScore, String gChar){
        this.gText = "Above ";
        this.gScore = gScore;
        this.gChar = gChar;
    }

    public GradeRangeRow(String gText, int gScore, String gChar){
        this.gText = gText;
        this.gScore = gScore;
        this.gChar = gChar;
    }

    public String getGChar() {
        return gChar;
    }

    public int getGScore() {
        return gScore;
    }

    public String getGText() {
        return gText;
    }

    public void setGChar(String gChar) {
        this.gChar = gChar;
    }

    public void setGScore(int gScore) {
        this.gScore = gScore;
    }

    public void setGText(String gText) {
        this.gText = gText;
    }
}
