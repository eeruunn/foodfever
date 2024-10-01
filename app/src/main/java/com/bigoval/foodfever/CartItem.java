package com.bigoval.foodfever;

public class CartItem {
    private String mImageUrl;
    private String mname;
    private String mseller;
    private String msellerid;
    private String mid;
    private String msize;
    private String mctext;
    private int mprice;
    private int mquantity;
    private int mdeliverablewithin;
    public CartItem() {}
    public CartItem(String imageUrl, String name, int price, int quantity, String seller, String id,String size,int deliverablewithin,String ctext,String sellerid) {
        this.mImageUrl = imageUrl;
        this.mname = name;
        this.msize = size;
        this.mctext = ctext;
        this.mprice = price;
        this.mquantity = quantity;
        this.mseller = seller;
        this.mid=id;
        this.mdeliverablewithin=deliverablewithin;
        this.msellerid=sellerid;
    }
    public String getImageUrl() {
        return mImageUrl;
    }
    public String getsize() {
        return msize;
    }
    public String getname() {
        return mname;
    }
    public String getseller() {
        return mseller;
    }
    public String getid() {
        return mid;
    }
    public String getctext() {
        return mctext;
    }
    public String getsellerid() {
        return msellerid;
    }
    public int getprice() {
        return mprice;
    }
    public int getquantity() {
        return mquantity;
    }
    public int getdeliverablewithin() {
        return mdeliverablewithin;
    }
    public void setImageUrl(String mImageUrl) {
        this.mImageUrl= mImageUrl;
    }
    public void setsize(String msize) {
        this.msize= msize;
    }
    public void setname(String mname) {
        this.mname=mname;
    }
    public void setseller(String mseller) {
        this.mseller = mseller;
    }
    public void setid(String mid) {
        this.mid = mid;
    }
    public void setctext(String mctext) {
        this.mctext = mctext;
    }
    public void setsellerid(String sellerid) {
        this.msellerid = sellerid;
    }
    public void setquantity(int mquantity) {
        this.mquantity=mquantity;
    }
    public void setprice(int mprice) {
        this.mprice=mprice;
    }
    public void setdeliverablewithin(int mdeliverablewithin) {
        this.mdeliverablewithin=mdeliverablewithin;
    }
}