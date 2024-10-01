package com.bigoval.foodfever;

public class OrderedItem {
    private String mImageUrl;
    private String mname;
    private String mseller;
    private String morderedon;
    private String mstatus;
    private String mquantity;
    private String mtext;
    private String mitemid;
    private int mprice;

    public OrderedItem(String imageUrl, String name, int price, String seller, String orderedon,String status,String itemid,String quantity,String text) {
        this.mImageUrl = imageUrl;
        this.mname = name;
        this.mprice = price;
        this.mseller = seller;
        this.morderedon = orderedon;
        this.mstatus = status;
        this.mitemid = itemid;
        this.mquantity = quantity;
        this.mtext = text;

    }

    public OrderedItem() {

    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getname() {
        return mname;
    }

    public String getseller() {
        return mseller;
    }

    public String getorderedon() {
        return morderedon;
    }

    public String getstatus() {
        return mstatus;
    }

    public String getitemid() {
        return mitemid;
    }

    public String getquantity() {
        return mquantity;
    }

    public String gettext() {
        return mtext;
    }

    public int getprice() {
        return mprice;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setname(String mname) {
        this.mname = mname;
    }

    public void setseller(String mseller) {
        this.mseller = mseller;
    }

    public void setorderedon(String morderedon) {
        this.morderedon = morderedon;
    }

    public void setstatus(String mstatus) {
        this.mstatus = mstatus;
    }

    public void setitemid(String mitemid) {
        this.mitemid = mitemid;
    }

    public void setquantity(String mquantity) {
        this.mquantity = mquantity;
    }

    public void settext(String mtext) {
        this.mtext = mtext;
    }

    public void setprice(int mprice) {
        this.mprice = mprice;
    }

}