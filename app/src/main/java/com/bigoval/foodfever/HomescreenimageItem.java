package com.bigoval.foodfever;

public class HomescreenimageItem {
    private String mImageUrl;
    private String mname;
    private String mseller;
    private String mtype;
    private String mcategory;
    private String mproduct;
    private String mlink;

    public HomescreenimageItem() {}
    public HomescreenimageItem(String imageUrl, String name,String seller,String type,String category,String product,String link) {
        this.mImageUrl = imageUrl;
        this.mname = name;
        this.mseller = seller;
        this.mcategory = category;
        this.mtype = type;
        this.mproduct = product;
        this.mlink = link;

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
    public String gettype() {
        return mtype;
    }
    public String getcategory() {
        return mcategory;
    }
    public String getproduct() {
        return mproduct;
    }
    public String getlink() {
        return mlink;
    }
    public void setImageUrl(String mImageUrl) {
        this.mImageUrl= mImageUrl;
    }
    public void setname(String mname) {
        this.mname=mname;
    }
    public void setseller(String mseller) {
        this.mseller = mseller;
    }
    public void settype(String mtype) {
        this.mtype = mtype;
    }
    public void setcategory(String mcategory) {
        this.mcategory = mcategory;
    }
    public void setproduct(String mproduct) {
        this.mproduct = mproduct;
    }
    public void setlink(String mlink) {
        this.mlink = mlink;
    }


}