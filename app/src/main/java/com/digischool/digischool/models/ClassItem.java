package com.digischool.digischool.models;

public class ClassItem {
    String classy;
    String exam_name;
    String names;
    String stdreg_no;
    String term;
    String total;

    public ClassItem(String names, String stdreg_no, String classy, String exam_name, String term, String total) {
        this.names = names;
        this.stdreg_no = stdreg_no;
        this.classy = classy;
        this.exam_name = exam_name;
        this.term = term;
        this.total = total;
    }

    public String getNames() {
        return this.names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getStdreg_no() {
        return this.stdreg_no;
    }

    public void setStdreg_no(String stdreg_no) {
        this.stdreg_no = stdreg_no;
    }

    public String getClassy() {
        return this.classy;
    }

    public void setClassy(String classy) {
        this.classy = classy;
    }

    public String getExam_name() {
        return this.exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getTerm() {
        return this.term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTotal() {
        return this.total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
