package com.digischool.digischool.models;

public class AttendanceItem {
    String names;
    String phoneNumber;
    boolean present = true;
    String studentId;

    public String getStudentId() {
        return this.studentId;
    }

    public AttendanceItem(String studentId, String names, String phoneNumber, boolean present) {
        this.studentId = studentId;
        this.names = names;
        this.phoneNumber = phoneNumber;
        this.present = present;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getNames() {
        return this.names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isPresent() {
        return this.present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
