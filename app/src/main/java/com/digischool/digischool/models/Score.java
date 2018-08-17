package com.digischool.digischool.models;

public class Score {
    int mark;
    String title;

    public Score(String title, int mark) {
        this.title = title;
        this.mark = mark;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMark() {
        return this.mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
