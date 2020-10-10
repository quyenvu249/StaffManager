package com.example.manager.model;

public class Staff {
    String staffID;
    String email;
    String password;
    String staffName;
    String phone;
    String birthday;
    boolean shift1, shift2;

    public Staff() {
    }

    public Staff(String staffID, String email, String password, String staffName, String phone, String birthday, boolean shift1, boolean shift2) {
        this.staffID = staffID;
        this.email = email;
        this.password = password;
        this.staffName = staffName;
        this.phone = phone;
        this.birthday = birthday;
        this.shift1 = shift1;
        this.shift2 = shift2;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }


    public boolean isShift1() {
        return shift1;
    }

    public void setShift1(boolean shift1) {
        this.shift1 = shift1;
    }

    public boolean isShift2() {
        return shift2;
    }

    public void setShift2(boolean shift2) {
        this.shift2 = shift2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
