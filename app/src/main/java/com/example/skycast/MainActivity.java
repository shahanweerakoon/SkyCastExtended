package com.example.skycast;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView longitude = (TextView) findViewById(R.id.longitude);
        TextView latitude = (TextView) findViewById(R.id.latitude);
        TextView location = (TextView) findViewById(R.id.location);
        TextView temperature = (TextView) findViewById(R.id.temperature);
        TextView humidity = (TextView) findViewById(R.id.humidity);
        TextView wind = (TextView) findViewById(R.id.wind);
        TextView weather = (TextView) findViewById(R.id.weather);
        TextView description = (TextView) findViewById(R.id.description);
        String url = "https://api.openweathermap.org/data/3.0/onecall?lat=6.948974&lon=79.914424&appid=2e33b0da032430d81e04947b8535c068";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String timezone = jsonObject.getString("timezone");
                            JSONObject current = jsonObject.getJSONObject("current");
                            double temp = current.getDouble("temp");
                            int humidityValue = current.getInt("humidity");
                            double windSpeed = current.getDouble("wind_speed");
                            JSONArray weatherArray = current.getJSONArray("weather");
                            JSONObject weatherObj = weatherArray.getJSONObject(0);
                            String mainWeather = weatherObj.getString("main");
                            String descriptionValue = weatherObj.getString("description");

                            double tempCal= temp - 273.15;
                            int tempCelsius = (int) tempCal;


                            // Set values to TextViews
                            longitude.setText("" + jsonObject.getDouble("lon"));
                            latitude.setText("" + jsonObject.getDouble("lat"));
                            location.setText("" + timezone);
                            temperature.setText("" + tempCelsius );
                            humidity.setText("" + humidityValue + "%");
                            wind.setText("" + windSpeed + " m/s");
                            weather.setText("" + mainWeather);
                            description.setText("" + descriptionValue);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        throw new RuntimeException(volleyError);
                    }
                }
        );

        queue.add(stringRequest);
    }
}