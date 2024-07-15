package com.example.todoapp.data.repositories

import com.example.todoapp.data.database.TaskDao
import com.example.todoapp.data.model.Task
import com.example.todoapp.domain.ITaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao
) : ITaskRepository {
    override fun getAllTasks(): Flow<List<Task>> {
        return dao.getAllTasks()
    }

    override suspend fun add(task: Task) {
        dao.add(task)
    }

    override suspend fun update(task: Task) {
        dao.update(task)
    }

    override suspend fun delete(task: Task) {
        dao.delete(task)
    }
}