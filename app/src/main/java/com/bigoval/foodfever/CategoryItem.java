package com.bigoval.foodfever;

public class CategoryItem {
    private String mimage;
    private String mparent;
    private String mname;
    private String mid;
    private String mchildcount;

    public CategoryItem() {
    }

    public CategoryItem(String image, String parent, String name, String id, String childcount) {
        this.mimage = image;
        this.mparent = parent;
        this.mname = name;
        this.mid = id;
        this.mchildcount = childcount;

    }

    public String getimage() {
        return mimage;
    }

    public String getmname() {
        return mname;
    }

    public String getparent() {
        return mparent;
    }

    public String getid() {
        return mid;
    }

    public String getchildcount() {
        return mchildcount;
    }

    public void setname(String mname) {
        this.mname = mname;
    }

    public void setid(String mid) {
        this.mid = mid;
    }

    public void setimage(String mimage) {
        this.mimage = mimage;
    }

    public void setparent(String mparent) {
        this.mparent = mparent;
    }

    public void setchildcount(String mchildcount) {
        this.mchildcount = mchildcount;
    }
}