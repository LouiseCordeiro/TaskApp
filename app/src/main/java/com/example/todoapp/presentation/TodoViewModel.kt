package com.example.todoapp.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.model.Task
import com.example.todoapp.domain.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val applicationContext: Application
) : ViewModel() {

    fun getTasks(): StateFlow<List<Task>> {
        return taskRepository.getAllTasks()
            .stateIn(
                initialValue = listOf(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.add(task)
            scheduleNotification(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.update(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.delete(task)
        }
    }

    private fun scheduleNotification(task: Task) {
        NotificationScheduler.scheduleTaskReminder(applicationContext, task)
    }

}