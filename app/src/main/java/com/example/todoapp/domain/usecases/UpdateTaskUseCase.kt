package com.example.todoapp.domain.usecases

import com.example.todoapp.domain.ITaskRepository
import com.example.todoapp.domain.model.Task
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: ITaskRepository
) : IUpdateTaskUseCase {
    override suspend fun invoke(task: Task) {
        repository.update(task)
    }
}