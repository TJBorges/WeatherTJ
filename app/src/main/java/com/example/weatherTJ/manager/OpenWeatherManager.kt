package com.example.weatherTJ.manager

import com.example.weatherTJ.service.OpenWeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenWeatherManager {

    private val instance = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getOpenWeatherService() = instance.create(OpenWeatherService::class.java)
}