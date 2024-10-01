package com.bigoval.foodfever;


import org.json.JSONArray;

import java.util.List;

public class Menucatsitem {

    private String Catname;
    private List<Menuitems> MenuItems;
    private JSONArray JsonArray;

    public Menucatsitem() {
    }
    public Menucatsitem(String catname, List<Menuitems> menuitems,JSONArray jsonArray) {
        this.Catname = Catname;
        this.MenuItems = menuitems;
        this.JsonArray = jsonArray;


    }



    public String getCatname() {
        return Catname;
    }
    public JSONArray getProductsarray() {
        return JsonArray;
    }
    public List<Menuitems> getmenuItems() {
        return MenuItems;
    }


    public void setCatname(String catname) {
        this.Catname = catname;
    }
    public void setProductsarray(JSONArray jsonArray) {
        this.JsonArray = jsonArray;
    }
    public void setMenuItems(List<Menuitems> menuItems) {
        this.MenuItems = menuItems;
    }

}