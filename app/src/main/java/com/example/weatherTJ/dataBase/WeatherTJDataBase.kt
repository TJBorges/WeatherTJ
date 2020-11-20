package com.example.weatherTJ.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherTJ.dao.CityDataBaseDao
import com.example.weatherTJ.model.CityDataBase

@Database(entities = arrayOf(CityDataBase::class), version = 1)
abstract class WeatherTJDataBase: RoomDatabase() {

    abstract fun cityDataBaseDao(): CityDataBaseDao

    companion object{
        private var INSTANCE: WeatherTJDataBase? = null

        fun getInstance(context: Context): WeatherTJDataBase? {

            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                            WeatherTJDataBase::class.java, "WeatherTJ.db")
                            .allowMainThreadQueries().build()
            }
            return INSTANCE
        }
    }
}