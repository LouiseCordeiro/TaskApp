package com.example.todoapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.domain.model.Task
import com.example.todoapp.domain.usecases.IAddTaskUseCase
import com.example.todoapp.domain.usecases.IDeleteTaskUseCase
import com.example.todoapp.domain.usecases.IGetAllTasksUseCase
import com.example.todoapp.domain.usecases.IGetCurrentWeatherUseCase
import com.example.todoapp.domain.usecases.IGetLocationUseCase
import com.example.todoapp.domain.usecases.IUpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

sealed class WeatherState {
    data object Loading : WeatherState()
    data class Success(val temperature: String) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: IGetCurrentWeatherUseCase,
    private val getLocationUseCase: IGetLocationUseCase,
    private val addTaskUseCase: IAddTaskUseCase,
    private val deleteTaskUseCase: IDeleteTaskUseCase,
    private val updateTaskUseCase: IUpdateTaskUseCase,
    private val getAllTasksUseCase: IGetAllTasksUseCase
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> get() = _weatherState

    private val decimalFormat = DecimalFormat("#")

    fun getCurrentWeather(apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _weatherState.value = WeatherState.Loading
            try {
                getLocationUseCase().collect { location ->
                    location?.let {
                        val temp = getCurrentWeatherUseCase(it.latitude, it.longitude, apiKey)
                        val formattedTemp = decimalFormat.format(temp)
                        _weatherState.value = WeatherState.Success(formattedTemp)
                    } ?: run {
                        _weatherState.value = WeatherState.Error("Location not available")
                    }
                }
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getAllTasks(): Flow<List<Task>> = getAllTasksUseCase()

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            addTaskUseCase(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTaskUseCase(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTaskUseCase(task)
        }
    }
}