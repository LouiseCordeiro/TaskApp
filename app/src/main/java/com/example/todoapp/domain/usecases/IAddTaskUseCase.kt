package com.example.todoapp.domain.usecases

import com.example.todoapp.domain.model.Task

interface IAddTaskUseCase {
    suspend operator fun invoke(task: Task)
}