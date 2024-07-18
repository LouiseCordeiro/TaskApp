package com.example.todoapp.domain.usecases

import com.example.todoapp.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface IGetAllTasksUseCase {
    operator fun invoke() : Flow<List<Task>>
}