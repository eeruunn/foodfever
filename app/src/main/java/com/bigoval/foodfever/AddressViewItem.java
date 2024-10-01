package com.bigoval.foodfever;

public class AddressViewItem {
    private String mphone;
    private String mname;
    private String mlandmark;
    private String mpincode;
    private String mhousename;
    private String mtownname;
    private String maddress;
    private String mid;

    public AddressViewItem() {}
    public AddressViewItem(String phone,String name,String landmark,String pincode,String housename,String townname,String address,String id) {
        this.mphone = phone;
        this.mname = name;
        this.mlandmark = landmark;
        this.mpincode = pincode;
        this.mhousename = housename;
        this.mtownname = townname;
        this.maddress = address;
        this.mid = id;
    }
    public String getPhone() {
        return mphone;
    }
    public String getname() {
        return mname;
    }
    public String getLandmark() {
        return mlandmark;
    }
    public String getPincode() {
        return mpincode;
    }
    public String getHousename() {
        return mhousename;
    }
    public String getTownname() {
        return mtownname;
    }
    public String getAddress() {
        return maddress;
    }
    public String getId() {
        return mid;
    }

    public void setPhone(String mphone) {
        this.mphone= mphone;
    }
    public void setname(String mname) {
        this.mname=mname;
    }
    public void setPincode(String mpincode) {
        this.mpincode = mpincode;
    }
    public void setHousename(String mhousename) {
        this.mhousename= mhousename;
    }
    public void setTownname(String mtownname) {
        this.mtownname=mtownname;
    }
    public void setAddress(String maddress) {
        this.maddress = maddress;
    }
    public void setLandmark(String mlandmark) {
        this.mlandmark = mlandmark;
    }
    public void setId(String mid) {
        this.mid = mid;
    }

}