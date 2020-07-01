package com.joe.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        final TextView textView = findViewById(R.id.weatherText);
        final EditText editText = findViewById(R.id.editButton);

        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                String url =  String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&units=imperial&appid=9d82366d70ac901aa6d5c07ba194e996", editText.getText());


                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject rawWeatherData = null;
                                try {
                                    rawWeatherData = new JSONObject(response);
                                    Log.d("WEATHER", rawWeatherData.toString());

                                    JSONObject weatherData  = (JSONObject)rawWeatherData.getJSONArray("weather").get(0);
                                    JSONObject mainWeatherData = rawWeatherData.getJSONObject("main");


                                    String weatherDisplayString = String.format("%s, %s° F, feels like %s° F",
                                            weatherData.getString("description"),
                                            mainWeatherData.getInt("temp"),
                                            mainWeatherData.getInt("feels_like"));

                                    textView.setText(weatherDisplayString);
                                } catch (JSONException e) {
                                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
    }
}
