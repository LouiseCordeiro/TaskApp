package com.example.todoapp.domain.usecases

import com.example.todoapp.data.model.Task
import kotlinx.coroutines.flow.Flow

interface IGetAllTasksUseCase {
    operator fun invoke() : Flow<List<Task>>
}