package com.example.pc.weather_forecast;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherForecast extends Fragment {

    public ArrayAdapter<String> mForecastAdapter;
    public WeatherForecast() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_weather_forecast, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.weather_forecast_list);
        ArrayList<String> arrayList = new ArrayList<String>();
        for(int i = 0; i < 40; i++){
            arrayList.add("DAY" +i+ "temperature = " +i+ "degree Celsius");
        }
        mForecastAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.forecast_dislpay,
                R.id.forecastDisplay,
                arrayList);
         listView.setAdapter(mForecastAdapter);
        return rootView;

    }
}
