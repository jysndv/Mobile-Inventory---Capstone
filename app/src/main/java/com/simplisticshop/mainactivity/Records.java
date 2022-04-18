package com.simplisticshop.mainactivity;

public class Records {

    String prodbarcode;
    String prodname;
    String prodquantity;
    String email;
    String time;
    String quantityOut;

    public Records(){

    }

    public Records(String prodname,
                   String prodbarcode,
                   String prodquantity,
                   String email,
                   String time,
                   String quantityOut)
    {
        this.prodname = prodname;
        this.prodbarcode = prodbarcode;
        this.prodquantity = prodquantity;
        this.email = email;
        this.time = time;
        this.quantityOut = quantityOut;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQuantityOut() {
        return quantityOut;
    }

    public void setQuantityOut(String quantityOut) {
        this.quantityOut = quantityOut;
    }
}


