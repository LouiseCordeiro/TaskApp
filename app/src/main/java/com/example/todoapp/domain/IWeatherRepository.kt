package com.example.todoapp.domain

interface IWeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double, apiKey: String): Double?
}