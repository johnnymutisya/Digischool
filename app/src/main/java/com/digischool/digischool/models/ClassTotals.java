package com.digischool.digischool.models;

public class ClassTotals {
    String classy;
    String total;

    public ClassTotals(String classy, String total) {
        this.classy = classy;
        this.total = total;
    }

    public String getClassy() {
        return this.classy;
    }

    public void setClassy(String classy) {
        this.classy = classy;
    }

    public String getTotal() {
        return this.total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
