package com.devipriya.reportcard;

/**
 * Created by Devipriya on 10-Jul-15.
 */
public class MarksRow {

    public String subjectName;
    public int subjectMarks;

    public MarksRow() {
        this.subjectName = "";
        this.subjectMarks = 0;
    }

    public MarksRow(String testName, int testMaxMarks) {
        this.subjectName = testName;
        this.subjectMarks = testMaxMarks;
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