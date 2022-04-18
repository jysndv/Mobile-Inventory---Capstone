package com.simplisticshop.mainactivity;

public class NewUser {

    String name, email, datecreated, timecreated;
    private int usertype;

    public int getUsertype(){
        return usertype;
    }

    public void setUsertype(int usertype){
        this.usertype = usertype;
    }

    public NewUser(){

    }

    public NewUser(String name, String email,  String datecreated, String timecreated, int usertype){
        this.name = name;
        this.email = email;
        this.datecreated = datecreated;
        this.timecreated = timecreated;
        usertype = usertype;

    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public String getTimecreated() {
        return timecreated;
    }
}
