package com.bigoval.foodfever;


public class Menuitems {

    String Itemname;
    String ItemPrice;
    String Itemcat;
    String ItemImage;
    String Id;
    boolean Writeable;


    public String getItemname() {
        return Itemname;
    }
    public String getItemPrice() {
        return ItemPrice;
    }
    public String getItemcat() {
        return Itemcat;
    }
    public String getItemImage() {
        return ItemImage;
    }
    public String getItemId() {
        return Id;
    }
    public boolean getWriteable(){return Writeable;}


    public void setItemname(String itemname) {
        this.Itemname = itemname;
    }
    public void setItemPrice(String itemPrice) {
        this.ItemPrice = itemPrice;
    }
    public void setItemcat(String itemcat) {
        this.Itemcat = itemcat;
    }
    public void setItemImage(String itemImage) {
        this.ItemImage = itemImage;
    }
    public void setItemId(String itemId) {
        this.Id = itemId;
    }
    public void setWriteable(boolean writeable) {
        this.Writeable = writeable;
    }



}