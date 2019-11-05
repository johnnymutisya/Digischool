package com.digischool.digischool.models;

import java.util.HashMap;
import java.util.List;

public class DailyAttendanceItem {
    String fname,lname, department;
    List<String> attendance;

    public DailyAttendanceItem(String fname, String lname, String department, List<String> attendance) {
        this.fname = fname;
        this.lname = lname;
        this.department = department;
        this.attendance = attendance;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getAttendance() {
        return attendance;
    }

    public void setAttendance(List<String> attendance) {
        this.attendance = attendance;
    }
}
