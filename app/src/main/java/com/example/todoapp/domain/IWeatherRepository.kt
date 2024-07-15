package com.example.todoapp.domain

interface IWeatherRepository {
    suspend fun getCurrentTemperature(lat: Double, lon: Double, apiKey: String): Double?
}