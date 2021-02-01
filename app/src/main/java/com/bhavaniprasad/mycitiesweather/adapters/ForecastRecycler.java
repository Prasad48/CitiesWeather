package com.bhavaniprasad.mycitiesweather.adapters;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bhavaniprasad.mycitiesweather.R;
import com.bhavaniprasad.mycitiesweather.models.City;
import com.bhavaniprasad.mycitiesweather.models.Forecast;
import com.bhavaniprasad.mycitiesweather.models.StoreData;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.List;
import java.util.TimeZone;

public class ForecastRecycler extends RecyclerView.Adapter<ForecastRecycler.RecyclerViewHolder> {
    List<Forecast> forecast;
    List<City> city;

//    https://www.metaweather.com/static/img/weather/png/64/hr.png
    public static final String IMG_URL = "https://www.metaweather.com/static/img/weather/png/64/";
    Context context;
    LayoutInflater inflater;
    private Double temperaturecelsius,mintemperature,maxtemperature;


    public ForecastRecycler(List city, List forecast, Context context) {
        this.forecast = forecast;
        this.city=city;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

//    public void setForecast(Forecast forecast) {
//        this.forecast = forecast;
//    }

    @NonNull
    @Override
    public ForecastRecycler.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.row_item,viewGroup,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ForecastRecycler.RecyclerViewHolder recyclerViewHolder, int i) {

        temperaturecelsius=forecast.get(i).getThe_temp().doubleValue();
        recyclerViewHolder.temperature.setText(Math.round(temperaturecelsius)+" \u2103");
        mintemperature=forecast.get(i).getMin_temp().doubleValue();
        recyclerViewHolder.mintemp.setText("Minimum: "+Math.round(mintemperature)+" \u2103");
        maxtemperature=forecast.get(i).getMax_temp().doubleValue();
        recyclerViewHolder.maxtemp.setText("Maximum :"+Math.round(maxtemperature)+" \u2103");
        String iconName=forecast.get(i).getWeather_state_abbr();
        Picasso.get().load(IMG_URL +iconName +".png").into(recyclerViewHolder.weathertypeimg);
        recyclerViewHolder.weathertype.setText(forecast.get(i).getWeather_state_name());
        int humidity=forecast.get(i).getHumidity();
        int pressure=forecast.get(i).getAir_pressure().intValue();
        Double w=forecast.get(i).getWind_speed().doubleValue();
        recyclerViewHolder.humidity.setText(""+humidity);
        recyclerViewHolder.pressure.setText(""+pressure);
        recyclerViewHolder.wind.setText(w.toString());
        recyclerViewHolder.location.setText(city.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return city.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView feelike,location,timestampdt,temperature,weathertype,mintemp,maxtemp,sunrise,sunset,wind,pressure,humidity;
        ImageView weathertypeimg;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            location=itemView.findViewById(R.id.CITY);
            temperature=itemView.findViewById(R.id.temperature);
            weathertype=itemView.findViewById(R.id.WeatherType);
            mintemp=itemView.findViewById(R.id.mintemp);
            maxtemp=itemView.findViewById(R.id.maxtemp);
            wind=itemView.findViewById(R.id.Wind);
            weathertypeimg=itemView.findViewById(R.id.weatherTypeImg);
            humidity=itemView.findViewById(R.id.Humidity);
            pressure=itemView.findViewById(R.id.pressure);
        }

    }
}

