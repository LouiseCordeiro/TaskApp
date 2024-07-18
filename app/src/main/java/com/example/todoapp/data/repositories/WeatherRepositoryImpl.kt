package com.example.todoapp.data.repositories

import com.example.todoapp.data.remote.WeatherApiService
import com.example.todoapp.domain.IWeatherRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val crashlytics: FirebaseCrashlytics
) : IWeatherRepository {

    override suspend fun getCurrentWeather(lat: Double, lon: Double, apiKey: String) : Double? {
        return try {
            val response = weatherApiService.getCurrentWeather(lat, lon, apiKey)
            response.main.temp
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            null
        }
    }
}