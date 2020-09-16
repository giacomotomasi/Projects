package com.example.beerday;

public class Beer {
    String beer_name;
    Integer time;

    public void setBeerName(String beer_name)
    {
        this.beer_name = beer_name;
    }
    public String getBeerName()
    {
        return this.beer_name;
    }

    public void setBeerTime(Integer time)
    {
        this.time = time;
    }
    public Integer getBeerTime()
    {
        return this.time;
    }
}

