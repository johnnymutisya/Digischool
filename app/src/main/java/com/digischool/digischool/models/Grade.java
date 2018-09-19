package com.digischool.digischool.models;

/**
 * Created by walter on 9/9/18.
 */

public class Grade {
    private int upper;
    private int lower;
    private String grade;

    public Grade(int upper, int lower, String garde) {
        this.upper = upper;
        this.lower = lower;
        this.grade = garde;
    }

    public int getUpper() {
        return upper;
    }

    public void setUpper(int upper) {
        this.upper = upper;
    }

    public int getLower() {
        return lower;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}