package com.example.todoapp.domain.usecases

import com.example.todoapp.domain.model.Task

interface IDeleteTaskUseCase {
    suspend operator fun invoke(task: Task)
}