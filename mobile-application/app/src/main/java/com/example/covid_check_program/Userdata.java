package com.example.covid_check_program;

public class Userdata {

    private String userName;
    private String userPhone;
    private String userEmail;
    private String UUID;
    private String Major;
    private String Minor;
    private String timeData;

    public Userdata(){
        this.userName = "NULL";
        this.userPhone = "NULL";
        this.userEmail = "NULL";
        this.UUID = "NULL";
        this.Major = "NULL";
        this.Minor = "NULL";
        this.timeData = "NULL";
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUUID() {
        return UUID;
    }

    public String getMajor() {
        return Major;
    }

    public String getMinor() {
        return Minor;
    }

    public String getTimeData() {
        return timeData;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public void setMajor(String major) {
        Major = major;
    }

    public void setMinor(String minor) {
        Minor = minor;
    }

    public void setTimeData(String timeData) {
        this.timeData = timeData;
    }

}
