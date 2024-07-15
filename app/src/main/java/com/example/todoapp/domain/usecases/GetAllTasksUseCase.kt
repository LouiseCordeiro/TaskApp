package com.example.todoapp.domain.usecases

import com.example.todoapp.domain.ITaskRepository
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(
    private val repository: ITaskRepository
) : IGetAllTasksUseCase{
    override fun invoke() = repository.getAllTasks()
}