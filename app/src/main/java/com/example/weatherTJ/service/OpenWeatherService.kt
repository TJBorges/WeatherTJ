package com.example.weatherTJ.service

import com.example.weatherTJ.model.City
import com.example.weatherTJ.model.Root
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {
    
    private val apikey: String
        get() = "337f5248688361c86e2cc887681ae8f6"

    @GET("weather")
    fun getCityWeather(
            @Query("q") cityName: String,
            @Query("ApiKey") apiKey: String = apikey
    ) : Call<City>
    
    @GET("find")
    fun getTemperatures(
            @Query("q") cityName: String,
            @Query("units") units: String =  "metrics",
            @Query("ApiKey") apiKey: String = apikey
    ) : Call<Root>
}