package com.example.todoapp.domain.usecases

interface IGetCurrentWeatherUseCase {
    suspend operator fun  invoke(lat: Double, lon: Double, apiKey: String) : Double?
}