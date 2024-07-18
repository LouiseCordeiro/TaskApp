package com.example.todoapp.domain

import com.example.todoapp.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface ITaskRepository {

    fun getAllTasks() : Flow<List<Task>>

    suspend fun add(task: Task)

    suspend fun update(task: Task)

    suspend fun delete(task: Task)
}