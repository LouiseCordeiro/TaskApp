package com.example.todoapp.domain

import com.example.todoapp.data.model.Task
import kotlinx.coroutines.flow.Flow


interface TaskRepository {

    fun getAllTasks() : Flow<List<Task>>

    fun getTask(taskId: Int) : Flow<Task>

    suspend fun add(task: Task)

    suspend fun update(task: Task)

    suspend fun delete(task: Task)
}