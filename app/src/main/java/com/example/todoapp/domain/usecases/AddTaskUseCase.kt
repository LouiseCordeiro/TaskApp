package com.example.todoapp.domain.usecases

import com.example.todoapp.domain.ITaskRepository
import com.example.todoapp.domain.model.Task
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: ITaskRepository
) : IAddTaskUseCase{
    override suspend fun invoke(task: Task) {
        repository.add(task)
    }
}