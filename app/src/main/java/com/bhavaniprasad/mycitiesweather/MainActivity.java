package com.bhavaniprasad.mycitiesweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import android.arch.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bhavaniprasad.mycitiesweather.adapters.ForecastRecycler;
import com.bhavaniprasad.mycitiesweather.models.City;
import com.bhavaniprasad.mycitiesweather.models.Forecast;
import com.bhavaniprasad.mycitiesweather.models.StoreData;
import com.bhavaniprasad.mycitiesweather.repo.RetrofitGenerator;
import com.bhavaniprasad.mycitiesweather.viewmodels.MainViewModel;
import com.google.gson.JsonElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function4;
import io.reactivex.functions.Function5;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private RelativeLayout layout_nonetwork;
    public MainViewModel mainViewModel;
    Button retry;
    RecyclerView recyclerView;
    private ForecastRecycler forecastRecycler;
    StoreData storeData;
    List forecastWeatherData;
    List<String> cityList;
    ThreadPoolExecutor executor,executor2;
    int reccount=0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retry=findViewById(R.id.retry);
        layout_nonetwork=findViewById(R.id.no_network);
        recyclerView=findViewById(R.id.recycler_View);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#00BFFF")));
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.Sky));
        progressBar = findViewById(R.id.progress_bar);
        if(isNetworkAvailable()){
            layout_nonetwork.setVisibility(View.GONE);
            callAllApis();
        }
        else {
            layout_nonetwork.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    progressBar.setVisibility(View.VISIBLE);
                    layout_nonetwork.setVisibility(View.GONE);
                    callAllApis();
                }
                else{
                    layout_nonetwork.setVisibility(View.VISIBLE);
                }
            }
        });

        mainViewModel.getShowProgressBar().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        mainViewModel.getShow_networkError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    layout_nonetwork.setVisibility(View.VISIBLE);
                } else {
                    layout_nonetwork.setVisibility(View.GONE);
                }
            }
        });
    }

    private void callAllApis() {
        List<Future<Long>> list = new ArrayList<Future<Long>>();
        BlockingQueue<Runnable> runnables = new ArrayBlockingQueue<Runnable>(1024);
        BlockingQueue<Runnable> runnables2 = new ArrayBlockingQueue<Runnable>(1024);
        executor = new ThreadPoolExecutor(4, 4, 1, TimeUnit.NANOSECONDS, runnables);
        cityList=new ArrayList();
        cityList.add("Hyderabad");
        cityList.add("Chennai");
        cityList.add("Bangalore");
        cityList.add("Kolkata");


        for (int i = 0; i < cityList.size(); i++) {
            try {
                Future<Long> future = executor.submit(new LongOperation(cityList.get(i)));
                list.add(future);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e("Exce","sd");
                // We were cancelled; stop sleeping!
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private final class LongOperation implements Callable {
        public String item;
        public LongOperation(String item) {
            this.item=item;
        }

        @Override
        public Object call() throws Exception {
            forecastWeatherData=new ArrayList<>();
            storeData=StoreData.getInstance();

            try{
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        mainViewModel.getcityWoeidMutableLiveData(item).observe(MainActivity.this, new Observer<List<City>>() {
                            @Override
                            public void onChanged(List<City> cities) {
                                storeData.setCity(cities);
                                reccount++;
                                if(reccount==4){
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        public void run() {
                                            mainViewModel.getForecastMutableLiveData().observe(MainActivity.this, new Observer<List<Forecast>>() {
                                                @Override
                                                public void onChanged(List<Forecast> forecasts) {
                                                    if(forecasts.size()==cityList.size()){
                                                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                                        forecastRecycler = new ForecastRecycler(storeData.getCity(),forecasts,MainActivity.this);
                                                        recyclerView.setAdapter(forecastRecycler);
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                    }

                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

            }
            catch (Exception e){
                Log.e("Caught Excecption","Caught"+e);
            }
            return null;
        }
    }

}


