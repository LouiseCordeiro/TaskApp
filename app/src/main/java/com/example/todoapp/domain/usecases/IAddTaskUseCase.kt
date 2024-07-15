package com.example.todoapp.domain.usecases

import com.example.todoapp.data.model.Task

interface IAddTaskUseCase {
    suspend operator fun invoke(task: Task)
}