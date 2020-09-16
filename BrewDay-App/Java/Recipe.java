package com.example.beerday;

public class Recipe {
    String ing_name, unit;
    Double quantity;

    public void setIngName(String ing_name)
    {
        this.ing_name = ing_name;
    }
    public String getIngName()
    {
        return this.ing_name;
    }

    public void setIngQuantity(Double quantity)
    {
        this.quantity = quantity;
    }
    public Double getIngQuantity()
    {
        return this.quantity;
    }

    public void setIngUnit(String unit)
    {
        this.unit = unit;
    }
    public String getIngUnit()
    {
        return this.unit;
    }
}

