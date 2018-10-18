package com.example.padcc.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.padcc.weatherapp.ItemClickListener;
import com.example.padcc.weatherapp.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemClickListener {

    public ArrayList<Weather> weatherarray = new ArrayList<Weather>();
    private String TAG = MainActivity.class.getSimpleName();

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_weather);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerAdapter(MainActivity.this, weatherarray);
        recyclerAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerAdapter);


        new GetWeather().execute();


    }

    @Override
    public void onItemClick(View view, int position) {
       // Toast.makeText(getApplicationContext(),productsArr.get(position).getProductName(),Toast.LENGTH_SHORT).show();
    }

    private class GetWeather extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String url = "https://samples.openweathermap.org/data/2.5/forecast?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";
            String jsonStr = "";
            try {
                // Making a request to url and getting response
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(url));
                HttpResponse response = client.execute(request);
                jsonStr = EntityUtils.toString(response.getEntity());
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
//                    // Getting JSON Array
//                   JSONArray users = new JSONArray(jsonStr);
//
//                    // looping through All Contacts
//                    for (int i = 0; i < users.length(); i++) {
//                        JSONObject pObj = users.getJSONObject(i);
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray jsonArray = new JSONArray();
                    jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        JSONArray jsonweatherArray = new JSONArray();
                        jsonweatherArray = data.getJSONArray("weather");
                        for (int j = 0; j < jsonweatherArray.length(); j++) {
                            JSONObject weatherdata = jsonweatherArray.getJSONObject(j);

                            Weather weatherData = new Weather();
                            try {
                                weatherData.setId(weatherdata.getString("id"));
                                weatherData.setMain(weatherdata.getString("main"));
                                weatherData.setDescription(weatherdata.getString("description"));
                                weatherData.setIcon(weatherdata.getString("icon"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                              }

                            //  weatherdata.setUnit(weatherdata.getString("unit"));

                            weatherarray.add(weatherData);
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                Toast.makeText(getApplicationContext(),
                        "Couldn't get json from server. Check LogCat for possible errors!",
                        Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            recyclerAdapter.notifyDataSetChanged();

        }
    }
}
