package com.example.todoapp

import com.example.todoapp.data.remote.WeatherApiService
import com.example.todoapp.data.repositories.WeatherRepositoryImpl
import com.example.todoapp.domain.IWeatherRepository
import com.example.todoapp.data.model.WeatherResponse
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.mockk
import io.mockk.coEvery
import io.mockk.every
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {
    private val weatherApiService = mockk<WeatherApiService>()
    private val crashlytics = mockk<FirebaseCrashlytics>()

    private lateinit var repository: IWeatherRepository

    @Before
    fun setUp() {
        repository = WeatherRepositoryImpl(
            weatherApiService, crashlytics
        )
    }

    @Test
    fun `should get current temperature`() = runTest {
        val mockResponse = mockk<WeatherResponse> {
            every { main } returns mockk {
                every { temp } returns 25.78
            }
        }
        val lat = 37.7749
        val lon = -122.4194
        val apiKey = "fake_api_key"

        coEvery { weatherApiService.getCurrentWeather(lat, lon, apiKey) } returns mockResponse

        val temperature = repository.getCurrentWeather(lat, lon, apiKey)

        assertEquals(temperature,25.78)
    }

}