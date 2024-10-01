package com.bigoval.foodfever;

public class ExampleItem {
    private String mImageUrl;
    private String mImageUrl2;
    private String mImageUrl3;
    private String mImageUrl4;
    private String mImageUrl5;
    private String mImageUrl6;
    private String mImageUrl7;
    private String mCreator;
    private String mseller;
    private String mid;
    private String mdescription;
    private String mcategory;
    private String msellerid;
    private String mquantitytype;
    private int mLikes;
    private int mrealprice;
    private int mStock;
    private boolean mwriteable;

    public ExampleItem() {
    }

    public ExampleItem(String imageUrl, String imageUrl2, String imageUrl3, String imageUrl4, String imageUrl5, String imageUrl6, String imageUrl7, String creator, int likes, String seller, String description, String id, int stock, String category, String sellerid, int realprice, String quantitytype,boolean writeable) {
        this.mImageUrl = imageUrl;
        this.mImageUrl2 = imageUrl2;
        this.mImageUrl3 = imageUrl3;
        this.mImageUrl4 = imageUrl4;
        this.mImageUrl5 = imageUrl5;
        this.mImageUrl6 = imageUrl6;
        this.mImageUrl7 = imageUrl7;
        this.mCreator = creator;
        this.mLikes = likes;
        this.mrealprice = realprice;
        this.mseller = seller;
        this.msellerid = sellerid;
        this.mdescription = description;
        this.mStock = stock;
        this.mid = id;
        this.mcategory = category;
        this.mquantitytype = quantitytype;
        this.mwriteable = writeable;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getImageUrl2() {
        return mImageUrl2;
    }

    public String getImageUrl3() {
        return mImageUrl3;
    }

    public String getImageUrl4() {
        return mImageUrl4;
    }

    public String getImageUrl5() {
        return mImageUrl5;
    }

    public String getImageUrl6() {
        return mImageUrl6;
    }

    public String getImageUrl7() {
        return mImageUrl7;
    }

    public String getCreator() {
        return mCreator;
    }

    public String getseller() {
        return mseller;
    }

    public String getsellerid() {
        return msellerid;
    }

    public String getdescription() {
        return mdescription;
    }

    public String getcategory() {
        return mcategory;
    }

    public String getid() {
        return mid;
    }

    public int getLikeCount() {
        return mLikes;
    }

    public int getrealprice() {
        return mrealprice;
    }

    public int getStock() {
        return mStock;
    }

    public boolean getwriteable(){return mwriteable;}

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setImageUrl2(String mImageUrl2) {
        this.mImageUrl2 = mImageUrl2;
    }

    public void setImageUrl3(String mImageUrl3) {
        this.mImageUrl3 = mImageUrl3;
    }

    public void setImageUrl4(String mImageUrl4) {
        this.mImageUrl4 = mImageUrl4;
    }

    public void setImageUrl5(String mImageUrl5) {
        this.mImageUrl5 = mImageUrl5;
    }

    public void setImageUrl6(String mImageUrl6) {
        this.mImageUrl6 = mImageUrl6;
    }

    public void setImageUrl7(String mImageUrl7) {
        this.mImageUrl7 = mImageUrl7;
    }

    public void setCreator(String mCreator) {
        this.mCreator = mCreator;
    }

    public void setsellerid(String mSellerid) {
        this.msellerid = mSellerid;
    }

    public void setseller(String mseller) {
        this.mseller = mseller;
    }

    public void setdescription(String mdescription) {
        this.mdescription = mdescription;
    }

    public void setcategory(String mcategory) {
        this.mcategory = mcategory;
    }

    public void setid(String mid) {
        this.mid = mid;
    }

    public void setLikeCount(int mLikes) {
        this.mLikes = mLikes;
    }

    public void setrealprice(int mrealprice) {
        this.mrealprice = mrealprice;
    }

    public void setStock(int mStock) {
        this.mStock = mStock;
    }

    public void setwriteable(boolean mwriteable){this.mwriteable = mwriteable;}
}