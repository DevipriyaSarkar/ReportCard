package com.devipriya.reportcard.models;

/**
 * Created by Devipriya on 10-Jul-15.
 */
public class MarksRow {

    private String subjectName;
    private int subjectMarks;

    public MarksRow() {
        this.subjectName = "";
        this.subjectMarks = 0;
    }

    public MarksRow(String subjectName, int subjectMarks) {
        this.subjectName = subjectName;
        this.subjectMarks = subjectMarks;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getSubjectMarks() {
        return subjectMarks;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setSubjectMarks(int subjectMarks) {
        if(subjectMarks < 0)
            this.subjectMarks = 0;
        else
            this.subjectMarks = subjectMarks;
    }

}