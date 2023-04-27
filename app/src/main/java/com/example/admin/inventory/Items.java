package com.example.admin.inventory;

public class Items {
    private String itemname;
    private String itemcategory;
    private String itemlocation;
    private String itemprice;
    private String itembarcode;


public Items() {

}

public Items(String itemname,String itemcategory,String itemlocation, String itemprice,String itembarcode){

    this.itemname=itemname;
    this.itemcategory=itemcategory;
    this.itemlocation=itemlocation;
    this.itemprice=itemprice;
    this.itembarcode= itembarcode;
}



    public String getItemname() {
        return itemname;
    }

    public String getItemcategory() {
        return itemcategory;
    }
    public String getItemlocation() {
        return itemlocation;
    }

    public String getItemprice() {
        return itemprice;
    }

    public String getItembarcode() {
        return itembarcode;
    }
}