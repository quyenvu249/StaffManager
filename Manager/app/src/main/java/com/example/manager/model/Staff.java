package com.example.manager.model;

public class Staff {
    String staffID;
    String email;
    String password;
    String avatarLink;
    String staffName;
    String phone;
    String birthday;
    String schedule;

    public Staff() {
    }

    public Staff(String staffID, String email, String password, String avatarLink, String staffName, String phone, String birthday, String schedule) {
        this.staffID = staffID;
        this.email = email;
        this.password = password;
        this.avatarLink = avatarLink;
        this.staffName = staffName;
        this.phone = phone;
        this.birthday = birthday;
        this.schedule = schedule;
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


    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
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

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }
}
