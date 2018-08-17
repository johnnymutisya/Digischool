package com.digischool.digischool.models;

public class Marks {
    private int adm;
    private String names;
    private int score;

    public Marks(int score, int adm, String names) {
        this.score = score;
        this.adm = adm;
        this.names = names;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getAdm() {
        return this.adm;
    }

    public void setAdm(int adm) {
        this.adm = adm;
    }

    public String getNames() {
        return this.names;
    }

    public void setNames(String names) {
        this.names = names;
    }
}
