package com.bhavaniprasad.mycitiesweather.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhavaniprasad.mycitiesweather.models.City;
import com.bhavaniprasad.mycitiesweather.models.StoreData;
import com.bhavaniprasad.mycitiesweather.repo.RetrofitGenerator;
import com.bhavaniprasad.mycitiesweather.interfaces.Api;
import com.bhavaniprasad.mycitiesweather.models.Forecast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    public MutableLiveData<List<Forecast>> forecastMutableLiveData;
    public MutableLiveData<List<City>> cityWoeidMutableLiveData;

    public MutableLiveData<Boolean> showProgressBar = new MutableLiveData<>();
    public MutableLiveData<Boolean> show_networkError = new MutableLiveData<>();
    private Call<List<Forecast>> apicallForecast;
    private Call<List<City>> apicall_woeid;
    public List cityObjects;
    private List<Forecast> forecastObjects;


    private StoreData storeData;
    public String todayDate;

    public LiveData<Boolean> getShowProgressBar() {
        return showProgressBar;
    }


    public MutableLiveData<Boolean> getShow_networkError() {
        return show_networkError;
    }

    public LiveData<List<City>> getcityWoeidMutableLiveData(String cityName) {
        cityObjects=new ArrayList<>();
            cityWoeidMutableLiveData = new MutableLiveData<>();
            Api api = RetrofitGenerator.getApi();
            try{
                    apicall_woeid = api.getWoeid(cityName);
                    apicall_woeid.enqueue(new Callback<List<City>>() {
                        @Override
                        public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                            cityObjects.add(response.body().get(0));
                            cityWoeidMutableLiveData.setValue(cityObjects);
                        }

                        @Override
                        public void onFailure(Call<List<City>> call, Throwable t) {
                            String error_message= t.getMessage();
                            Log.d("Error loading data", error_message);
                            show_networkError.setValue(true);
                            showProgressBar.setValue(false);
                        }
                    });
            }
            catch (Exception e){
                Log.e("forecast","exception"+e);
            }
        return cityWoeidMutableLiveData;
    }


    public LiveData<List<Forecast>> getForecastMutableLiveData() {
        forecastObjects=new ArrayList<>();
        forecastMutableLiveData=new MutableLiveData<>();
        storeData=StoreData.getInstance();

        if(storeData.getCity()!=null && storeData.getCity().size()>0){
            Api api = RetrofitGenerator.getApi();
            Date cDate = new Date();
            todayDate = new SimpleDateFormat("yyyy/MM/dd").format(cDate);
            try{
                for(int index=0;index<storeData.getCity().size();index++){
                    apicallForecast = api.getForecast(storeData.getCity().get(index).getWoeid(),todayDate);
                    final int finalJ = index;
                    apicallForecast.enqueue(new Callback<List<Forecast>>() {
                        @Override
                        public void onResponse(Call<List<Forecast>> call, Response<List<Forecast>> response) {

                            Log.d("success", "onResponse: "+response.raw().toString());
                            forecastObjects.add(response.body().get(finalJ));
                            if(forecastObjects.size()==storeData.getCity().size())
                            forecastMutableLiveData.setValue(forecastObjects);
                        }

                        @Override
                        public void onFailure(Call<List<Forecast>> call, Throwable t) {
                            String error_message= t.getMessage();
                            Log.d("Error loading data", error_message);
                            show_networkError.setValue(true);
                            showProgressBar.setValue(false);
                        }
                    });
                }
            }
            catch (Exception e){
                Log.e("forecast","exception"+e);
            }
        }
        return forecastMutableLiveData;
    }

}
