package com.simplisticshop.mainactivity;

public class Products {
    String prodbarcode;
    String prodname;
    String prodquantity;
    String proddescrip;
    String prodprice;
    String proddate;
    String prodtime;
    String prodimage;


    public Products(){
        //Empty Constructor Needed
    }




    public Products(String prodbarcode,
                    String prodname,
                    String prodquantity,
                    String proddescrip,
                    String prodprice,
                    String proddate,
                    String prodtime) {

        this.prodbarcode = prodbarcode;
        this.prodname = prodname;
        this.prodquantity = prodquantity;
        this.proddescrip = proddescrip;
        this.prodprice = prodprice;
        this.proddate = proddate;
        this.prodtime = prodtime;
        this.prodimage = prodimage;
        
    }

    public String getProdbarcode() {
        return prodbarcode;
    }

    public String getProdname() {
        return prodname;
    }

    public String getProdquantity() {
        return prodquantity;
    }

    public String getProddescrip() {
        return proddescrip;
    }

    public String getProdprice() {
        return prodprice;
    }

    public String getProddate() {
        return proddate;
    }

    public String getProdtime() {
        return prodtime;
    }

    public String getProdimage() { return prodimage; }

}
