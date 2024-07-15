package com.example.todoapp.data.repositories

import com.example.todoapp.data.remote.WeatherApiService
import com.example.todoapp.domain.IWeatherRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val crashlytics: FirebaseCrashlytics
) : IWeatherRepository {

    override suspend fun getCurrentTemperature(lat: Double, lon: Double, apiKey: String) : Double? {
        return try {
            val response = weatherApiService.getCurrentWeather(lat, lon, apiKey)
            response.main.temp
        } catch (e: IOException) {
            handleException(e)
            null
        } catch (e: Exception) {
            handleException(e)
            null
        }
    }

    private fun handleException(e: Exception) {
        crashlytics.recordException(e)
        e.printStackTrace()
    }
}