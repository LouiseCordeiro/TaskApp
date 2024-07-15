package com.example.todoapp.data.model

data class WeatherResponse(
    val main: Main
)

data class Main(
    val temp: Double
)
