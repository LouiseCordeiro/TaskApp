package com.example.todoapp.domain.usecases

import com.example.todoapp.domain.ITaskRepository
import com.example.todoapp.domain.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(
    private val repository: ITaskRepository
) : IGetAllTasksUseCase{
    override fun invoke() : Flow<List<Task>> = repository.getAllTasks()
}