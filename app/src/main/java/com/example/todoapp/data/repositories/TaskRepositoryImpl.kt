package com.example.todoapp.data.repositories

import com.example.todoapp.data.database.TaskDao
import com.example.todoapp.data.model.toEntity
import com.example.todoapp.data.model.toTask
import com.example.todoapp.domain.ITaskRepository
import com.example.todoapp.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao
) : ITaskRepository {
    override fun getAllTasks(): Flow<List<Task>> {
        return dao.getAllTasks().map { taskEntities ->
            taskEntities.map {
                it.toTask()
            }
        }
    }

    override suspend fun add(task: Task) {
        dao.add(task.toEntity())
    }

    override suspend fun update(task: Task) {
        dao.update(task.toEntity())
    }

    override suspend fun delete(task: Task) {
        dao.delete(task.toEntity())
    }
}