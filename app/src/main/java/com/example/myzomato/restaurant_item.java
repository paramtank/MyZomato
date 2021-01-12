package com.example.myzomato;

public class restaurant_item {
    private String res_name;
    private String locality;
    private String rating;
    private String cusine;
    public restaurant_item(String restaurant_name, String Locality,String Rating,String Cusine){
        res_name=restaurant_name;
        locality=Locality;
        rating=Rating;
        cusine=Cusine;
    }
    public String getRes_name(){
        return res_name;
    }
    public String getLocality(){
        return locality;
    }
    public String getRating(){
        return rating;
    }
    public String getCusine(){
        return cusine;
    }
}
