package com.simplisticshop.mainactivity;

public class LoginRecord {

    String email;
    String date;
    String time;

    public LoginRecord(){}

    public LoginRecord(String email,
                       String date,
                       String time){

        this.email = email;
        this.date = date;
        this.time = time;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
