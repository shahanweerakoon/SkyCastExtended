package com.example.skycast;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

        String inputLocation = getIntent().getStringExtra("inputLocation");
        if (inputLocation == null || inputLocation.isEmpty()) {
            inputLocation = "kelaniya"; // Set a default location
        }

        TextView longitude = (TextView) findViewById(R.id.longitude);
        TextView latitude = (TextView) findViewById(R.id.latitude);
        TextView location = (TextView) findViewById(R.id.location);
        TextView time = (TextView) findViewById(R.id.time);
        TextView temperature = (TextView) findViewById(R.id.temperature);
        TextView humidity = (TextView) findViewById(R.id.humidity);
        TextView wind = (TextView) findViewById(R.id.wind);
        TextView weather = (TextView) findViewById(R.id.weather);
        TextView description = (TextView) findViewById(R.id.description);
        LinearLayout locationlayout = (LinearLayout) findViewById(R.id.locationLayout);

        String currentTime = getCurrentTime();
        time.setText(currentTime);

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + inputLocation + "&appid=2e33b0da032430d81e04947b8535c068" ;

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            // Extract coordinates
                            JSONObject coord = jsonObject.getJSONObject("coord");
                            double lon = coord.getDouble("lon");
                            double lat = coord.getDouble("lat");

                            // Extract main weather details
                            JSONObject main = jsonObject.getJSONObject("main");
                            double temp = main.getDouble("temp");
                            int humidityValue = main.getInt("humidity");

                            // Extract wind details
                            JSONObject windValue = jsonObject.getJSONObject("wind");
                            double windSpeed = windValue.getDouble("speed");

                            // Extract weather details
                            JSONArray weatherArray = jsonObject.getJSONArray("weather");
                            JSONObject weatherObj = weatherArray.getJSONObject(0);
                            String mainWeather = weatherObj.getString("main");
                            String descriptionValue = weatherObj.getString("description");

                            // Extract location name
                            String name = jsonObject.getString("name");

                            // Convert temperature from Kelvin to Celsius
                            double tempCal = temp - 273.15;
                            int tempCelsius = (int) tempCal;

                            // Set values to TextViews
                            longitude.setText("" + lon);
                            latitude.setText("" + lat);
                            location.setText(name);
                            temperature.setText("" + tempCelsius);
                            humidity.setText("" + humidityValue + "%");
                            wind.setText("" + windSpeed + " m/s");
                            weather.setText(mainWeather);
                            description.setText(descriptionValue);
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

        locationlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, dialog_search.class);
                startActivity(intent);
            }
        });
    }

    private String getCurrentTime() {
        // Create a SimpleDateFormat instance with the desired format
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        // Get the current date and time
        Date now = new Date();
        // Format the date and return the string
        return sdf.format(now);
    }


}