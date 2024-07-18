package com.example.todoapp.domain.usecases

import com.example.todoapp.domain.IWeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: IWeatherRepository
) : IGetCurrentWeatherUseCase{
    override suspend fun invoke(lat: Double, lon: Double, apiKey: String) =
        repository.getCurrentWeather(lat, lon, apiKey)
}