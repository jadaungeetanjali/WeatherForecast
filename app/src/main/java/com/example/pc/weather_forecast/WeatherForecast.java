package com.example.pc.weather_forecast;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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
        View rootView = inflater.inflate(R.layout.fragment_weather_forecast, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.weather_forecast_list);
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < 40; i++) {
            arrayList.add("DAY" + i + "temperature = " + i + "degree Celsius");
        }
        mForecastAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.forecast_dislpay,
                R.id.forecastDisplay,
                arrayList);
        listView.setAdapter(mForecastAdapter);
        WeatherData obj = new WeatherData();
        obj.execute();
        return rootView;

    }

    public class WeatherData extends AsyncTask<Void, Void, String> {
        private ArrayList<String> jsonWeatherParser(String jsonForecastData)throws JSONException{
            ArrayList<String> weatherArray = new ArrayList<>();
            JSONObject weatherObject = new JSONObject(jsonForecastData);
            JSONArray list = weatherObject.getJSONArray("list");
            for(int i = 0; i < list.length(); i++){
                JSONObject weather = list.getJSONObject(i);
                // to covert json date format in desired format
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date netDate = (new Date(Long.parseLong(weather.get("dt").toString())));
                String date = df.format(netDate);
                JSONObject temp = (JSONObject) weather.get("temp");
                // to extract maximum and minimum temperature from json list temp
                String max_temp = temp.get("max").toString();
                String min_temp = temp.get("min").toString();
                weatherArray.add(date + " " + max_temp + " " + min_temp);
            }
            return weatherArray;
        }
        // AsyncTask method doInBackground for url connection and background computation
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader br = null;
            String jsonForecast = null;
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=london,uk&APPID=c10e7100063f10864ba3ffb839aed7f3");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream input = urlConnection.getInputStream();
                if (input == null) {
                    jsonForecast = null;
                }
                StringBuffer stringBuffer = new StringBuffer();
                br = new BufferedReader(new InputStreamReader(input));
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    stringBuffer.append(inputLine + "\n");
                }
                if (stringBuffer.length() == 0) {
                    jsonForecast = null;
                }
                jsonForecast = stringBuffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jsonForecast;
        }
        //called when background computation is performed
        protected void onPostExecute(String forecastData) {
            ArrayList<String> forecastList = null;
            try {
                forecastList = jsonWeatherParser(forecastData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (forecastList.size() != 0) {
                //to clear fake data populated in list view
                mForecastAdapter.clear();
                //to add real data in listView
                mForecastAdapter.addAll(forecastList);
            }
        }
    }
}

