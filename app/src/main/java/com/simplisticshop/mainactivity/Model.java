package com.simplisticshop.mainactivity;

public class Model {

    private String imageUrl;
    String prodbarcode;
    String prodname;
    String prodquantity;
    String proddescrip;
    String prodprice;
    String email;
    String date;
    String time;


    public Model(){

    }


    public Model (
                  String imageUrl,
                  String prodname,
                  String prodbarcode,
                  String prodquantity,
                  String proddescrip,
                  String prodprice,
                  String email,
                  String date,
                  String time)
    {

        this.imageUrl = imageUrl;
        this.prodname = prodname;
        this.prodbarcode = prodbarcode;
        this.prodquantity = prodquantity;
        this.proddescrip = proddescrip;
        this.prodprice = prodprice;
        this.email = email;
        this.date = date;
        this.time = time;

    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProdname() {
        return prodname;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public String getProdbarcode() {
        return prodbarcode;
    }

    public void setProdbarcode(String prodbarcode) {
        this.prodbarcode = prodbarcode;
    }

    public String getProdquantity() {
        return prodquantity;
    }

    public void setProdquantity(String prodquantity) {
        this.prodquantity = prodquantity;
    }

    public String getProddescrip() {
        return proddescrip;
    }

    public void setProddescrip(String proddescrip) {
        this.proddescrip = proddescrip;
    }

    public String getProdprice() {
        return prodprice;
    }

    public void setProdprice(String prodprice) {
        this.prodprice = prodprice;
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
