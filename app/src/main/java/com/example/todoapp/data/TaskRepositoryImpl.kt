package com.example.todoapp.data

import com.example.todoapp.data.model.Task
import com.example.todoapp.domain.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao
) : TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> {
        return dao.getAllTasks()
    }

    override fun getTask(taskId: Int): Flow<Task> {
        return dao.getTask(taskId)
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