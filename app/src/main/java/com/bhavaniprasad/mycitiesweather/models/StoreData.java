package com.bhavaniprasad.mycitiesweather.models;

import java.util.ArrayList;
import java.util.List;

public class StoreData {

    private static StoreData single_instance = null;
    private List<City> city;

    public void setCity(List<City> city) {
        this.city = city;
    }

    public List<City> getCity() {
        try {
            if(city.size()>0)
                return city;
        }
        catch (Exception e){
            return null;
        }
        return null;
    }

    public List<Forecast> weatherData;



    public static StoreData getInstance()
    {
        if (single_instance == null)
            single_instance = new StoreData();

        return single_instance;
    }

}
