package com.bigoval.foodfever;


public class homescreenshopsitem {

    String ShopImage;
    String Shopname;
    String Id;
    String ShopBanner;
    String ShopAddress;


    public String getShopImage() {
        return ShopImage;
    }
    public String getShopname() {
        return Shopname;
    }
    public String getShopBanner(){return ShopBanner;}
    public String getShopAddress(){return ShopAddress;}
    public String getId() {
        return Id;
    }


    public void setShopImage(String shopImage) {
        this.ShopImage = shopImage;
    }
    public void setShopname(String shopname) {
        this.Shopname = shopname;
    }
    public void setShopBanner(String shopBanner) {
        this.ShopBanner = shopBanner;
    }
    public void setShopAddress(String shopAddress) {
        this.ShopAddress = shopAddress;
    }
    public void setId(String id) {
        this.Id = id;
    }

}