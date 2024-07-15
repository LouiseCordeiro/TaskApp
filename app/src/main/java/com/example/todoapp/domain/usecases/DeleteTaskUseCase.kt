package com.example.todoapp.domain.usecases

import com.example.todoapp.data.model.Task
import com.example.todoapp.domain.ITaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: ITaskRepository
) : IDeleteTaskUseCase {
    override suspend fun invoke(task: Task) {
        repository.delete(task)
    }
}