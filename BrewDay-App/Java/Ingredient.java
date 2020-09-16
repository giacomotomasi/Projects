package com.example.beerday;

public class Ingredient {
    String mying_name, myunit;
    Double myquantity;

    public void setMyIngName(String ing_name)
    {
        this.mying_name = ing_name;
    }
    public String getMyIngName()
    {
        return this.mying_name;
    }

    public void setMyIngQuantity(Double quantity)
    {
        this.myquantity = quantity;
    }
    public Double getMyIngQuantity()
    {
        return this.myquantity;
    }

    public void setMyIngUnit(String unit)
    {
        this.myunit = unit;
    }
    public String getMyIngUnit()
    {
        return this.myunit;
    }
}
