package com.digischool.digischool.models;

public class Student {
    String names, adm, stream;

    public Student(String names, String adm, String stream) {
        this.names = names;
        this.adm = adm;
        this.stream = stream;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getAdm() {
        return adm;
    }

    public void setAdm(String adm) {
        this.adm = adm;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }
}
